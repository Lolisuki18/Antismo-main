package com.demo.demo.api;

import com.demo.demo.dto.DefaultQuitPlanResponse;
import com.demo.demo.entity.DefaultQuitPlan.DefaultQuitPlan;
import com.demo.demo.entity.SmokingStatus.SmokingStatus;
import com.demo.demo.service.DefaultQuitPlanService;
import com.demo.demo.service.SmokingStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/default-quit-plans")
@CrossOrigin(origins = "*")
public class DefaultAPI {

    @Autowired
    private DefaultQuitPlanService defaultQuitPlanService;

    @Autowired
    private SmokingStatusService smokingStatusService;

    @PostMapping
    public ResponseEntity<?> createDefaultQuitPlan(@RequestBody DefaultQuitPlan defaultQuitPlanRequest) {
        defaultQuitPlanService.saveDefaultQuitPlan(defaultQuitPlanRequest);
        return ResponseEntity.ok(defaultQuitPlanRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDefaultQuitPlanByUserId(@PathVariable("id") int id) {
        defaultQuitPlanService.deleteDefaultQuitPlanById(id);
        return ResponseEntity.ok(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getQuitPlanByUserId(@PathVariable("id") int id) {
        // Fetch the default quit plan and smoking status by user ID
        Optional<DefaultQuitPlan> defaultQuitPlan = defaultQuitPlanService.getDefaultQuitPlanByUserId(id);
        Optional<SmokingStatus> smokingStatus = smokingStatusService.getByUserId(id);

        // If either the quit plan or smoking status is not found, return 404 Not Found
        DefaultQuitPlanResponse response;
        if (defaultQuitPlan.isEmpty() || smokingStatus.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            //Calculate the progress of the quit plan
            int progress = 0;
            LocalDate startDate = defaultQuitPlan.get().getStartDate();
            LocalDate targetDate = defaultQuitPlan.get().getTargetDate();
            LocalDate currentDate = LocalDate.now();

            long totalDays = ChronoUnit.DAYS.between(startDate, targetDate);
            long daysPassed = ChronoUnit.DAYS.between(startDate, currentDate);

            if (totalDays > 0) {
                progress = (int) Math.min(100, Math.max(0, (daysPassed * 100) / totalDays));
            }

            // Create the response object
            response = new DefaultQuitPlanResponse(
                defaultQuitPlan.get().getId(),
                defaultQuitPlan.get().getStartDate().toString(),
                defaultQuitPlan.get().getTargetDate().toString(),
                progress,
                smokingStatus.get().getCigarettesPerDay(),
                defaultQuitPlan.get().getQuitReason(),
                defaultQuitPlan.get().getMoneyTarget(),
                defaultQuitPlan.get().getNote()
            );
        }

        return ResponseEntity.ok(response);
    }

}
