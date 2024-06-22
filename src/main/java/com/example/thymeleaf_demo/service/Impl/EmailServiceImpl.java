package com.example.thymeleaf_demo.service.Impl;

import com.example.thymeleaf_demo.domain.User;
import com.example.thymeleaf_demo.dto.UserDto;
import com.example.thymeleaf_demo.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
public class EmailServiceImpl implements EmailService {

    @Value("${spring.mail.from}")
    private String from;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;


    @Override
    public void sendVerificationEmail(UserDto user, String token) throws MessagingException {

        String subject = "Email verification";
        String confirmationLink = "http://localhost:8080/verify?token=" + token;
        String recipientAddress = user.getEmail();

        Map<String,Object> model = new HashMap<>();
        model.put("username",user.getUsername());
        model.put("verificationUrl",confirmationLink);

        /*MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "utf-8");
            String htmlMsg = "<h3>Hesabınızı doğrulamak icin lutfen asagıdaki baglantiya tiklayin:</h3><br><a href='" + verificationLink + "'>Doğrulama Baglantisi</a>";
            mimeMessage.setContent(htmlMsg, "text/html");
            helper.setTo(toEmail);
            helper.setSubject("Hesap Doğrulama");
            mailSender.send(mimeMessage);*/
        sendHtmlEmail(recipientAddress,subject,"verify-email.html",model);
    }

    @Override
    public void sendPasswordResetEmail(UserDto user, String resetLink) {
        String subject = "Password reset request";
        String senderName = "User management app";
        String mailContent = "<p> Hello "+user.getUsername()+ ",</p>";

        mailContent += "<p>You have requested to reset your password.</p>";
        mailContent += "<p>Please click the link below to reset your password:</p>";
        mailContent += "<p><a href='" + resetLink + "'>Reset Password</a></p>";
        mailContent += "<p>If you did not request this, please ignore this email.</p>";

        mailContent += "<p>Thank you, <br>User management app </p>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom("admin@example.com",senderName);
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(mailContent,true);
        } catch (MessagingException | UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }

    private void sendHtmlEmail(String to, String subject, String templateName, Map<String, Object> model) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            Context context = new Context();
            context.setVariables(model);
            String htmlContent = templateEngine.process(templateName, context);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email", e);
        }
    }
}


























