package com.example.rabbit_config.Publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Model.EmailModel;

@Component
public class EmailPublisher {

    private String routingKey = "email_routing";

    private final RabbitTemplate rabbitTemplate;

    public EmailPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publisher1(EmailModel email) {
        rabbitTemplate.convertAndSend(
                "Email_exchange",
                routingKey,
                email);
    }
}
