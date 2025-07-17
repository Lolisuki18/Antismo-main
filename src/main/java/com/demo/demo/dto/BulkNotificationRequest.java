// src/main/java/com/demo/demo/dto/BulkNotificationRequest.java
package com.demo.demo.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkNotificationRequest {
    @NotEmpty
    private List<Integer> userIds;
    @NotBlank
    private String type;
    @NotBlank
    private String message;
}
