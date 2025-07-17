package com.demo.demo.repository.User;

import com.demo.demo.dto.AssignedUserCoachResponse;
import com.demo.demo.entity.User.UserCoach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserCoachRepository extends JpaRepository<UserCoach, Integer> {
  @Query("SELECT NEW com.demo.demo.dto.AssignedUserCoachResponse(u.id, u.fullName, u.email, ss.cigarettesPerDay) FROM User u LEFT JOIN SmokingStatus ss ON u.id = ss.userId JOIN UserCoach uc ON uc.userId = u.id WHERE uc.coachId = :coachId")
    Optional<List<AssignedUserCoachResponse>> findByCoachId(@Param("coachId") int coachId);
}
