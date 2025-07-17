package com.demo.demo.api;

import com.demo.demo.dto.CigaretteProgressDTO;
import com.demo.demo.service.AnalyticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/analytics")
@CrossOrigin(origins = "*")
public class AnalyticsAPI {

    @Autowired
    AnalyticsService analyticsService;

    @GetMapping("/progress/{id}")
    public ResponseEntity<?> getSevenDayProgress(@PathVariable("id") int id) {
        List<CigaretteProgressDTO> result = analyticsService.getProgressFromStartOfPlan(id);
        return ResponseEntity.ok(result);
    }
}
