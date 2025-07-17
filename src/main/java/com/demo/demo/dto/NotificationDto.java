package com.demo.demo.dto;

import com.demo.demo.entity.Notification.Notification;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationDto {

    private Integer id;
    private String type;
    private String message;
    private LocalDateTime sentAt;
    private Boolean read;

    public static NotificationDto fromEntity(Notification n) {
        return NotificationDto.builder()
                .id(n.getId())
                .type(n.getType())
                .message(n.getMessage())
                .sentAt(n.getSentAt())
                .read(n.getRead())
                .build();
    }
}
