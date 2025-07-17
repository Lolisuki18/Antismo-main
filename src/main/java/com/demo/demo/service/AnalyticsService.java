package com.demo.demo.service;

import com.demo.demo.dto.CigaretteProgressDTO;
import com.demo.demo.entity.DefaultQuitPlan.DefaultQuitPlan;
import com.demo.demo.entity.ProgressLogs.ProgressLog;
import com.demo.demo.entity.SmokingStatus.SmokingStatus;
import com.demo.demo.repository.DefaultQuitPlan.DefaultQuitPlanRepository;
import com.demo.demo.repository.Progress_logs.ProgressLogRepository;
import com.demo.demo.repository.SmokingStatus.SmokingStatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AnalyticsService {

    @Autowired
    private ProgressLogRepository progressLogRepository;

    @Autowired
    private SmokingStatusRepository smokingStatusRepository;

    @Autowired
    private DefaultQuitPlanRepository defaultQuitPlanRepository;

    public List<CigaretteProgressDTO> getProgressFromStartOfPlan(Integer userId) {
        SmokingStatus smokingStatus = smokingStatusRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Smoking status not found for user ID: " + userId));
        DefaultQuitPlan defaultQuitPlan = defaultQuitPlanRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Default quit plan not found for user ID: " + userId));
        List<ProgressLog> logs = progressLogRepository.findByUserId(userId);

        LocalDate today = LocalDate.now();
        LocalDate startDate = defaultQuitPlan.getStartDate();
        LocalDate targetDate = defaultQuitPlan.getTargetDate();
        long totalPlanDays = java.time.temporal.ChronoUnit.DAYS.between(startDate, targetDate);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd");

        Long currentSmoked = smokingStatus.getCigarettesPerDay();

        // Filter logs from start date to today, then group by date and sum smoked
        Map<LocalDate, Integer> smokedPerDay = logs.stream()
                .filter(log -> {
                    LocalDate logDate = log.getDate();
                    boolean isInRange = !logDate.isBefore(startDate) && !logDate.isAfter(today);
                    System.out.println("Log date: " + logDate + ", in range: " + isInRange);
                    return isInRange;
                })
                .collect(Collectors.groupingBy(
                        ProgressLog::getDate,
                        Collectors.summingInt(ProgressLog::getSmoked)
                ));

        List<CigaretteProgressDTO> result = new ArrayList<>();

        // Generate results from start date to today
        LocalDate currentDate = startDate;
        while (!currentDate.isAfter(today)) {
            int smoked = smokedPerDay.getOrDefault(currentDate, 0);

            // Calculate allowed based on entire plan duration
            long daysSinceStart = startDate.until(currentDate).getDays();

            // Calculate allowed cigarettes based on the current smoked amount and days since start
            int allowed = Math.round(currentSmoked * (1 - (float)daysSinceStart / totalPlanDays));
            allowed = Math.max(0, allowed); // Ensure allowed doesn't go below 0

            // If current date is after target date, set allowed to 0
            result.add(new CigaretteProgressDTO(
                    currentDate.format(formatter),
                    smoked,
                    allowed
            ));

            // Move to the next day
            currentDate = currentDate.plusDays(1);
        }

        return result;
    }
}