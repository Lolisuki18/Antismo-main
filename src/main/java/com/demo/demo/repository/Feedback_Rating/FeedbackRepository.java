package com.demo.demo.repository.Feedback_Rating;

import com.demo.demo.dto.FeedbackDTO;
import com.demo.demo.entity.Feedback_Rating.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.util.List;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByUserId(int id);

    void deleteById(int id);

    @Query("SELECT new com.demo.demo.dto.FeedbackDTO(f.id, f.title, f.comment, f.createdAt, f.userId, u.fullName,u.role, f.status, f.tags) " +
            "FROM Feedback f JOIN User u ON f.userId = u.id")
    List<FeedbackDTO> findAllWithUserFullname();

}
