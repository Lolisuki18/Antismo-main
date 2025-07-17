package com.demo.demo.api;

import com.demo.demo.entity.SmokingStatus.SmokingStatus;
import com.demo.demo.service.SmokingStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/smoking-statuses")
@CrossOrigin(origins = "*")
public class SmokingAPI {

    @Autowired
    private SmokingStatusService smokingStatusService;

   @GetMapping("/me")
   @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SmokingStatus> getMySmokingStatus() {
        // Get the current authenticated user's email
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        // Log for debugging
        System.out.println("Current authenticated user email: " + email);
        
        if (email == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }
        
        return smokingStatusService.getByUserEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<SmokingStatus> create(@RequestBody SmokingStatus status) {
        System.out.println("Received fullname from frontend: " + status.getFullName());
        SmokingStatus savedStatus = smokingStatusService.create(status);
        return ResponseEntity.ok(savedStatus);
    }

//    @GetMapping
//    public ResponseEntity<List<SmokingStatus>> getByUserId(@RequestParam Long userId) {
//        return ResponseEntity.ok(smokingStatusService.getByUserId(userId));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<SmokingStatus> getById(@PathVariable("id") Integer id) {
        return smokingStatusService.getByUserId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @PutMapping("/{id}")
//    public ResponseEntity<SmokingStatus> update(@PathVariable Long id, @RequestBody SmokingStatus updated) {
//        return ResponseEntity.ok(smokingStatusService.update(id, updated));
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id) {
        smokingStatusService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
