package com.demo.demo.service;

import com.demo.demo.dto.BulkNotificationRequest;
import com.demo.demo.dto.MailBody;
import com.demo.demo.dto.RoleNotificationRequest;
import com.demo.demo.entity.Notification.AdminNotificationRequest;
import com.demo.demo.entity.Notification.BroadcastNotificationRequest;
import com.demo.demo.entity.Notification.Notification;
import com.demo.demo.entity.User.User;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.Notification.NotificationRepository;
import com.demo.demo.repository.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testCreateAndSend_Success() {
        // Arrange
        int userId = 1;
        String type = "REMINDER";
        String message = "Test notification message";
        String userEmail = "test@example.com";

        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());
        doNothing().when(emailService).sendSimpleMail(any(MailBody.class));

        // Act
        notificationService.createAndSend(userId, type, message, userEmail);

        // Assert
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(emailService, times(1)).sendSimpleMail(any(MailBody.class));
    }

    @Test
    void testGetByTypeAndSentAtBetween_Success() {
        // Arrange
        String type = "REMINDER";
        OffsetDateTime start = OffsetDateTime.now().minusDays(1);
        OffsetDateTime end = OffsetDateTime.now();
        List<Notification> mockNotifications = Arrays.asList(
                createTestNotification(1, type, "Message 1"),
                createTestNotification(2, type, "Message 2"));

        when(notificationRepository.findByTypeAndSentAtBetween(type, start, end))
                .thenReturn(mockNotifications);

        // Act
        List<Notification> result = notificationService.getByTypeAndSentAtBetween(type, start, end);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Message 1", result.get(0).getMessage());
        assertEquals("Message 2", result.get(1).getMessage());
        verify(notificationRepository, times(1)).findByTypeAndSentAtBetween(type, start, end);
    }

    @Test
    void testSendToUser_Success() {
        // Arrange
        AdminNotificationRequest request = new AdminNotificationRequest();
        request.setUserId(1);
        request.setType("ADMIN_MESSAGE");
        request.setMessage("Admin notification");

        User mockUser = createTestUser(1, "John Doe", "john@example.com", true);
        when(userRepository.findById(1)).thenReturn(Optional.of(mockUser));
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());
        doNothing().when(emailService).sendSimpleMail(any(MailBody.class));

        // Act
        notificationService.sendToUser(request);

        // Assert
        verify(userRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(emailService, times(1)).sendSimpleMail(any(MailBody.class));
    }

    @Test
    void testSendToUser_UserNotFound() {
        // Arrange
        AdminNotificationRequest request = new AdminNotificationRequest();
        request.setUserId(999);
        request.setType("ADMIN_MESSAGE");
        request.setMessage("Admin notification");

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> notificationService.sendToUser(request));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(999);
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(emailService, never()).sendSimpleMail(any(MailBody.class));
    }

    @Test
    void testSendToUser_InactiveUser() {
        // Arrange
        AdminNotificationRequest request = new AdminNotificationRequest();
        request.setUserId(1);
        request.setType("ADMIN_MESSAGE");
        request.setMessage("Admin notification");

        User inactiveUser = createTestUser(1, "John Doe", "john@example.com", false);
        when(userRepository.findById(1)).thenReturn(Optional.of(inactiveUser));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class,
                () -> notificationService.sendToUser(request));
        assertEquals("Cannot notify inactive user", exception.getMessage());
        verify(userRepository, times(1)).findById(1);
        verify(notificationRepository, never()).save(any(Notification.class));
        verify(emailService, never()).sendSimpleMail(any(MailBody.class));
    }

    @Test
    void testBroadcast_Success() {
        // Arrange
        BroadcastNotificationRequest request = new BroadcastNotificationRequest();
        request.setType("BROADCAST");
        request.setMessage("Broadcast message");

        List<User> activeUsers = Arrays.asList(
                createTestUser(1, "User 1", "user1@example.com", true),
                createTestUser(2, "User 2", "user2@example.com", true));

        when(userRepository.findAllActiveNotiUsers()).thenReturn(activeUsers);
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());
        doNothing().when(emailService).sendSimpleMail(any(MailBody.class));

        // Act
        notificationService.broadcast(request);

        // Assert
        verify(userRepository, times(1)).findAllActiveNotiUsers();
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(emailService, times(2)).sendSimpleMail(any(MailBody.class));
    }

    @Test
    void testSendToRole_Success() {
        // Arrange
        RoleNotificationRequest request = new RoleNotificationRequest();
        request.setRole("USER");
        request.setType("ROLE_MESSAGE");
        request.setMessage("Message for users");

        List<User> usersWithRole = Arrays.asList(
                createTestUser(1, "User 1", "user1@example.com", true),
                createTestUser(2, "User 2", "user2@example.com", true));

        when(userRepository.findByRoleAndIsActiveTrue(Role.USER)).thenReturn(usersWithRole);
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());
        doNothing().when(emailService).sendSimpleMail(any(MailBody.class));

        // Act
        notificationService.sendToRole(request);

        // Assert
        verify(userRepository, times(1)).findByRoleAndIsActiveTrue(Role.USER);
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(emailService, times(2)).sendSimpleMail(any(MailBody.class));
    }

    @Test
    void testSendToUsers_Success() {
        // Arrange
        BulkNotificationRequest request = new BulkNotificationRequest();
        request.setUserIds(Arrays.asList(1, 2, 3));
        request.setType("BULK_MESSAGE");
        request.setMessage("Bulk notification");

        User user1 = createTestUser(1, "User 1", "user1@example.com", true);
        User user2 = createTestUser(2, "User 2", "user2@example.com", true);
        User user3 = createTestUser(3, "User 3", "user3@example.com", false); // inactive

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(2)).thenReturn(Optional.of(user2));
        when(userRepository.findById(3)).thenReturn(Optional.of(user3));
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());
        doNothing().when(emailService).sendSimpleMail(any(MailBody.class));

        // Act
        notificationService.sendToUsers(request);

        // Assert
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findById(2);
        verify(userRepository, times(1)).findById(3);
        // Only 2 notifications should be sent (user3 is inactive)
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(emailService, times(2)).sendSimpleMail(any(MailBody.class));
    }

    @Test
    void testSendToUsers_SomeUsersNotFound() {
        // Arrange
        BulkNotificationRequest request = new BulkNotificationRequest();
        request.setUserIds(Arrays.asList(1, 999));
        request.setType("BULK_MESSAGE");
        request.setMessage("Bulk notification");

        User user1 = createTestUser(1, "User 1", "user1@example.com", true);

        when(userRepository.findById(1)).thenReturn(Optional.of(user1));
        when(userRepository.findById(999)).thenReturn(Optional.empty());
        when(notificationRepository.save(any(Notification.class))).thenReturn(new Notification());
        doNothing().when(emailService).sendSimpleMail(any(MailBody.class));

        // Act
        notificationService.sendToUsers(request);

        // Assert
        verify(userRepository, times(1)).findById(1);
        verify(userRepository, times(1)).findById(999);
        // Only 1 notification should be sent (user 999 not found)
        verify(notificationRepository, times(1)).save(any(Notification.class));
        verify(emailService, times(1)).sendSimpleMail(any(MailBody.class));
    }

    // Helper methods
    private Notification createTestNotification(int id, String type, String message) {
        return Notification.builder()
                .id(id)
                .type(type)
                .message(message)
                .read(false)
                .build();
    }

    private User createTestUser(int id, String fullName, String email, boolean isActive) {
        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setIsActive(isActive);
        user.setRole(Role.USER);
        return user;
    }
}
