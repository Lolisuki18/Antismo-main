package com.demo.demo.entity.Coach;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Coaches")
@Getter
@Setter
public class Coach {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "userId", nullable = false)
    private Integer userId;

    @Column(name = "sessions")
    private Integer sessions;

    @Column(name = "rating")
    private Integer rating;

    @Column(name = "experience", length = 50)
    private String experience;

    @Column(name = "attendance", length = 10)
    private String attendance;

    @Column(name = "joined")
    private java.time.LocalDateTime joined;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "booking_url")
    private String bookingUrl;
}
