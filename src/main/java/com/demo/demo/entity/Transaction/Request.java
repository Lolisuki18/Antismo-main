package com.demo.demo.entity.Transaction;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Requests")
public class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "coach_id", nullable = false)
    private Integer coachId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

}

