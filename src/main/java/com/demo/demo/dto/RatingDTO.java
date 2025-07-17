package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RatingDTO {
    private int id;
    private int userId;
    private int coachId;
    private String userFullName;
    private String userAvatarUrl;
    private int rating;
    private String text;
    private LocalDateTime createdAt;

    public RatingDTO(int id, int userId, int coachId, String userFullName, String userAvatarUrl, int rating, String text, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.coachId = coachId;
        this.userFullName = userFullName;
        this.userAvatarUrl = userAvatarUrl;
        this.rating = rating;
        this.text = text;
        this.createdAt = createdAt;
    }
}
