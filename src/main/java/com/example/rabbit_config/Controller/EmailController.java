package com.example.rabbit_config.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbit_config.Model.EmailModel;
import com.example.rabbit_config.Publisher.EmailPublisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/email")
public class EmailController {

    private EmailPublisher emailPublisher;

    EmailController(EmailPublisher publisher) {
        emailPublisher = publisher;
    }

    private static final Logger log = LoggerFactory.getLogger(EmailController.class);

    @PostMapping("/send")
    public ResponseEntity<?> sendEmail(@RequestBody EmailModel email) {
        try {
            emailPublisher.publisher1(email);

        } catch (Exception e) {
            log.error("An error occured while publishing message to exchange");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occured while publishing message to exchange");
        }

        return ResponseEntity.status(HttpStatus.OK).body("Message published for processing");

    }

    @GetMapping("/get")
    public String getMethodName() {
        return new String("Hey shaky shaky");
    }

}
