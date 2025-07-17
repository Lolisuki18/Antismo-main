package com.demo.demo.api;

import com.demo.demo.dto.FeedbackDTO;
import com.demo.demo.entity.Feedback_Rating.Feedback;
import com.demo.demo.repository.Feedback_Rating.FeedbackRepository;
import com.demo.demo.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/feedbacks")
@CrossOrigin(origins = "*")
public class FeedbacksAPI {

    @Autowired
    private FeedbackService feedbackService;

    @Autowired
    private FeedbackRepository feedbackRepository;

    @GetMapping
    public ResponseEntity<?> getAllFeedbacksWithUser() {
        List<FeedbackDTO> results = feedbackService.getAllFeedbacks();
        return ResponseEntity.ok(results);
    }

    @PostMapping
    public Feedback createFeedback(@RequestBody Feedback feedback) {
        feedbackService.saveFeedback(feedback);
        return feedback;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable("id") int id) {
        List<Feedback> results = feedbackService.getFeedbackById(id);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteFeedbackById(@PathVariable("id") int id) {
        feedbackService.deleteFeedbackById(id);
        return ResponseEntity.ok("Feedback with ID " + id + " has been deleted successfully.");
    }
    
}
