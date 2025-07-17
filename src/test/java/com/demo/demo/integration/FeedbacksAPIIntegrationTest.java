package com.demo.demo.integration;

import com.demo.demo.dto.FeedbackDTO;
import com.demo.demo.entity.Feedback_Rating.Feedback;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.Feedback_Rating.FeedbackRepository;
import com.demo.demo.service.FeedbackService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureWebMvc
@TestPropertySource(properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration"
})
public class FeedbacksAPIIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FeedbackService feedbackService;

    @MockBean
    private FeedbackRepository feedbackRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testFeedbacksAPIIntegration_FullWorkflow() throws Exception {
        // Test GET all feedbacks
        List<FeedbackDTO> mockFeedbacks = Arrays.asList(
                new FeedbackDTO(1, "Integration Test", "Testing the full workflow",
                        LocalDateTime.now(), 1, "Test User", Role.USER, 1, "integration"));

        when(feedbackService.getAllFeedbacks()).thenReturn(mockFeedbacks);

        mockMvc.perform(get("/api/feedbacks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].title").value("Integration Test"));

        // Test POST create feedback
        Feedback newFeedback = new Feedback();
        newFeedback.setUserId(1);
        newFeedback.setTitle("New Integration Feedback");
        newFeedback.setComment("Created via integration test");
        newFeedback.setStatus(1);

        doNothing().when(feedbackService).saveFeedback(any(Feedback.class));

        mockMvc.perform(post("/api/feedbacks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newFeedback)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("New Integration Feedback"));

        // Test GET feedback by user ID
        List<Feedback> userFeedbacks = Arrays.asList(newFeedback);
        when(feedbackService.getFeedbackById(1)).thenReturn(userFeedbacks);

        mockMvc.perform(get("/api/feedbacks/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        // Test DELETE feedback
        doNothing().when(feedbackService).deleteFeedbackById(1);

        mockMvc.perform(delete("/api/feedbacks/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Feedback with ID 1 has been deleted successfully."));

        // Verify all service methods were called
        verify(feedbackService, times(1)).getAllFeedbacks();
        verify(feedbackService, times(1)).saveFeedback(any(Feedback.class));
        verify(feedbackService, times(1)).getFeedbackById(1);
        verify(feedbackService, times(1)).deleteFeedbackById(1);
    }

    @Test
    void testErrorScenarios() throws Exception {
        // Test getting feedbacks for non-existent user
        when(feedbackService.getFeedbackById(999)).thenReturn(Arrays.asList());

        mockMvc.perform(get("/api/feedbacks/999"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));

        verify(feedbackService, times(1)).getFeedbackById(999);
    }
}
