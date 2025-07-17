// src/main/java/com/demo/demo/dto/RoleNotificationRequest.java
package com.demo.demo.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleNotificationRequest {
    @NotBlank
    private String role;     // ví dụ "USER", "COACH", "ADMIN"
    @NotBlank
    private String type;
    @NotBlank
    private String message;
}
