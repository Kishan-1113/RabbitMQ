package com.example.rabbit_config.Publisher;

import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Config.MessageHeaders;
import com.example.rabbit_config.Config.RabbitConfig;
import com.example.rabbit_config.Model.EmailModel;

@Component
public class EmailPublisher {

    private final RabbitTemplate rabbitTemplate;

    public EmailPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publisher1(EmailModel email) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.EMAIL_EXCHANGE,
                RabbitConfig.EMAIL_ROUTING_KEY,
                email,
                message -> {
                    MessageProperties props = message.getMessageProperties();
                    props.setHeader(MessageHeaders.RETRY_COUNT, 0);

                    return message;
                });
    }

    public void publishRetry(Message message) {

        rabbitTemplate.send(
                RabbitConfig.EMAIL_EXCHANGE,
                RabbitConfig.EMAIL_RETRY_ROUTING_KEY,
                message);
    }
}
