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
import com.example.rabbit_config.Publisher.GeminiPublisher;
import com.example.rabbit_config.Service.GeminiService;

@Component
public class GeminiConsumer {

    /// This consumer listens to gemini reply producer queue and generates reply
    /// from gemini
    /// After that with required credentials ---> creates a EmailModel object and
    /// pushes to Email exchange with the routing key of Email queue
    /// There is a consumer that is listening to this Email queue and does the work
    /// of sending emails
    private final GeminiService gService;
    private final EmailPublisher emailPublisher;
    private final GeminiPublisher geminiPublisher;
    private DeadLetterPublisher deadLetterPublisher;

    GeminiConsumer(GeminiService service, EmailPublisher emailPublisher, GeminiPublisher geminiPublisher,
            DeadLetterPublisher deadLetterPublisher) {
        this.gService = service;
        this.emailPublisher = emailPublisher;
        this.geminiPublisher = geminiPublisher;
        this.deadLetterPublisher = deadLetterPublisher;
    }

    private static final Logger log = LoggerFactory.getLogger(GeminiConsumer.class);

    @RabbitListener(queues = RabbitConfig.GEMINI_QUEUE)
    public void consumer(EmailModel email, Message message) {

        MessageProperties props = message.getMessageProperties();

        int retryCount = (Integer) props.getHeaders().getOrDefault(MessageHeaders.RETRY_COUNT, 0);

        String prom = "Hello gemini, create a professional email reply for this email body : \n" + "Subject : "
                + email.getSubject() + "\nBody" + email.getContent()
                + "\nDo not keep any extra text, just the reply in proper professional tone, remove any extra overhead lines so that I can directly copy paste the reply";

        try {
            log.info("\nGemini Consumer : Email Listed for reply generation... ");

            String geminiResponse = gService.getGeminiResponse(prom);

            log.info("Gemini Consumer : Reply Generated");

            EmailModel newEmail = new EmailModel(email.getAddress(), email.getSubject(), geminiResponse);

            try {
                emailPublisher.publisher1(newEmail);

                log.info("Gemini Consumer : Email successfully published for sending");

            } catch (Exception e) {
                log.error("Gemini Consumer : Error publishing email to sender queue");
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error("Gemini Consumer : Error occured while generating reply");

            retryCount++;
            props.setHeader(MessageHeaders.RETRY_COUNT, retryCount);

            if (retryCount < 5) {
                geminiPublisher.publishRetryEmail(message);
                log.info("Gemini Consumer : Published to gemini retry queue");

            } else {
                props.setHeader(MessageHeaders.LAST_ERROR, e.getMessage());

                deadLetterPublisher.publishDeadLetter(message);

                log.info("Gemini Consumer : Published to dead letter queue");
            }
        }
    }

}