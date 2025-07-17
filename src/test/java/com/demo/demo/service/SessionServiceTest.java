package com.demo.demo.service;

import com.demo.demo.dto.SessionInfoDTO;
import com.demo.demo.entity.Session.Session;
import com.demo.demo.repository.Session.SessionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @InjectMocks
    private SessionService sessionService;

    @Test
    void testSaveSession_Success() {
        // Arrange
        Session session = createTestSession("session1", 1, "Test Summary");
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        // Act
        sessionService.saveSession(session);

        // Assert
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    void testGetSession_Success() {
        // Arrange
        String sessionId = "session1";
        Session mockSession = createTestSession(sessionId, 1, "Test Summary");
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        // Act
        Optional<Session> result = sessionService.getSession(sessionId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(sessionId, result.get().getId());
        assertEquals("Test Summary", result.get().getSummary());
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void testGetSession_NotFound() {
        // Arrange
        String sessionId = "nonexistent";
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // Act
        Optional<Session> result = sessionService.getSession(sessionId);

        // Assert
        assertFalse(result.isPresent());
        verify(sessionRepository, times(1)).findById(sessionId);
    }

    @Test
    void testGetAllSessions_Success() {
        // Arrange
        List<Session> mockSessions = Arrays.asList(
                createTestSession("session1", 1, "Summary 1"),
                createTestSession("session2", 2, "Summary 2"),
                createTestSession("session3", 1, "Summary 3"));
        when(sessionRepository.findAll()).thenReturn(mockSessions);

        // Act
        List<Session> result = sessionService.getAllSessions();

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals("Summary 1", result.get(0).getSummary());
        assertEquals("Summary 2", result.get(1).getSummary());
        assertEquals("Summary 3", result.get(2).getSummary());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetAllSessions_EmptyList() {
        // Arrange
        when(sessionRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Session> result = sessionService.getAllSessions();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    void testGetSessionsByUserId_Success() {
        // Arrange
        int userId = 1;
        List<SessionInfoDTO> mockSessionInfos = Arrays.asList(
                createTestSessionInfoDTO("session1", 1, 101, "Coach 1", LocalDateTime.now()),
                createTestSessionInfoDTO("session2", 1, 102, "Coach 2", LocalDateTime.now().minusDays(1)));
        when(sessionRepository.findByUserId(userId)).thenReturn(mockSessionInfos);

        // Act
        List<SessionInfoDTO> result = sessionService.getSessionsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Coach 1", result.get(0).getCoachName());
        assertEquals("Coach 2", result.get(1).getCoachName());
        verify(sessionRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetSessionsByUserId_NoSessions() {
        // Arrange
        int userId = 999;
        when(sessionRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        // Act
        List<SessionInfoDTO> result = sessionService.getSessionsByUserId(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(sessionRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testDeleteSession_Success() {
        // Arrange
        String sessionId = "session1";
        doNothing().when(sessionRepository).deleteById(sessionId);

        // Act
        sessionService.deleteSession(sessionId);

        // Assert
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testDeleteSession_MultipleDeletes() {
        // Arrange
        String[] sessionIds = { "session1", "session2", "session3" };
        for (String id : sessionIds) {
            doNothing().when(sessionRepository).deleteById(id);
        }

        // Act
        for (String id : sessionIds) {
            sessionService.deleteSession(id);
        }

        // Assert
        verify(sessionRepository, times(3)).deleteById(anyString());
    }

    // Helper methods
    private Session createTestSession(String id, int userId, String summary) {
        Session session = new Session();
        session.setId(id);
        session.setUserId(userId);
        session.setCoachId(101);
        session.setSummary(summary);
        session.setStartTime(LocalDateTime.now());
        session.setEndTime(LocalDateTime.now().plusHours(1));
        return session;
    }

    private SessionInfoDTO createTestSessionInfoDTO(String id, int userId, int coachId, String coachName,
            LocalDateTime startTime) {
        return new SessionInfoDTO(id, userId, coachId, coachName, "http://avatar.com", startTime,
                startTime.plusHours(1));
    }
}
