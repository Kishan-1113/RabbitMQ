package com.example.rabbit_config.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Model.EmailModel;
import com.example.rabbit_config.Service.MailService;

@Component
public class EmailConsumer {

    private MailService mailService;

    EmailConsumer(MailService service) {
        mailService = service;
    }

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @RabbitListener(queues = "Email_queue")
    public void consumer1(EmailModel message) {

        try {
            log.info("\nEmail Listed : " + message.toString());
            mailService.sendSimpleEmail(message);
            log.info("Email Sent successful");
        } catch (Exception e) {
            log.error("Error occured while sending mail");
            e.printStackTrace();
        }
    }
}