package com.demo.demo.repository.Session;

import com.demo.demo.dto.SessionInfoDTO;
import com.demo.demo.entity.Session.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {
    Optional<Boolean> getSessionById(String id);

    boolean existsById(String id);

    @Query("SELECT new com.demo.demo.dto.SessionInfoDTO(s.id, s.userId, s.coachId, u.fullName, u.avatarUrl, s.startTime, s.endTime) FROM Session s JOIN User u ON u.id = s.coachId WHERE s.userId = ?1")
    List<SessionInfoDTO> findByUserId(int id);
}