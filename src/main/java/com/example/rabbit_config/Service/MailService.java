package com.example.rabbit_config.Service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private JavaMailSender mailSender;

    MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSimpleEmail(String to, String subject, String body) {

        // SimpleMailMessage message = new SimpleMailMessage();
        // message.setFrom("bhadrakishan717@gmail.com");
        // message.setTo(to);
        // message.setSubject(subject);
        // message.setText(body);

        // mailSender.send(message);
        System.out.println("To : " + to);
        System.out.println("Subject : " + subject);
        System.out.println("Content : " + body);
    }
}