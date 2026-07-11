package com.example.rabbit_config.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Config.RabbitConfig;
import com.example.rabbit_config.Publisher.GeminiPublisher;
import com.example.rabbit_config.Publisher.WebhookPublisher;
import com.example.rabbit_config.Service.GmailService;

@Component
public class WebhookConsumer {

    private Logger log = LoggerFactory.getLogger(WebhookConsumer.class);

    private WebhookPublisher webhookPublisher;
    private GeminiPublisher geminiPublisher;
    private GmailService gmailService;

    WebhookConsumer(WebhookPublisher webhookPublisher, GeminiPublisher geminiPublisher, GmailService gmailService) {
        this.webhookPublisher = webhookPublisher;
        this.geminiPublisher = geminiPublisher;
        this.gmailService = gmailService;
    }

    @RabbitListener(queues = RabbitConfig.WEBHOOK_QUEUE)
    public void consumer(Message message) {

        try {
            log.info("New Notification Received");
            // download the new incoming email using the messageID/HistoryId
            // using Gmail service

            // publish to gemini queue for reply generation and done

        } catch (Exception e) {
            log.error("Error downloading Email");
            webhookPublisher.publishRetryEmail(message);
            log.info("Published for Retry");
        }

    }

}
