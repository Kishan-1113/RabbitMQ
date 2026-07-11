package com.example.rabbit_config.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.rabbit_config.Model.EmailModel;

@Service
public class MailSendService {

    private JavaMailSender mailSender;

    MailSendService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Value("${spring.mail.username}")
    private String sender;

    public void sendSimpleEmail(EmailModel email) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(sender);
        message.setTo(email.getAddress());
        message.setSubject(email.getSubject());
        message.setText(email.getContent());

        System.out.println("====================================\n");
        System.out.println("Sender : " + sender);
        System.out.println("Receiver : " + email.getAddress());
        System.out.println("\n====================================");

        mailSender.send(message);
    }

}