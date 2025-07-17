package com.demo.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class UserUpdateDTO {
        private String fullName;
        private String phone;
        private String avatarUrl;
    }

