package com.demo.demo.api;

import com.demo.demo.dto.ProgressLogRequest;
import com.demo.demo.entity.ProgressLogs.ProgressLog;
import com.demo.demo.service.ProgressLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/progress-logs")
public class ProgressLogAPI {

    @Autowired
    private ProgressLogService service;

    @GetMapping("/{id}")
    public ResponseEntity<List<ProgressLog>> getLogById(@PathVariable("id") Integer id) {
        List<ProgressLog> progressLogsByUserId = service.getProgressLogsByUserId(id);
        return ResponseEntity.ok(progressLogsByUserId);
    }

    @PostMapping
    public ResponseEntity<ProgressLog> createLog(@RequestBody ProgressLog log) {
        ProgressLog savedLog = service.saveLog(log);
        return ResponseEntity.ok(savedLog);
    }

    @DeleteMapping("/{id}")
    public void deleteLog(@PathVariable("id") Integer id) {
        service.deleteLog(id);
    }
}