package com.demo.demo.api;

import com.demo.demo.dto.RatingDTO;
import com.demo.demo.entity.Feedback_Rating.Rating;
import com.demo.demo.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@CrossOrigin(origins = "*")
public class RatingAPI {

    @Autowired
    private RatingService ratingService;

    @GetMapping
    public List<Rating> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("{coachId}")
    private List<RatingDTO> getRatingById(@PathVariable("coachId") int id) {
        return ratingService.getRatingsByCoachId(id);
    }
}