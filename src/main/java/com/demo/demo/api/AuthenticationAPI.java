package com.demo.demo.api;

import com.demo.demo.dto.RegisterRequest;
import com.demo.demo.dto.RegisterResponse;
import com.demo.demo.dto.SigninRequest;
import com.demo.demo.entity.User.User;
import com.demo.demo.service.AuthenticationService;
import com.demo.demo.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthenticationAPI {

    @Autowired
    AuthenticationService authenticationService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/api/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        User newUser = authenticationService.register(request);
        String token = jwtUtil.generateToken(newUser.getEmail());

        // Build the response DTO
        Map<String, Object> userDto = new HashMap<>();
        userDto.put("id", newUser.getId());
        userDto.put("email", newUser.getEmail());
        userDto.put("fullName", newUser.getFullName());
        userDto.put("dateOfBirth", newUser.getDateOfBirth());
        userDto.put("avatarUrl", newUser.getAvatarUrl());
        userDto.put("role", newUser.getRole());

        RegisterResponse response = new RegisterResponse(userDto, token);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/api/signin")
    public ResponseEntity<?> signIn(@RequestBody SigninRequest request) {
        String email = request.getEmail();
        String password = request.getPassword();
        String jwt = authenticationService.signIn(email, password);

        Map<String, Object> token = new HashMap<>();
        token.put("token", jwt);
        return ResponseEntity.ok(token);
    }

}
