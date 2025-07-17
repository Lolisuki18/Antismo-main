package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressLogRequest {
    private Long userId;
    private Instant time;
    private Integer smoked;
    private Integer logType;
    private Integer cravingIntensity;
    private Integer emotion;
    private String context;
}