package com.demo.demo.service;

import com.demo.demo.dto.RatingDTO;
import com.demo.demo.entity.Feedback_Rating.Rating;
import com.demo.demo.repository.Feedback_Rating.RatingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingService ratingService;

    @Test
    void testGetAllRatings_Success() {
        // Arrange
        List<Rating> mockRatings = Arrays.asList(
                createTestRating(1, 1, 101, 5, "Excellent coach!"),
                createTestRating(2, 2, 101, 4, "Very good support"),
                createTestRating(3, 3, 102, 3, "Average experience"),
                createTestRating(4, 4, 103, 5, "Outstanding guidance"));
        when(ratingRepository.findAll()).thenReturn(mockRatings);

        // Act
        List<Rating> result = ratingService.getAllRatings();

        // Assert
        assertNotNull(result);
        assertEquals(4, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(3, result.get(2).getId());
        assertEquals(4, result.get(3).getId());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void testGetAllRatings_EmptyList() {
        // Arrange
        when(ratingRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Rating> result = ratingService.getAllRatings();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void testGetAllRatings_SingleRating() {
        // Arrange
        List<Rating> singleRating = Arrays.asList(
                createTestRating(1, 1, 101, 4, "Good experience"));
        when(ratingRepository.findAll()).thenReturn(singleRating);

        // Act
        List<Rating> result = ratingService.getAllRatings();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(1, result.get(0).getUserId());
        assertEquals(101, result.get(0).getCoachId());
        assertEquals(4, result.get(0).getRating());
        verify(ratingRepository, times(1)).findAll();
    }

    @Test
    void testGetRatingsByCoachId_Success() {
        // Arrange
        Integer coachId = 101;
        List<RatingDTO> mockRatingDTOs = Arrays.asList(
                createTestRatingDTO(1, 1, coachId, 5, "Excellent!", "John Doe", "avatar1.jpg"),
                createTestRatingDTO(2, 2, coachId, 4, "Very helpful", "Jane Smith", "avatar2.jpg"),
                createTestRatingDTO(3, 3, coachId, 5, "Outstanding", "Bob Johnson", "avatar3.jpg"));
        when(ratingRepository.findByCoachId(coachId)).thenReturn(mockRatingDTOs);

        // Act
        List<RatingDTO> result = ratingService.getRatingsByCoachId(coachId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(coachId, result.get(0).getCoachId());
        assertEquals(coachId, result.get(1).getCoachId());
        assertEquals(coachId, result.get(2).getCoachId());
        assertEquals("John Doe", result.get(0).getUserFullName());
        assertEquals("Jane Smith", result.get(1).getUserFullName());
        assertEquals("Bob Johnson", result.get(2).getUserFullName());
        verify(ratingRepository, times(1)).findByCoachId(coachId);
    }

    @Test
    void testGetRatingsByCoachId_NoRatings() {
        // Arrange
        Integer coachId = 999;
        when(ratingRepository.findByCoachId(coachId)).thenReturn(Arrays.asList());

        // Act
        List<RatingDTO> result = ratingService.getRatingsByCoachId(coachId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(ratingRepository, times(1)).findByCoachId(coachId);
    }

    @Test
    void testGetRatingsByCoachId_SingleRating() {
        // Arrange
        Integer coachId = 102;
        List<RatingDTO> singleRatingDTO = Arrays.asList(
                createTestRatingDTO(1, 1, coachId, 3, "Average", "Alice Brown", "avatar.jpg"));
        when(ratingRepository.findByCoachId(coachId)).thenReturn(singleRatingDTO);

        // Act
        List<RatingDTO> result = ratingService.getRatingsByCoachId(coachId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(coachId, result.get(0).getCoachId());
        assertEquals(3, result.get(0).getRating());
        assertEquals("Average", result.get(0).getText());
        assertEquals("Alice Brown", result.get(0).getUserFullName());
        verify(ratingRepository, times(1)).findByCoachId(coachId);
    }

    @Test
    void testGetRatingsByCoachId_MultipleCoaches() {
        // Arrange
        Integer coachId1 = 101;
        Integer coachId2 = 102;

        List<RatingDTO> coach1Ratings = Arrays.asList(
                createTestRatingDTO(1, 1, coachId1, 5, "Excellent!", "User 1", "avatar1.jpg"));
        List<RatingDTO> coach2Ratings = Arrays.asList(
                createTestRatingDTO(2, 2, coachId2, 4, "Good!", "User 2", "avatar2.jpg"));

        when(ratingRepository.findByCoachId(coachId1)).thenReturn(coach1Ratings);
        when(ratingRepository.findByCoachId(coachId2)).thenReturn(coach2Ratings);

        // Act
        List<RatingDTO> result1 = ratingService.getRatingsByCoachId(coachId1);
        List<RatingDTO> result2 = ratingService.getRatingsByCoachId(coachId2);

        // Assert
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(1, result1.size());
        assertEquals(1, result2.size());
        assertEquals(coachId1, result1.get(0).getCoachId());
        assertEquals(coachId2, result2.get(0).getCoachId());
        assertEquals(5, result1.get(0).getRating());
        assertEquals(4, result2.get(0).getRating());

        verify(ratingRepository, times(1)).findByCoachId(coachId1);
        verify(ratingRepository, times(1)).findByCoachId(coachId2);
    }

    @Test
    void testGetRatingsByCoachId_HighRatingsOnly() {
        // Arrange
        Integer coachId = 103;
        List<RatingDTO> highRatings = Arrays.asList(
                createTestRatingDTO(1, 1, coachId, 5, "Perfect!", "Happy User 1", "avatar1.jpg"),
                createTestRatingDTO(2, 2, coachId, 5, "Amazing!", "Happy User 2", "avatar2.jpg"),
                createTestRatingDTO(3, 3, coachId, 4, "Great!", "Happy User 3", "avatar3.jpg"));
        when(ratingRepository.findByCoachId(coachId)).thenReturn(highRatings);

        // Act
        List<RatingDTO> result = ratingService.getRatingsByCoachId(coachId);

        // Assert
        assertNotNull(result);
        assertEquals(3, result.size());
        assertTrue(result.stream().allMatch(r -> r.getRating() >= 4));
        assertTrue(result.stream().allMatch(r -> r.getCoachId() == coachId));
        verify(ratingRepository, times(1)).findByCoachId(coachId);
    }

    // Helper methods
    private Rating createTestRating(Integer id, Integer userId, Integer coachId, Integer rating, String text) {
        Rating r = new Rating();
        r.setId(id);
        r.setUserId(userId);
        r.setCoachId(coachId);
        r.setRating(rating);
        r.setText(text);
        r.setCreatedAt(LocalDateTime.now());
        return r;
    }

    private RatingDTO createTestRatingDTO(Integer id, Integer userId, Integer coachId, Integer rating, String text,
            String userFullName, String avatarUrl) {
        return new RatingDTO(id, userId, coachId, userFullName, avatarUrl, rating, text, LocalDateTime.now());
    }
}
