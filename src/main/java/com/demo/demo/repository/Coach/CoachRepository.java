package com.demo.demo.repository.Coach;

import com.demo.demo.dto.CoachInfoDTO;
import com.demo.demo.entity.Coach.Coach;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CoachRepository extends JpaRepository<Coach, Integer> {

    @Query("SELECT new com.demo.demo.dto.CoachInfoDTO(c.userId, u.fullName, u.avatarUrl, c.sessions, c.rating, " +
            "c.experience, c.attendance, u.createdAt, c.title, c.bio, u.email, c.bookingUrl) " +
            "FROM Coach c JOIN User u ON c.userId = u.id")
    List<CoachInfoDTO> findAllCoachesInfo();

    @Query("SELECT new com.demo.demo.dto.CoachInfoDTO(c.userId, u.fullName, u.avatarUrl, c.sessions, c.rating, " +
            "c.experience, c.attendance, u.createdAt, c.title, c.bio, u.email, c.bookingUrl) " +
            "FROM Coach c JOIN User u ON c.userId = u.id WHERE c.userId = ?1")
    Optional<CoachInfoDTO> findCoachInfoById(int id);
}
