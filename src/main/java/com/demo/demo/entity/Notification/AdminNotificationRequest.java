package com.demo.demo.entity.Notification;


import lombok.*;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminNotificationRequest {
    @NotNull
    private Integer userId;

    @NotBlank
    private String type;

    @NotBlank
    private String message;
}