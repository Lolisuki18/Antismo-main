package com.demo.demo.dto;

import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Getter
@Setter
public class RegisterRequest {
    private String email;
    private String password;
    private String fullName;
    private Gender gender;
    private LocalDate dateOfBirth;
}
