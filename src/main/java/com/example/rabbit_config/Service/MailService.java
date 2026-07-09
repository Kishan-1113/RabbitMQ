package com.example.rabbit_config.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.rabbit_config.Model.EmailModel;

@Service
public class MailService {

    private JavaMailSender mailSender;

    MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private static String sender;
    @Value("${spring.mail.password}")
    private static String password;

    public void sendSimpleEmail(String to, String subject, String body) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);

        System.out.println("Sender" + sender);
        System.out.println("password" + password);
        System.out.println("pass len" + password.length());

        mailSender.send(message);

        System.out.println("To : " + to);
        System.out.println("Subject : " + subject);
        System.out.println("Content : " + body);
    }

    public void sendSimpleEmail(EmailModel email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(email.getAddress());
        message.setSubject(email.getSubject());
        message.setText(email.getContent());

        System.out.println("\n\n====================================\n\n");
        System.out.println("Sender" + sender);
        System.out.println("password" + password);
        System.out.println("pass len" + password.length());
        System.out.println("\n\n====================================\n\n");

        mailSender.send(message);

        System.out.println("To : " + email.getAddress());
        System.out.println("Subject : " + email.getSubject());
        System.out.println("Content : " + email.getContent());
    }
}