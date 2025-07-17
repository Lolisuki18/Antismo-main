package com.demo.demo.repository.Auth;

import com.demo.demo.entity.Auth.ForgotPassword;
import com.demo.demo.entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ForgotPasswordRepository extends JpaRepository<ForgotPassword, Integer> {
    Optional<ForgotPassword> findByOtpAndUser(Integer otp, User user);
}
