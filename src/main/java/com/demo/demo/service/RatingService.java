package com.demo.demo.service;

import com.demo.demo.dto.RatingDTO;
import com.demo.demo.entity.Feedback_Rating.Rating;
import com.demo.demo.repository.Feedback_Rating.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    public List<Rating> getAllRatings() {
        return ratingRepository.findAll();
    }

    public List<RatingDTO> getRatingsByCoachId(Integer coachId) {
        return ratingRepository.findByCoachId(coachId);
    }
}