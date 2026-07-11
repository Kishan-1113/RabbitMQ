package com.example.rabbit_config.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbit_config.Publisher.WebhookPublisher;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    private WebhookPublisher webhookPublisher;

    WebhookController(WebhookPublisher webhookPublisher) {
        this.webhookPublisher = webhookPublisher;
    }

    @PostMapping("/incoming-email")
    public String postMethodName(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

}
