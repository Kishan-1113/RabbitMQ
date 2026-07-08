package com.example.rabbit_config.Consumer;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Model.EmailModel;

@Component
public class EmailConsumer {

    @RabbitListener(queues = "Email_queue")
    public void consumer1(EmailModel message) {

        System.out.println("\nEmail Listed : " + message);

    }

}
