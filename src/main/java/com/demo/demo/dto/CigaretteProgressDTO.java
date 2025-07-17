package com.demo.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CigaretteProgressDTO {
    private String date;
    private int cigarettesSmoked;
    private int cigarettesAllowed;

    public CigaretteProgressDTO(String date, int smoked, int allowed) {
        this.date = date;
        this.cigarettesSmoked = smoked;
        this.cigarettesAllowed = allowed;
    }
}
