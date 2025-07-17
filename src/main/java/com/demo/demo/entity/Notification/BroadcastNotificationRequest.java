package com.demo.demo.entity.Notification;

import lombok.*;

import jakarta.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BroadcastNotificationRequest {
    @NotBlank
    private String type;

    @NotBlank
    private String message;
}