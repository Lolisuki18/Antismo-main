package com.demo.demo.service;

import com.demo.demo.dto.BulkNotificationRequest;
import com.demo.demo.dto.RoleNotificationRequest;
import com.demo.demo.entity.Notification.AdminNotificationRequest;
import com.demo.demo.entity.Notification.BroadcastNotificationRequest;
import com.demo.demo.entity.Notification.Notification;
import com.demo.demo.entity.User.User;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.Notification.NotificationRepository;
import com.demo.demo.dto.MailBody;
import com.demo.demo.repository.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    @Transactional
    public void createAndSend(
            int userId,
            String type,
            String message,
            String userEmail
    ) {
        Notification notif = Notification.builder()
                .userId(userId)
                .type(type)
                .message(message)
                .read(false)
                .build();

        notificationRepository.save(notif);

        // gửi email
        emailService.sendSimpleMail(new MailBody(userEmail, type, message));
    }

    public List<Notification> getByTypeAndSentAtBetween(
            String type,
            OffsetDateTime start,
            OffsetDateTime end
    ) {
        return notificationRepository.findByTypeAndSentAtBetween(type, start, end);
    }

    @Transactional
    public void sendToUser(AdminNotificationRequest req) {
        User u = userRepository.findById(req.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        if (!u.getIsActive()) {
            throw new IllegalStateException("Cannot notify inactive user");
        }
        createAndSend(
                u.getId(),
                req.getType(),
                req.getMessage(),
                u.getEmail()
        );
    }

    /**
     * Gửi notification tới tất cả user đang active & bật nhận noti
     */
    @Transactional
    public void broadcast(BroadcastNotificationRequest req) {
        for (User u : userRepository.findAllActiveNotiUsers()) {
            createAndSend(
                    u.getId(),
                    req.getType(),
                    req.getMessage(),
                    u.getEmail()
            );
        }
    }
    /** Gửi notification tới tất cả user có role nhất định (và isActive=true). */
    @Transactional
    public void sendToRole(RoleNotificationRequest req) {
        Role targetRole = Role.valueOf(req.getRole().toUpperCase());
        for (User u : userRepository.findByRoleAndIsActiveTrue(targetRole)) {
            createAndSend(
                    u.getId(),
                    req.getType(),
                    req.getMessage(),
                    u.getEmail()
            );
        }
    }

    /** Gửi notification tới danh sách userId cụ thể. */
    @Transactional
    public void sendToUsers(BulkNotificationRequest req) {
        for (Integer uid : req.getUserIds()) {
            userRepository.findById(uid).ifPresent(u -> {
                if (Boolean.TRUE.equals(u.getIsActive())) {
                    createAndSend(
                            u.getId(),
                            req.getType(),
                            req.getMessage(),
                            u.getEmail()
                    );
                }
            });
        }
    }
}
