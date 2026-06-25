package com.example.rabbit_config.Consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    @RabbitListener(queues = "Listener_queue")
    public void consumer1(String message) {

        System.out.println("\nListened : " + message);

    }
}
