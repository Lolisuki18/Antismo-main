package com.demo.demo.service;


import com.demo.demo.dto.EmailDetail;
import com.demo.demo.dto.MailBody;
import com.demo.demo.entity.User.User;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;


@Service
public class EmailService {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;


    public void sendMail(User user, EmailDetail emailDetail) {
        try {
            Context context = new Context();

            // 👈 Lấy fullName từ Account
            context.setVariable("name", user.getFullName());

            String html = templateEngine.process("emailtemplate", context);

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);

            helper.setFrom("admin@gmail.com");
            helper.setTo(emailDetail.getRecipient());
            helper.setSubject(emailDetail.getSubject());
            helper.setText(html, true);

            javaMailSender.send(mimeMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendSimpleMail(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailBody.to());
        message.setFrom("thormastran@gmail.com");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.message());

        javaMailSender.send(message);
    }
}