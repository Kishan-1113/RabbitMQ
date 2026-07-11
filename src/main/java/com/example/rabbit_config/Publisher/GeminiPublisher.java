package com.example.rabbit_config.Publisher;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Config.MessageHeaders;
import com.example.rabbit_config.Config.RabbitConfig;
import com.example.rabbit_config.Model.EmailModel;

@Component
public class GeminiPublisher {

    private final RabbitTemplate rabbitTemplate;

    public GeminiPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;

    }

    public void publishEmail(EmailModel email) {
        rabbitTemplate.convertAndSend(
                RabbitConfig.GEMINI_EXCHANGE,
                RabbitConfig.GEMINI_KEY,
                email, message -> {
                    MessageProperties props = message.getMessageProperties();
                    props.setHeader(MessageHeaders.RETRY_COUNT, 0);

                    return message;
                });
    }

    public void publishRetryEmail(Message message) {
        rabbitTemplate.send(
                RabbitConfig.GEMINI_EXCHANGE,
                RabbitConfig.GEMINI_RETRY_KEY,
                message);
    }

}
