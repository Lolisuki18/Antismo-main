package com.demo.demo.api;

import com.demo.demo.dto.AssignUserCoachRequest;
import com.demo.demo.dto.AssignedUserCoachResponse;
import com.demo.demo.entity.User.UserCoach;
import com.demo.demo.repository.User.UserCoachRepository;
import com.demo.demo.service.UserCoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-coach/")
public class UserCoachAPI {

   @Autowired
    private UserCoachService userCoachService;

   @Autowired
   private UserCoachRepository userCoachRepository;

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUsersAssigned(@PathVariable("id") int coachId) {
        Optional<List<AssignedUserCoachResponse>> users = userCoachService.findUsersAssigned(coachId);
        return ResponseEntity.ok(users);
    }

    @PostMapping("/users")
    public ResponseEntity<?> assignUsersToCoach(@RequestBody AssignUserCoachRequest request) {
        UserCoach userCoach = new UserCoach();
        userCoach.setCoachId(request.getCoachId());
        userCoach.setUserId(request.getUserId());
        userCoach.setCreatedAt(LocalDateTime.now());

        userCoachRepository.save(userCoach);
        return ResponseEntity.ok(userCoach);
    }
}
