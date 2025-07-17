package com.demo.demo.service;

import com.demo.demo.entity.User.User;
import com.demo.demo.enums.Gender;
import com.demo.demo.enums.Role;
import com.demo.demo.repository.User.UserRepository;
import com.demo.demo.util.JwtUtil;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import java.time.LocalDateTime;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

//     @Autowired
// private RestTemplate restTemplate;

// @Autowired
// private OAuth2AuthorizedClientService authorizedClientService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String fullName = oauthUser.getAttribute("name");
        String avatarUrl = oauthUser.getAttribute("picture");

        // Tìm người dùng theo email
        User user = userRepository.findUserByEmail(email)
                .orElseGet(() -> {
                    // Nếu không tìm thấy, tạo người dùng mới
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setFullName(fullName);
                    newUser.setAvatarUrl(avatarUrl);
                    newUser.setRole(Role.USER); // Set default role as USER
                    newUser.setCreatedAt(LocalDateTime.now());
                    newUser.setDateOfBirth(null); // Set date of birth if available
                    newUser.setGender(Gender.MALE);
                    return userRepository.save(newUser);
                });

        String jwt = jwtUtil.generateToken(email);


        // // 👇 Thêm userId vào URL
        // String redirectUrl = String.format(
        //         "http://localhost:5173/form?userId=%d&fullName=%s&email=%s",
        //         user.getId(),
        //         URLEncoder.encode(fullName, StandardCharsets.UTF_8),
        //         URLEncoder.encode(email, StandardCharsets.UTF_8)
        // );

        String redirectUrl = String.format(
                "http://localhost:5173/oauth-callback?t=%s",
                jwt
        );

        response.sendRedirect(redirectUrl);
    }
}


