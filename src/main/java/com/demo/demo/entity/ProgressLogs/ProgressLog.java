package com.demo.demo.entity.ProgressLogs;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.OffsetDateTime;


@Entity
@Table(name = "ProgressLogs")
@Getter
@Setter
public class ProgressLog {

   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;

    @Column(name = "user_id")
    private int userId;

    @Column(name = "time")
    private OffsetDateTime createdAt;

    @Column(name = "smoked")
    private int smoked;

    @Column(name = "log_type")
    private int logType;

    @Column(name = "cravingIntensity")
    private int cravingIntensity;

    @Column(name = "emotion")
    private int emotion;

    @Column(name = "context")
    private String context;

    public LocalDate getDate() {
        return createdAt.toLocalDate();
    }
}
