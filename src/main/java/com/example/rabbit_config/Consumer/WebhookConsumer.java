package com.example.rabbit_config.Consumer;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Config.RabbitConfig;
import com.example.rabbit_config.Model.DecodedGmailPayload;

import com.example.rabbit_config.Model.PubSubEnvelope;
import com.example.rabbit_config.Model.ReceivedEmail;
import com.example.rabbit_config.Publisher.GeminiPublisher;
import com.example.rabbit_config.Publisher.WebhookPublisher;
import com.example.rabbit_config.Service.EmailTabelService;
import com.example.rabbit_config.Service.GmailService;
import com.example.rabbit_config.Service.UserTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class WebhookConsumer {

    private Logger log = LoggerFactory.getLogger(WebhookConsumer.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    private WebhookPublisher webhookPublisher;
    private GeminiPublisher geminiPublisher;
    private GmailService gmailService;
    private UserTokenService uService;
    private EmailTabelService eTabelService;

    WebhookConsumer(WebhookPublisher webhookPublisher, GeminiPublisher geminiPublisher, GmailService gmailService,
            UserTokenService uService, EmailTabelService service) {
        this.webhookPublisher = webhookPublisher;
        this.geminiPublisher = geminiPublisher;
        this.gmailService = gmailService;
        this.uService = uService;
        this.eTabelService = service;
    }

    @RabbitListener(queues = RabbitConfig.WEBHOOK_QUEUE)
    public void consumer(Message message) {

        try {
            log.info("New Notification Received");
            String body = new String(
                    message.getBody(),
                    StandardCharsets.UTF_8);
            /// Step 1: Parse Pub/Sub envelope
            PubSubEnvelope envelope = objectMapper.readValue(body, PubSubEnvelope.class);

            // Step 2: Extract Base64 data
            String encodedData = envelope.getMessage().getData();

            // Message Id as Datbase identity check
            String messageId = envelope.getMessage().getMessageId();

            if (!eTabelService.isMsgIdPresent(messageId)) {

                // Step 3: Decode Base64
                String decodedJson = new String(
                        Base64.getDecoder().decode(encodedData),
                        StandardCharsets.UTF_8);

                // Step 4: Parse decoded JSON
                DecodedGmailPayload notification = objectMapper.readValue(decodedJson, DecodedGmailPayload.class);

                // Step 5: Extract values
                String emailAddress = notification.getEmailAddress();
                String historyId = notification.getHistoryId();

                System.out.println("Email      : " + emailAddress);
                System.out.println("History Id : " + historyId);

                String previousHistoryId = uService.getLastHistoryId(emailAddress);

                if (previousHistoryId == null) {

                    uService.save(emailAddress, historyId);
                    return;
                }

                List<ReceivedEmail> emails = gmailService.getNewEmails(emailAddress, previousHistoryId);

                for (ReceivedEmail email : emails) {

                    log.info("--------------------------------");
                    log.info("Message Id  : {}", email.getMessageId());
                    log.info("From    : {}", email.getFrom());
                    log.info("To      : {}", email.getTo());
                    log.info("Subject : {}", email.getSubject());
                    log.info("Date    : {}", email.getDate());
                    log.info("Body    : {}", email.getBody());

                    geminiPublisher.publishEmail(email);
                }

                uService.save(emailAddress, historyId);
            } else {
                log.info("\nMessage Already Processed\n");
            }
        } catch (Exception e) {
            log.error("Error downloading Email");
            webhookPublisher.publishRetryEmail(message);
            log.info("Published for Retry");
        }

    }

}
