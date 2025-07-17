package com.demo.demo.entity.QuitPlan;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "QuitPlans")
@Getter
@Setter
public class QuitPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Integer planId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "coach_id")
    private Integer coachId;

    @Column(name = "status")
    private Integer status; // 1: ontrack, 2: struggling, 3: at risk

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "target_date")
    private LocalDate targetDate;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "isEnded")
    private Boolean isEnded;

    @Column(name = "stage_two_duration")
    private int stageTwoDuration;

    @Override
    public String toString() {
        return "QuitPlan{" +
                "planId=" + planId +
                ", userId=" + userId +
                ", coachId=" + coachId +
                ", status=" + status +
                ", startDate=" + startDate +
                ", targetDate=" + targetDate +
                ", reason='" + reason + '\'' +
                ", createdAt=" + createdAt +
                ", isEnded=" + isEnded +
                ", stageTwoDuration=" + stageTwoDuration +
                '}';
    }
}
