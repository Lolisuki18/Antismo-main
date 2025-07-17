package com.demo.demo.dto;

import com.demo.demo.enums.Role;

import java.time.LocalDateTime;

public record FeedbackDTO(
        Integer id,
        String title,
        String comment,
        LocalDateTime createdAt,
        Integer userId,
        String fullName,
        Role role,
        Integer status,
        String tags
) {
}
