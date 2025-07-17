package com.demo.demo.entity.Session;

import com.demo.demo.dto.GoogleEventDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table (name = "Sessions")
@Getter
@Setter
public class Session {
    @Id
    private String id;

    @Column(name = "summary", nullable = false)
    private String summary;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "coach_id", nullable = false)
    private int coachId;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

}
