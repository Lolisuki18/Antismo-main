package com.demo.demo.service;

import com.demo.demo.dto.UserUpdateDTO;
import com.demo.demo.entity.User.User;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.User.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void testGetUserById_Success() {
        // Arrange
        Integer userId = 1;
        User mockUser = createTestUser(userId, "John Doe", "john@example.com", Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        User result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals("John Doe", result.getFullName());
        assertEquals("john@example.com", result.getEmail());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserById_UserNotFound() {
        // Arrange
        Integer userId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserById(userId));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testUpdateUser_Success() {
        // Arrange
        Integer userId = 1;
        User existingUser = createTestUser(userId, "Old Name", "old@example.com", Role.USER);
        UserUpdateDTO updateDTO = new UserUpdateDTO("New Name", "123456789", "http://newavatar.com/avatar.jpg");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // Act
        User result = userService.updateUser(userId, updateDTO);

        // Assert
        assertNotNull(result);
        assertEquals("New Name", result.getFullName());
        assertEquals("http://newavatar.com/avatar.jpg", result.getAvatarUrl());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testUpdateUser_UserNotFound() {
        // Arrange
        Integer userId = 999;
        UserUpdateDTO updateDTO = new UserUpdateDTO("New Name", "123456789", "http://avatar.com");
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.updateUser(userId, updateDTO));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetAllUsers_Success() {
        // Arrange
        List<User> mockUsers = Arrays.asList(
                createTestUser(1, "User 1", "user1@example.com", Role.USER),
                createTestUser(2, "User 2", "user2@example.com", Role.COACH),
                createTestUser(3, "User 3", "user3@example.com", Role.ADMIN));
        when(userRepository.findAll()).thenReturn(mockUsers);

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("User 1", result.get(0).getFullName());
        assertEquals("User 2", result.get(1).getFullName());
        assertEquals("User 3", result.get(2).getFullName());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testGetAllUsers_EmptyList() {
        // Arrange
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<User> result = userService.getAllUsers();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testChangeUserRole_Success() {
        // Arrange
        Integer userId = 1;
        User mockUser = createTestUser(userId, "John Doe", "john@example.com", Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        // Act
        userService.changeUserRole(userId, "COACH");

        // Assert
        assertEquals(Role.COACH, mockUser.getRole());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(mockUser);
    }

    @Test
    void testChangeUserRole_UserNotFound() {
        // Arrange
        Integer userId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.changeUserRole(userId, "COACH"));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testChangeUserRole_InvalidRole() {
        // Arrange
        Integer userId = 1;
        User mockUser = createTestUser(userId, "John Doe", "john@example.com", Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.changeUserRole(userId, "INVALID_ROLE"));
        assertEquals("Invalid role name", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDelete_Success() {
        // Arrange
        Integer userId = 1;
        doNothing().when(userRepository).deleteById(userId);

        // Act
        userService.delete(userId);

        // Assert
        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testGetUserNameAndMailById_Success() {
        // Arrange
        Integer userId = 1;
        User mockUser = createTestUser(userId, "John Doe", "john@example.com", Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // Act
        Object result = userService.getUserNameAndMailById(userId);

        // Assert
        assertNotNull(result);
        // Use reflection to check the anonymous object properties
        try {
            String name = (String) result.getClass().getMethod("getName").invoke(result);
            String email = (String) result.getClass().getMethod("getEmail").invoke(result);
            assertEquals("John Doe", name);
            assertEquals("john@example.com", email);
        } catch (Exception e) {
            fail("Failed to access anonymous object methods");
        }
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserNameAndMailById_UserNotFound() {
        // Arrange
        Integer userId = 999;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.getUserNameAndMailById(userId));
        assertEquals("User not found", exception.getMessage());
        verify(userRepository, times(1)).findById(userId);
    }

    // Helper method to create test user
    private User createTestUser(Integer id, String fullName, String email, Role role) {
        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(role);
        return user;
    }
}
