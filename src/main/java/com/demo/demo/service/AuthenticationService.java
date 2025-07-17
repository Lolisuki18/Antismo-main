package com.demo.demo.service;

import com.demo.demo.dto.EmailDetail;
import com.demo.demo.dto.RegisterRequest;
import com.demo.demo.entity.User.User;
import com.demo.demo.repository.User.UserRepository;

import com.demo.demo.util.JwtUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    EmailService emailService;

    @Autowired
    ModelMapper modelMapper;

    // Load user by username (Spring Security)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return user;
    }

    //Sign in
    public String signIn(String email, String password) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        return jwtUtil.generateToken(email);
    }

    //Register
    public User register(RegisterRequest request) {
        User user = modelMapper.map(request, User.class);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient(user.getEmail());
        emailDetail.setSubject("Welcome to my system antismoke");
        emailService.sendMail(user, emailDetail);

        return userRepository.save(user);
    }

//    public Account register(RegisterRequest request) {
//        Account user = new Account();
//        user.setEmail(request.getEmail());
//        user.setPassword(passwordEncoder.encode(request.getPassword()));
//        user.setFullName(request.getFullName());
//        user.setGender(request.getGender());
//        user.setDateOfBirth(request.getDateOfBirth());
//
//        user.setUsername(generateUsernameFromEmail(request.getEmail())); // thêm hàm ở dưới
//        user.setPhone(null);
//        user.setRole(Role.CUSTOMER);
//        user.setCreatedAt(LocalDateTime.now());
//
//        EmailDetail emailDetail = new EmailDetail();
//        emailDetail.setRecipient(user.getEmail());
//        emailDetail.setSubject("Welcome to my system antismoke");
//
//        emailService.sendMail(user, emailDetail);
//
//        return authenticationRepository.save(user);
//    }


//    private String generateUsernameFromEmail(String email) {
//        String base = email.split("@")[0];
//        return base;
//    }


}
