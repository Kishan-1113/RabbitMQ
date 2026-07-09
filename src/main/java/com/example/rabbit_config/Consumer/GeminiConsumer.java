package com.example.rabbit_config.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Model.EmailModel;
import com.example.rabbit_config.Publisher.EmailPublisher;
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

    GeminiConsumer(GeminiService service, EmailPublisher emailPublisher) {
        this.gService = service;
        this.emailPublisher = emailPublisher;
    }

    private static final Logger log = LoggerFactory.getLogger(EmailConsumer.class);

    @RabbitListener(queues = "Gemini_queue")
    public void consumer(EmailModel message) {

        String prom = "Hello gemini, create a professional email reply for this email body : \n" + "Subject : "
                + message.getSubject() + "\nBody" + message.getContent()
                + "\nDo not keep any extra text, just the reply in proper professional tone so that I can directly copy paste the reply";

        try {
            log.info("\nEmail Listed for reply generation : " + message.toString());
            String geminiResponse = gService.getGeminiResponse(prom);
            log.info("Reply Generation successful");

            EmailModel newEmail = new EmailModel(message.getAddress(), message.getSubject(), geminiResponse);

            try {
                emailPublisher.publisher1(newEmail);
                log.info("Email successfully published for sending");
            } catch (Exception e) {
                log.error("Error publishing email to sender queue");
                e.printStackTrace();
            }

        } catch (Exception e) {
            log.error("Error occured while generating reply");
            e.printStackTrace();
        }
    }

}