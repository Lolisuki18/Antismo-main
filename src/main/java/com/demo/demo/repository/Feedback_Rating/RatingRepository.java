package com.demo.demo.repository.Feedback_Rating;

import com.demo.demo.dto.RatingDTO;
import com.demo.demo.entity.Feedback_Rating.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Integer> {

    @Query("SELECT NEW com.demo.demo.dto.RatingDTO(r.id, r.userId, r.coachId, u.fullName, u.avatarUrl, r.rating, r.text, r.createdAt) FROM Rating r JOIN User u ON r.userId= u.id WHERE r.coachId = ?1")
    List<RatingDTO> findByCoachId(Integer coachId);
}
