package com.demo.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class UserBadgeDTO {



        private Integer userId;
        private Integer badgeId;
        private LocalDateTime earnedAt;
        private Boolean shared;

}
