package com.demo.demo.service;

import com.demo.demo.dto.FeedbackDTO;
import com.demo.demo.entity.Feedback_Rating.Feedback;
import com.demo.demo.repository.Feedback_Rating.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    public void saveFeedback(Feedback feedback) {
        feedback.setCreatedAt(java.time.LocalDateTime.now());
        feedbackRepository.save(feedback);
    }

    public List<FeedbackDTO> getAllFeedbacks() {
        return feedbackRepository.findAllWithUserFullname();
    }

    public List<Feedback> getFeedbackById(int id) {
        return feedbackRepository.findByUserId(id);
    }

    public void deleteFeedbackById(int id) {
        feedbackRepository.deleteById(id);
    }


}
