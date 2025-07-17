package com.demo.demo.service;

import com.demo.demo.entity.User.User;
import com.demo.demo.entity.User.UserPackage;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.Progress_logs.ProgressLogRepository;
import com.demo.demo.repository.User.UserPackageRepository;
import com.demo.demo.repository.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.*;

@Service
@RequiredArgsConstructor
public class NotificationSchedulerService {

    private final UserRepository userRepository;
    private final ProgressLogRepository logRepository;
    private final NotificationService notificationService;
    private final UserPackageRepository packageRepo;
    /**
     * 1. NO_LOG: nếu user chưa tạo log trong ngày (sau khung giờ), chỉ với isNoti = true
     */
    @Scheduled(cron = "${app.daily-log-reminder.cron}", zone = "Asia/Bangkok")
    public void detectMissingDailyLog() {
        LocalDate today = LocalDate.now(ZoneId.of("Asia/Bangkok"));
        OffsetDateTime start = today.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime end   = today.plusDays(1).atStartOfDay().atOffset(ZoneOffset.ofHours(7));

        for (User u : userRepository.findAllActiveNotiUsers()) {
            if (!logRepository.existsByUserIdAndCreatedAtBetween(u.getId(), start, end)) {
                notificationService.createAndSend(
                        u.getId(),
                        "Is there a log today? I hope you have one!",
                        "Have you made your log today? Please remember to update your daily logs - " + today,
                        u.getEmail()
                );
            }
        }
    }

    /**
     * 2. WARNING_DELETE: sau 5 ngày không log, chỉ với isNoti = true
     */
    @Scheduled(cron = "${app.warning-delete.cron}", zone = "Asia/Bangkok")
    public void warnAccountDeletion() {
        LocalDate target = LocalDate.now(ZoneId.of("Asia/Bangkok")).minusDays(5);
        OffsetDateTime s = target.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime e = target.plusDays(1).atStartOfDay().atOffset(ZoneOffset.ofHours(7));

        for (User u : userRepository.findAllActiveNotiUsers()) {
            if (!logRepository.existsByUserIdAndCreatedAtBetween(u.getId(), s, e)) {
                notificationService.createAndSend(
                        u.getId(),
                        "Is your account going to be deleted? Please log today!",
                        "Your account will be unactivated if you continue not recording logs.",
                        u.getEmail()
                );
            }
        }
    }

    /**
     * 3. FINAL_DELETE & immediate deactivation: sau 1 tuần không log,
     *    gửi FINAL_DELETE và ngay lập tức khoá tài khoản (bất kể isNoti).
     */
    @Scheduled(cron = "${app.final-warning.cron}", zone = "Asia/Bangkok")
    public void finalWarningAndDeactivate() {
        LocalDate target = LocalDate.now(ZoneId.of("Asia/Bangkok")).minusWeeks(1);
        OffsetDateTime s = target.atStartOfDay().atOffset(ZoneOffset.ofHours(7));
        OffsetDateTime e = target.plusDays(1).atStartOfDay().atOffset(ZoneOffset.ofHours(7));

        // Lấy tất cả user active (bỏ qua isNoti)
        for (User u : userRepository.findAllByIsActiveTrue()) {
            if (!logRepository.existsByUserIdAndCreatedAtBetween(u.getId(), s, e)) {
                // 3a. Gửi cảnh báo xoá tài khoản
                notificationService.createAndSend(
                        u.getId(),
                        "I cannot find your logs for a week! You have been warned.",
                        "Tài khoản của bạn sẽ bị xoá ngay lập tức do không ghi nhật ký trong 1 tuần.",
                        u.getEmail()
                );
                // 3b. Ngay lập tức khoá tài khoản
                u.setIsActive(false);
                userRepository.save(u);
            }
        }
    }
    /**
     * 1. Nhắc trước 5 ngày hủy gói
     */
    @Scheduled(cron = "${app.package-before-cancel.cron}", zone = "Asia/Bangkok")
    public void remindBeforeCancel() {
        LocalDateTime threshold = LocalDateTime.now(ZoneId.of("Asia/Bangkok"))
                .plusDays(5);
        for (UserPackage up : packageRepo.findDueBeforeCancel(threshold)) {
            userRepository.findById(up.getUserId()).ifPresent(u -> {
                if (Boolean.TRUE.equals(u.getIsActive())) {
                    notificationService.createAndSend(
                            u.getId(),
                            "Your Antismo day is coming to an end",
                            "Your amazing Antismo package will expire in 5 days, exactly on"
                                    + up.getExpiredAt().toLocalDate(),
                            u.getEmail()
                    );
                }
            });
            up.setNotifiedBeforeCancel(true);
            packageRepo.save(up);
        }
    }

    /**
     * 2. Hủy ngay khi đến hạn
     */
    @Scheduled(cron = "${app.package-cancel.cron}", zone = "Asia/Bangkok")
    public void processCancel() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("Asia/Bangkok"));
        for (UserPackage up : packageRepo.findDueCancel(now)) {
            userRepository.findById(up.getUserId()).ifPresent(u -> {
                if (Boolean.TRUE.equals(u.getIsActive())) {
                    // gửi notification hết hạn
                    notificationService.createAndSend(
                            u.getId(),
                            "Oh no! Your Antismo package has expired",
                            "Your Antismo package has expired as of "
                                    + up.getExpiredAt().toLocalDate()
                                    + ". Please consider renewing it to continue enjoying our services.",
                            u.getEmail()
                    );
                    // hạ role về USER
                    u.setRole(Role.USER);
                    userRepository.save(u);
                }
            });
            up.setNotifiedCanceled(true);
            packageRepo.save(up);
        }
    }
    // Đã loại bỏ phương thức deactivateOverdueAccounts()
}
