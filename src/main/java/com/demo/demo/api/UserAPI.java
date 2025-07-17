package com.demo.demo.api;

import com.demo.demo.dto.UserUpdateDTO;
import com.demo.demo.entity.User.User;
import com.demo.demo.repository.User.UserRepository;
import com.demo.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
        }

        // Use the email (username) to fetch the User entity
        User user = userRepository.findUserByEmail(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Build the response DTO
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", user.getId());
        userInfo.put("email", user.getEmail());
        userInfo.put("fullName", user.getFullName());
        userInfo.put("dateOfBirth", user.getDateOfBirth());
        userInfo.put("avatarUrl", user.getAvatarUrl());
        userInfo.put("role", user.getRole());

        return ResponseEntity.ok(userInfo);
    }

    // GET /api/users/{id}
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // PUT /api/users/{id}
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")

    public ResponseEntity<User> updateUser(
            @PathVariable("id") Integer id,
            @RequestBody UserUpdateDTO dto
    ) {
        User updatedUser = userService.updateUser(id, dto);
        return ResponseEntity.ok(updatedUser);
    }

    // GET /api/users - Lấy toàn bộ user
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/role")
    public ResponseEntity<?> changeRole(
            @PathVariable("id") Integer id,
            @RequestParam String roleName
    ) {
        userService.changeUserRole(id, roleName);
        return ResponseEntity.ok("Role changed!");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == principal.id")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id  ) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    
}
