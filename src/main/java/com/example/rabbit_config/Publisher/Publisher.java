package com.example.rabbit_config.Publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class Publisher {

    private String routingKey = "The_Routing_Key";

    private RabbitTemplate rabbitTemplate;

    Publisher(RabbitTemplate template) {
        rabbitTemplate = template;
    }

    public void publisher1(String message) {
        rabbitTemplate.convertAndSend("Listener_Exchange", routingKey, message);
    }

}
