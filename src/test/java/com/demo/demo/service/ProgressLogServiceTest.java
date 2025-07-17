package com.demo.demo.service;

import com.demo.demo.entity.ProgressLogs.ProgressLog;
import com.demo.demo.repository.Progress_logs.ProgressLogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProgressLogServiceTest {

    @Mock
    private ProgressLogRepository repository;

    @InjectMocks
    private ProgressLogService progressLogService;

    @Test
    void testGetProgressLogsByUserId_Success() {
        // Arrange
        Integer userId = 1;
        List<ProgressLog> mockLogs = Arrays.asList(
                createTestProgressLog(1L, userId, 5, 3, "Morning craving"),
                createTestProgressLog(2L, userId, 0, 2, "Afternoon resistance"),
                createTestProgressLog(3L, userId, 2, 4, "Evening struggle"));
        when(repository.findByUserId(userId)).thenReturn(mockLogs);

        // Act
        List<ProgressLog> result = progressLogService.getProgressLogsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(userId, result.get(0).getUserId());
        assertEquals(5, result.get(0).getSmoked());
        assertEquals(0, result.get(1).getSmoked());
        assertEquals(2, result.get(2).getSmoked());
        verify(repository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetProgressLogsByUserId_NoLogs() {
        // Arrange
        Integer userId = 999;
        when(repository.findByUserId(userId)).thenReturn(Arrays.asList());

        // Act
        List<ProgressLog> result = progressLogService.getProgressLogsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetProgressLogsByUserId_MultipleUsers() {
        // Arrange
        Integer userId1 = 1;
        Integer userId2 = 2;

        List<ProgressLog> user1Logs = Arrays.asList(
                createTestProgressLog(1L, userId1, 3, 2, "User 1 log"));
        List<ProgressLog> user2Logs = Arrays.asList(
                createTestProgressLog(2L, userId2, 1, 1, "User 2 log"));

        when(repository.findByUserId(userId1)).thenReturn(user1Logs);
        when(repository.findByUserId(userId2)).thenReturn(user2Logs);

        // Act
        List<ProgressLog> result1 = progressLogService.getProgressLogsByUserId(userId1);
        List<ProgressLog> result2 = progressLogService.getProgressLogsByUserId(userId2);

        // Assert
        assertEquals(1, result1.size());
        assertEquals(userId1, result1.get(0).getUserId());
        assertEquals(3, result1.get(0).getSmoked());

        assertEquals(1, result2.size());
        assertEquals(userId2, result2.get(0).getUserId());
        assertEquals(1, result2.get(0).getSmoked());

        verify(repository, times(1)).findByUserId(userId1);
        verify(repository, times(1)).findByUserId(userId2);
    }

    @Test
    void testSaveLog_Success() {
        // Arrange
        ProgressLog log = createTestProgressLog(null, 1, 2, 3, "New log entry");
        ProgressLog savedLog = createTestProgressLog(1L, 1, 2, 3, "New log entry");
        when(repository.save(any(ProgressLog.class))).thenReturn(savedLog);

        // Act
        ProgressLog result = progressLogService.saveLog(log);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(1, result.getUserId());
        assertEquals(2, result.getSmoked());
        assertEquals(3, result.getCravingIntensity());
        verify(repository, times(1)).save(log);
    }

    @Test
    void testSaveLog_UpdateExisting() {
        // Arrange
        ProgressLog existingLog = createTestProgressLog(1L, 1, 0, 1, "Updated log");
        when(repository.save(any(ProgressLog.class))).thenReturn(existingLog);

        // Act
        ProgressLog result = progressLogService.saveLog(existingLog);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(0, result.getSmoked());
        assertEquals(1, result.getCravingIntensity());
        verify(repository, times(1)).save(existingLog);
    }

    @Test
    void testSaveLog_MultipleLogs() {
        // Arrange
        ProgressLog log1 = createTestProgressLog(null, 1, 3, 4, "Log 1");
        ProgressLog log2 = createTestProgressLog(null, 2, 1, 2, "Log 2");
        ProgressLog savedLog1 = createTestProgressLog(1L, 1, 3, 4, "Log 1");
        ProgressLog savedLog2 = createTestProgressLog(2L, 2, 1, 2, "Log 2");

        when(repository.save(log1)).thenReturn(savedLog1);
        when(repository.save(log2)).thenReturn(savedLog2);

        // Act
        ProgressLog result1 = progressLogService.saveLog(log1);
        ProgressLog result2 = progressLogService.saveLog(log2);

        // Assert
        assertEquals(1L, result1.getId());
        assertEquals(1, result1.getUserId());
        assertEquals(2L, result2.getId());
        assertEquals(2, result2.getUserId());
        verify(repository, times(2)).save(any(ProgressLog.class));
    }

    @Test
    void testDeleteLog_Success() {
        // Arrange
        Integer logId = 1;
        doNothing().when(repository).deleteById(logId);

        // Act
        progressLogService.deleteLog(logId);

        // Assert
        verify(repository, times(1)).deleteById(logId);
    }

    @Test
    void testDeleteLog_MultipleDeletions() {
        // Arrange
        Integer[] logIds = { 1, 2, 3 };
        for (Integer id : logIds) {
            doNothing().when(repository).deleteById(id);
        }

        // Act
        for (Integer id : logIds) {
            progressLogService.deleteLog(id);
        }

        // Assert
        verify(repository, times(3)).deleteById(any(Integer.class));
    }

    @Test
    void testDeleteLog_NonExistentId() {
        // Arrange
        Integer nonExistentId = 999;
        doNothing().when(repository).deleteById(nonExistentId);

        // Act
        progressLogService.deleteLog(nonExistentId);

        // Assert
        verify(repository, times(1)).deleteById(nonExistentId);
    }

    // Helper method
    private ProgressLog createTestProgressLog(Long id, Integer userId, int smoked, int cravingIntensity,
            String context) {
        ProgressLog log = new ProgressLog();
        log.setId(id);
        log.setUserId(userId);
        log.setCreatedAt(OffsetDateTime.now());
        log.setSmoked(smoked);
        log.setLogType(1); // Default log type
        log.setCravingIntensity(cravingIntensity);
        log.setEmotion(1); // Default emotion
        log.setContext(context);
        return log;
    }
}
