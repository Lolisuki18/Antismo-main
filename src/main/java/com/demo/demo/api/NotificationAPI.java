package com.demo.demo.api;

import com.demo.demo.dto.BulkNotificationRequest;
import com.demo.demo.dto.NotificationDto;
import com.demo.demo.dto.RoleNotificationRequest;
import com.demo.demo.entity.Notification.AdminNotificationRequest;
import com.demo.demo.entity.Notification.BroadcastNotificationRequest;
import com.demo.demo.entity.Notification.Notification;
import com.demo.demo.repository.Notification.NotificationRepository;
import com.demo.demo.repository.User.UserRepository;
import com.demo.demo.service.NotificationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notifications")
public class NotificationAPI {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationService adminService;

    public NotificationAPI(
            NotificationRepository notificationRepository,
            UserRepository userRepository, NotificationService adminService
    ) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
        this.adminService = adminService;
    }

    @GetMapping
    public List<NotificationDto> listAll(@AuthenticationPrincipal(expression="id") Integer uid) {
        return notificationRepository.findByUserIdOrderBySentAtDesc(uid)
                .stream()
                .map(NotificationDto::fromEntity)
                .collect(Collectors.toList());
    }

    @PostMapping("/{id}/mark-read")
    public ResponseEntity<Void> markRead(
            @PathVariable Integer id,
            @AuthenticationPrincipal(expression="id") Integer uid
    ) {
        Notification n = notificationRepository.findById(id)
                .filter(x -> x.getUserId().equals(uid))
                .orElseThrow(() -> new RuntimeException("Forbidden/Not found"));
        n.setRead(true);
        notificationRepository.save(n);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/mark-all-read")
    public ResponseEntity<Void> markAllRead(
            @AuthenticationPrincipal(expression="id") Integer uid
    ) {
        notificationRepository.findByUserIdOrderBySentAtDesc(uid).forEach(n -> {
            if (!n.getRead()) {
                n.setRead(true);
                notificationRepository.save(n);
            }
        });
        return ResponseEntity.noContent().build();
    }
// Toggle notification setting for the user
    @PostMapping("/toggle-noti/{action}")
    public ResponseEntity<Void> toggleNoti(
            @PathVariable String action,
            @AuthenticationPrincipal(expression="id") Integer uid
    ) {
        var u = userRepository.findById(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if ("off".equals(action)) u.setIsNoti(false);
        else if ("on".equals(action)) u.setIsNoti(true);
        else return ResponseEntity.badRequest().build();
        userRepository.save(u);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/send")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ResponseEntity<Void> sendToUser(
            @Valid @RequestBody AdminNotificationRequest req
    ) {
        adminService.sendToUser(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Gửi broadcast notification cho tất cả user
     */
    @PostMapping("/broadcast")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ResponseEntity<Void> broadcast(
            @Valid @RequestBody BroadcastNotificationRequest req
    ) {
        adminService.broadcast(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Gửi notification tới tất cả user thuộc một Role nhất định.
     */
    @PostMapping("/send-role")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ResponseEntity<Void> sendToRole(
            @Valid @RequestBody RoleNotificationRequest req
    ) {
        adminService.sendToRole(req);
        return ResponseEntity.ok().build();
    }

    /**
     * Gửi notification tới danh sách user IDs cụ thể.
     */
    @PostMapping("/send-bulk")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COACH')")
    public ResponseEntity<Void> sendToUsers(
            @Valid @RequestBody BulkNotificationRequest req
    ) {
        adminService.sendToUsers(req);
        return ResponseEntity.ok().build();
    }

}
