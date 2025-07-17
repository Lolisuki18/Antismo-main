package com.demo.demo.repository.Progress_logs;

import com.demo.demo.entity.ProgressLogs.ProgressLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;

public interface ProgressLogRepository extends JpaRepository<ProgressLog, Long> {
    List<ProgressLog> findByUserId(int userId);
    void deleteById(int id);

    @Query("""
      SELECT CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END
      FROM ProgressLog p
      WHERE p.userId = :userId
        AND p.createdAt >= :startOfDay
        AND p.createdAt < :endOfDay
    """)
    boolean existsByUserIdAndCreatedAtBetween(
            @Param("userId") int userId,
            @Param("startOfDay") OffsetDateTime startOfDay,
            @Param("endOfDay")   OffsetDateTime endOfDay
    );
}

