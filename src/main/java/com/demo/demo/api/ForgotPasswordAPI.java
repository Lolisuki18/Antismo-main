package com.demo.demo.api;

import com.demo.demo.dto.ChangePassword;
import com.demo.demo.dto.MailBody;
import com.demo.demo.entity.Auth.ForgotPassword;
import com.demo.demo.entity.User.User;
import com.demo.demo.repository.Auth.ForgotPasswordRepository;
import com.demo.demo.repository.User.UserRepository;
import com.demo.demo.service.EmailService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPasswordAPI {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final ForgotPasswordRepository forgotPasswordRepository;
    private final PasswordEncoder passwordEncoder;

    public ForgotPasswordAPI(UserRepository userRepository,
                             EmailService emailService,
                             ForgotPasswordRepository forgotPasswordRepository,
                             PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.forgotPasswordRepository = forgotPasswordRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // 1. Gửi OTP kèm lưu vào cả hai chiều
    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> sendVerificationEmail(@PathVariable String email) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        int otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .subject("Email Verification")
                .message("Please verify your email with this otp: " + otp)
                .build();

        // 1. Tạo entity ForgotPassword
        ForgotPassword fp = ForgotPassword.builder()
                .otp(otp)
                .expiredAt(new Date(System.currentTimeMillis() + 5 * 60 * 1000))
                .user(user)                // set chiều ForgotPassword → User
                .build();

        // 2. Set chiều ngược lại để Hibernate biết cả hai entity liên kết
        user.setForgotPassword(fp);

        // 3. Lưu user (cascade = ALL sẽ persist cả fp)
        userRepository.save(user);

        // 4. Gửi mail
        emailService.sendSimpleMail(mailBody);

        return ResponseEntity.ok("Verification 6-digit code sent to " + email);
    }

    // 2. Verify OTP: chỉ “bẻ” liên kết rồi save user, Hibernate tự remove bản ghi orphan
    @PostMapping("/verify-otp/{email}/{otp}")
    public ResponseEntity<String> verifyOtp(@PathVariable String email,
                                            @PathVariable Integer otp) {
        User user = userRepository.findUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        ForgotPassword fp = forgotPasswordRepository
                .findByOtpAndUser(otp, user)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        // OTP expired?
        if (fp.getExpiredAt().before(Date.from(Instant.now()))) {
            // remove association → orphanRemoval sẽ xóa fp
            user.setForgotPassword(null);
            userRepository.save(user);
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("OTP has expired. Please request a new OTP.");
        }

        // valid OTP → xoá tương tự
        user.setForgotPassword(null);
        userRepository.save(user);

        return ResponseEntity.ok("OTP verified successfully");
    }

    // 3. Reset password (không liên quan đến ForgotPassword)
    @PostMapping("/reset-password/{email}")
    public ResponseEntity<String> resetPassword(@RequestBody ChangePassword changePassword,
                                                @PathVariable String email) {
        if (!changePassword.password().equals(changePassword.confirmPassword())) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("New password and confirm password do not match");
        }
        String encoded = passwordEncoder.encode(changePassword.password());
        userRepository.updateUserPasswordByEmail(email, encoded);
        return ResponseEntity.ok("Password reset successfully");
    }

    private Integer otpGenerator() {
        SecureRandom rnd = new SecureRandom();
        return 100000 + rnd.nextInt(900000);
    }
    
}
