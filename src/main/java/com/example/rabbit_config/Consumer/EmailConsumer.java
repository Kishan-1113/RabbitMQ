package com.example.rabbit_config.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Config.MessageHeaders;
import com.example.rabbit_config.Config.RabbitConfig;
import com.example.rabbit_config.Model.EmailModel;
import com.example.rabbit_config.Publisher.DeadLetterPublisher;
import com.example.rabbit_config.Publisher.EmailPublisher;
import com.example.rabbit_config.Service.MailService;

@Component
public class EmailConsumer {

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    private MailService mailService;
    private EmailPublisher emailPublisher;
    private DeadLetterPublisher deadLetterPublisher;

    EmailConsumer(MailService service, EmailPublisher emailPublisher, DeadLetterPublisher deadLetterPublisher) {
        this.mailService = service;
        this.emailPublisher = emailPublisher;
        this.deadLetterPublisher = deadLetterPublisher;
    }

    @RabbitListener(queues = RabbitConfig.EMAIL_QUEUE)
    public void consumer1(EmailModel email, Message message) {

        MessageProperties props = message.getMessageProperties();

        int retryCount = (Integer) props.getHeaders().getOrDefault(MessageHeaders.RETRY_COUNT, 0);

        try {
            log.info("\nEmail Consumer : Email sending to {} ", email.getAddress());

            mailService.sendSimpleEmail(email);

            log.info("Email Consumer : Email Sent successfully");

        } catch (Exception e) {
            log.error("Email Consumer : Error occured while sending mail");

            retryCount++;
            props.setHeader(MessageHeaders.RETRY_COUNT, retryCount);

            if (retryCount < 5) {

                emailPublisher.publishRetry(message);

                log.info("Email Consumer : Published to retry queue");

            } else {
                props.setHeader(MessageHeaders.LAST_ERROR, e.getMessage());
                deadLetterPublisher.publishDeadLetter(message);

                log.info("Email Consumer : Published to dead letter queue");
            }
        }
    }
}
