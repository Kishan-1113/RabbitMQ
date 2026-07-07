package com.example.rabbit_config.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class Consumer {

    private static final Logger log = LoggerFactory.getLogger((Consumer.class));

    @RabbitListener(queues = "Listener_queue")
    public void consumer1(String message) {

        System.out.println("\nListened : " + message);

    }
}
