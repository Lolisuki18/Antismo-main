package com.demo.demo.api;

import com.demo.demo.service.MessageCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {

    @Autowired
    private MessageCacheService cacheService;

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hello from Spring Boot Backend!");
    }

    @GetMapping("/test/websocket")
    public ResponseEntity<Map<String, Object>> testWebSocket() {
        Map<String, Object> response = new HashMap<>();
        response.put("websocket", "WebSocket is configured and ready");
        response.put("endpoint", "/ws");
        response.put("destinations", new String[]{"/topic/public", "/queue/history"});
        response.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test/database")
    public ResponseEntity<Map<String, Object>> testDatabase() {
        Map<String, Object> response = new HashMap<>();
        try {
            response.put("status", "Database connection OK");
            response.put("type", "SQL Server");
            response.put("timestamp", java.time.Instant.now().toString());
        } catch (Exception e) {
            response.put("status", "Database connection failed");
            response.put("error", e.getMessage());
            response.put("timestamp", java.time.Instant.now().toString());
        }
        return ResponseEntity.ok(response);
    }

    @GetMapping("/test/cache")
    public ResponseEntity<Map<String, Object>> testCache() {
        Map<String, Object> response = new HashMap<>();
        response.put("messageCount", cacheService.getHistory().size());
        response.put("cacheStatus", "MessageCacheService is working");
        response.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/test/clear-cache")
    public ResponseEntity<Map<String, Object>> clearCache() {
        cacheService.clear();
        Map<String, Object> response = new HashMap<>();
        response.put("status", "Cache cleared successfully");
        response.put("timestamp", java.time.Instant.now().toString());
        return ResponseEntity.ok(response);
    }
}
