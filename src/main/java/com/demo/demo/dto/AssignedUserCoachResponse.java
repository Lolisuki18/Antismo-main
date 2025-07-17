package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignedUserCoachResponse {
    private int id;
    private String fullName;
    private String email;
    private Long cigarettesPerDay;

    public AssignedUserCoachResponse(int id, String fullName, String email, Long cigarettesPerDay) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.cigarettesPerDay = cigarettesPerDay;
    }
}
