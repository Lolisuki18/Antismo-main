package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class SessionInfoDTO {
    private String id;
    private int userId;
    private int coachId;
    private String coachName;
    private String coachAvatarUrl;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public SessionInfoDTO(String id, int userId, int coachId, String coachName, String coachAvatarUrl, LocalDateTime startTime, LocalDateTime endTime) {
        this.id = id;
        this.userId = userId;
        this.coachId = coachId;
        this.coachName = coachName;
        this.coachAvatarUrl = coachAvatarUrl;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
