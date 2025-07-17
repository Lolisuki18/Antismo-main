package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CoachInfoDTO {
    private int userId;
    private String name;
    private String avatarUrl;
    private Integer sessions;
    private Integer rating;
    private String experience;
    private String attendance;
    private LocalDateTime joined;
    private String title;
    private String bio;
    private String email;
    private String bookingUrl;

    public CoachInfoDTO(int userId, String name, String avatarUrl, Integer sessions, Integer rating, String experience, String attendance, LocalDateTime joined, String title, String bio, String email, String bookingUrl) {
        this.userId = userId;
        this.name = name;
        this.avatarUrl = avatarUrl;
        this.sessions = sessions;
        this.rating = rating;
        this.experience = experience;
        this.attendance = attendance;
        this.joined = joined;
        this.title = title;
        this.bio = bio;
        this.email = email;
        this.bookingUrl = bookingUrl;
    }
}
