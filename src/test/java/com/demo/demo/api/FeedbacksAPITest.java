package com.demo.demo.api;

import com.demo.demo.dto.FeedbackDTO;
import com.demo.demo.entity.Feedback_Rating.Feedback;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.Feedback_Rating.FeedbackRepository;
import com.demo.demo.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class FeedbacksAPITest {

    private MockMvc mockMvc;

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbacksAPI feedbacksAPI;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(feedbacksAPI).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void testGetAllFeedbacksWithUser_Success() throws Exception {
        // Arrange
        List<FeedbackDTO> mockFeedbacks = Arrays.asList(
                new FeedbackDTO(1, "Great app!", "Love the features",
                        LocalDateTime.now(), 1, "John Doe", Role.USER, 1, "positive"),
                new FeedbackDTO(2, "Bug report", "Found an issue",
                        LocalDateTime.now(), 2, "Jane Smith", Role.USER, 0, "bug"));

        when(feedbackService.getAllFeedbacks()).thenReturn(mockFeedbacks);

        // Act & Assert
        mockMvc.perform(get("/api/feedbacks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].title").value("Great app!"))
                .andExpect(jsonPath("$[0].comment").value("Love the features"))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Bug report"));

        verify(feedbackService, times(1)).getAllFeedbacks();
    }

    @Test
    void testGetAllFeedbacksWithUser_EmptyList() throws Exception {
        // Arrange
        when(feedbackService.getAllFeedbacks()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/feedbacks")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(feedbackService, times(1)).getAllFeedbacks();
    }

    @Test
    void testCreateFeedback_Success() throws Exception {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setId(1);
        feedback.setUserId(1);
        feedback.setTitle("Test Feedback");
        feedback.setComment("This is a test feedback");
        feedback.setStatus(1);
        feedback.setTags("test");

        doNothing().when(feedbackService).saveFeedback(any(Feedback.class));

        // Act & Assert
        mockMvc.perform(post("/api/feedbacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.title").value("Test Feedback"))
                .andExpect(jsonPath("$.comment").value("This is a test feedback"))
                .andExpect(jsonPath("$.status").value(1))
                .andExpect(jsonPath("$.tags").value("test"));

        verify(feedbackService, times(1)).saveFeedback(any(Feedback.class));
    }

    @Test
    void testCreateFeedback_WithMinimalData() throws Exception {
        // Arrange
        Feedback feedback = new Feedback();
        feedback.setUserId(1);
        feedback.setTitle("Minimal Feedback");

        doNothing().when(feedbackService).saveFeedback(any(Feedback.class));

        // Act & Assert
        mockMvc.perform(post("/api/feedbacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(feedback)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1))
                .andExpect(jsonPath("$.title").value("Minimal Feedback"));

        verify(feedbackService, times(1)).saveFeedback(any(Feedback.class));
    }

    @Test
    void testGetFeedbackById_Success() throws Exception {
        // Arrange
        int userId = 1;
        List<Feedback> mockFeedbacks = Arrays.asList(
                createMockFeedback(1, userId, "Feedback 1", "Comment 1"),
                createMockFeedback(2, userId, "Feedback 2", "Comment 2"));

        when(feedbackService.getFeedbackById(userId)).thenReturn(mockFeedbacks);

        // Act & Assert
        mockMvc.perform(get("/api/feedbacks/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].userId").value(userId))
                .andExpect(jsonPath("$[0].title").value("Feedback 1"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].title").value("Feedback 2"));

        verify(feedbackService, times(1)).getFeedbackById(userId);
    }

    @Test
    void testGetFeedbackById_NoFeedbacksFound() throws Exception {
        // Arrange
        int userId = 999;
        when(feedbackService.getFeedbackById(userId)).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/feedbacks/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(feedbackService, times(1)).getFeedbackById(userId);
    }

    @Test
    void testDeleteFeedbackById_Success() throws Exception {
        // Arrange
        int feedbackId = 1;
        doNothing().when(feedbackService).deleteFeedbackById(feedbackId);

        // Act & Assert
        mockMvc.perform(delete("/api/feedbacks/{id}", feedbackId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Feedback with ID " + feedbackId + " has been deleted successfully."));

        verify(feedbackService, times(1)).deleteFeedbackById(feedbackId);
    }

    @Test
    void testDeleteFeedbackById_MultipleIds() throws Exception {
        // Test deleting multiple different feedback IDs
        int[] feedbackIds = { 1, 2, 3 };

        for (int id : feedbackIds) {
            doNothing().when(feedbackService).deleteFeedbackById(id);

            mockMvc.perform(delete("/api/feedbacks/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Feedback with ID " + id + " has been deleted successfully."));
        }

        verify(feedbackService, times(3)).deleteFeedbackById(anyInt());
    }

    // Helper method to create mock feedback objects
    private Feedback createMockFeedback(int id, int userId, String title, String comment) {
        Feedback feedback = new Feedback();
        feedback.setId(id);
        feedback.setUserId(userId);
        feedback.setTitle(title);
        feedback.setComment(comment);
        feedback.setCreatedAt(LocalDateTime.now());
        feedback.setStatus(1);
        feedback.setTags("test");
        return feedback;
    }
}
