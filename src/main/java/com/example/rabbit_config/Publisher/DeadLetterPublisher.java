package com.example.rabbit_config.Publisher;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Config.RabbitConfig;

@Component
public class DeadLetterPublisher {

    private final RabbitTemplate rabbitTemplate;

    public DeadLetterPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publishDeadLetter(Message message) {

        rabbitTemplate.send(
                RabbitConfig.DEAD_LETTER_EXCNG,
                RabbitConfig.DEAD_LETTER_KEY,
                message);
    }
}
