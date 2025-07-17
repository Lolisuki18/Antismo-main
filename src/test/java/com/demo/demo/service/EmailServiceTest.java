package com.demo.demo.service;

import com.demo.demo.dto.EmailDetail;
import com.demo.demo.dto.MailBody;
import com.demo.demo.entity.User.User;
import com.demo.demo.enums.Role;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private TemplateEngine templateEngine;

    @Mock
    private JavaMailSender javaMailSender;

    @Mock
    private MimeMessage mimeMessage;

    @Mock
    private MimeMessageHelper mimeMessageHelper;

    @InjectMocks
    private EmailService emailService;

    @Test
    void testSendMail_Success() throws Exception {
        // Arrange
        User user = createTestUser(1, "John Doe", "john@example.com");
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("john@example.com");
        emailDetail.setSubject("Test Subject");

        when(templateEngine.process(eq("emailtemplate"), any(Context.class))).thenReturn("<html>Test HTML</html>");
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendMail(user, emailDetail);

        // Assert
        verify(templateEngine, times(1)).process(eq("emailtemplate"), any(Context.class));
        verify(javaMailSender, times(1)).createMimeMessage();
        verify(javaMailSender, times(1)).send(mimeMessage);
    }

    @Test
    void testSendMail_WithDifferentUsers() throws Exception {
        // Arrange
        User user1 = createTestUser(1, "Alice Smith", "alice@example.com");
        User user2 = createTestUser(2, "Bob Johnson", "bob@example.com");

        EmailDetail emailDetail1 = new EmailDetail();
        emailDetail1.setRecipient("alice@example.com");
        emailDetail1.setSubject("Welcome Alice");

        EmailDetail emailDetail2 = new EmailDetail();
        emailDetail2.setRecipient("bob@example.com");
        emailDetail2.setSubject("Welcome Bob");

        when(templateEngine.process(eq("emailtemplate"), any(Context.class))).thenReturn("<html>Test HTML</html>");
        when(javaMailSender.createMimeMessage()).thenReturn(mimeMessage);

        // Act
        emailService.sendMail(user1, emailDetail1);
        emailService.sendMail(user2, emailDetail2);

        // Assert
        verify(templateEngine, times(2)).process(eq("emailtemplate"), any(Context.class));
        verify(javaMailSender, times(2)).createMimeMessage();
        verify(javaMailSender, times(2)).send(mimeMessage);
    }

    @Test
    void testSendMail_WithNullUser() throws Exception {
        // Arrange
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("test@example.com");
        emailDetail.setSubject("Test Subject");

        // Act
        emailService.sendMail(null, emailDetail);

        // Assert - Should not call template engine or send email due to
        // NullPointerException
        verify(templateEngine, never()).process(any(String.class), any(Context.class));
        verify(javaMailSender, never()).createMimeMessage();
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void testSendMail_TemplateEngineException() throws Exception {
        // Arrange
        User user = createTestUser(1, "John Doe", "john@example.com");
        EmailDetail emailDetail = new EmailDetail();
        emailDetail.setRecipient("john@example.com");
        emailDetail.setSubject("Test Subject");

        when(templateEngine.process(eq("emailtemplate"), any(Context.class)))
                .thenThrow(new RuntimeException("Template processing failed"));

        // Act
        emailService.sendMail(user, emailDetail);

        // Assert - Should handle exception gracefully
        verify(templateEngine, times(1)).process(eq("emailtemplate"), any(Context.class));
        verify(javaMailSender, never()).createMimeMessage();
        verify(javaMailSender, never()).send(any(MimeMessage.class));
    }

    @Test
    void testSendSimpleMail_Success() {
        // Arrange
        MailBody mailBody = MailBody.builder()
                .to("recipient@example.com")
                .subject("Test Subject")
                .message("Test message content")
                .build();

        // Act
        emailService.sendSimpleMail(mailBody);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendSimpleMail_MultipleEmails() {
        // Arrange
        MailBody mailBody1 = MailBody.builder()
                .to("user1@example.com")
                .subject("Subject 1")
                .message("Message 1")
                .build();
        MailBody mailBody2 = MailBody.builder()
                .to("user2@example.com")
                .subject("Subject 2")
                .message("Message 2")
                .build();
        MailBody mailBody3 = MailBody.builder()
                .to("user3@example.com")
                .subject("Subject 3")
                .message("Message 3")
                .build();

        // Act
        emailService.sendSimpleMail(mailBody1);
        emailService.sendSimpleMail(mailBody2);
        emailService.sendSimpleMail(mailBody3);

        // Assert
        verify(javaMailSender, times(3)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendSimpleMail_WithEmptyContent() {
        // Arrange
        MailBody mailBody = MailBody.builder()
                .to("test@example.com")
                .subject("")
                .message("")
                .build();

        // Act
        emailService.sendSimpleMail(mailBody);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendSimpleMail_WithLongContent() {
        // Arrange
        String longMessage = "This is a very long message that contains multiple sentences. ".repeat(100);
        MailBody mailBody = MailBody.builder()
                .to("test@example.com")
                .subject("Long Content Test")
                .message(longMessage)
                .build();

        // Act
        emailService.sendSimpleMail(mailBody);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendSimpleMail_WithSpecialCharacters() {
        // Arrange
        MailBody mailBody = MailBody.builder()
                .to("test@example.com")
                .subject("Tiêu đề có dấu tiếng Việt")
                .message("Nội dung có ký tự đặc biệt: áàảãạăắằẳẵặâấầẩẫậéèẻẽẹêếềểễệ")
                .build();

        // Act
        emailService.sendSimpleMail(mailBody);

        // Assert
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendSimpleMail_NotificationTypes() {
        // Arrange
        MailBody reminderMail = MailBody.builder()
                .to("user@example.com")
                .subject("REMINDER")
                .message("Don't forget your appointment")
                .build();
        MailBody welcomeMail = MailBody.builder()
                .to("user@example.com")
                .subject("WELCOME")
                .message("Welcome to our service")
                .build();
        MailBody alertMail = MailBody.builder()
                .to("user@example.com")
                .subject("ALERT")
                .message("Important alert message")
                .build();

        // Act
        emailService.sendSimpleMail(reminderMail);
        emailService.sendSimpleMail(welcomeMail);
        emailService.sendSimpleMail(alertMail);

        // Assert
        verify(javaMailSender, times(3)).send(any(SimpleMailMessage.class));
    }

    // Helper method
    private User createTestUser(Integer id, String fullName, String email) {
        User user = new User();
        user.setId(id);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setRole(Role.USER);
        user.setIsActive(true);
        return user;
    }
}
