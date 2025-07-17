package com.demo.demo.dto;

import lombok.Data;
import lombok.Getter;
import java.time.OffsetDateTime;

@Getter
public class GoogleEventDTO {
    private String id;
    private String summary;
    private int userId;
    private String creator;
    private OffsetDateTime start;
    private OffsetDateTime end;
}
