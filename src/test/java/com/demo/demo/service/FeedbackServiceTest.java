package com.demo.demo.service;

import com.demo.demo.dto.FeedbackDTO;
import com.demo.demo.entity.Feedback_Rating.Feedback;
import com.demo.demo.repository.Feedback_Rating.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void testSaveFeedback_Success() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setUserId(1);
        feedback.setTitle("Test Feedback");
        feedback.setComment("Test Comment");

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        feedbackService.saveFeedback(feedback);

        // Assert
        assertNotNull(feedback.getCreatedAt());
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void testSaveFeedback_SetsCreatedAt() {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setUserId(1);
        feedback.setTitle("Test Feedback");

        LocalDateTime beforeSave = LocalDateTime.now().minusSeconds(1);

        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        // Act
        feedbackService.saveFeedback(feedback);

        // Assert
        assertNotNull(feedback.getCreatedAt());
        assertTrue(feedback.getCreatedAt().isAfter(beforeSave));
        assertTrue(feedback.getCreatedAt().isBefore(LocalDateTime.now().plusSeconds(1)));
        verify(feedbackRepository, times(1)).save(feedback);
    }

    @Test
    void testGetAllFeedbacks_Success() {
        // Arrange
        List<FeedbackDTO> expectedFeedbacks = Arrays.asList(
                new FeedbackDTO(1, "Title 1", "Comment 1", LocalDateTime.now(),
                        1, "User 1", null, 1, "tag1"),
                new FeedbackDTO(2, "Title 2", "Comment 2", LocalDateTime.now(),
                        2, "User 2", null, 1, "tag2"));

        when(feedbackRepository.findAllWithUserFullname()).thenReturn(expectedFeedbacks);

        // Act
        List<FeedbackDTO> result = feedbackService.getAllFeedbacks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Title 1", result.get(0).title());
        assertEquals("Title 2", result.get(1).title());
        verify(feedbackRepository, times(1)).findAllWithUserFullname();
    }

    @Test
    void testGetAllFeedbacks_EmptyResult() {
        // Arrange
        when(feedbackRepository.findAllWithUserFullname()).thenReturn(Arrays.asList());

        // Act
        List<FeedbackDTO> result = feedbackService.getAllFeedbacks();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(feedbackRepository, times(1)).findAllWithUserFullname();
    }

    @Test
    void testGetFeedbackById_Success() {
        // Arrange
        int userId = 1;
        List<Feedback> expectedFeedbacks = Arrays.asList(
                createTestFeedback(1, userId, "Feedback 1"),
                createTestFeedback(2, userId, "Feedback 2"));

        when(feedbackRepository.findByUserId(userId)).thenReturn(expectedFeedbacks);

        // Act
        List<Feedback> result = feedbackService.getFeedbackById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Feedback 1", result.get(0).getTitle());
        assertEquals("Feedback 2", result.get(1).getTitle());
        verify(feedbackRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testGetFeedbackById_NoFeedbacksFound() {
        // Arrange
        int userId = 999;
        when(feedbackRepository.findByUserId(userId)).thenReturn(Arrays.asList());

        // Act
        List<Feedback> result = feedbackService.getFeedbackById(userId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(feedbackRepository, times(1)).findByUserId(userId);
    }

    @Test
    void testDeleteFeedbackById_Success() {
        // Arrange
        int feedbackId = 1;
        doNothing().when(feedbackRepository).deleteById(feedbackId);

        // Act
        feedbackService.deleteFeedbackById(feedbackId);

        // Assert
        verify(feedbackRepository, times(1)).deleteById(feedbackId);
    }

    @Test
    void testDeleteFeedbackById_MultipleDeletes() {
        // Arrange
        int[] feedbackIds = { 1, 2, 3 };
        for (int id : feedbackIds) {
            doNothing().when(feedbackRepository).deleteById(id);
        }

        // Act
        for (int id : feedbackIds) {
            feedbackService.deleteFeedbackById(id);
        }

        // Assert
        verify(feedbackRepository, times(3)).deleteById(anyInt());
    }

    // Helper method to create test feedback
    private Feedback createTestFeedback(int id, int userId, String title) {
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setUserId(userId);
        feedback.setTitle(title);
        feedback.setComment("Test comment for " + title);
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setStatus(1);
        feedback.setTags("test");
        return feedback;
    }
}
