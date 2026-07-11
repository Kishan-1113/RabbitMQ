package com.example.rabbit_config.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rabbit_config.OAuth.OAuthSuccessHandler;
import com.example.rabbit_config.Publisher.WebhookPublisher;
import com.example.rabbit_config.Service.GmailService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/v1/webhook")
public class WebhookController {

    private WebhookPublisher webhookPublisher;
    private GmailService gmailService;

    WebhookController(WebhookPublisher webhookPublisher, GmailService gmailService) {
        this.webhookPublisher = webhookPublisher;
        this.gmailService = gmailService;
    }

    @PostMapping("/incoming-email")
    public String postMethodName(@RequestBody String entity) {
        // TODO: process POST request

        return entity;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getMethodName() {
        return ResponseEntity.status(HttpStatus.OK).body(gmailService.getProfile(OAuthSuccessHandler.ACCESS_TOKEN));
    }

    @GetMapping("/latest-emails/{num}")
    public ResponseEntity<?> getLatestEmails(@PathVariable int num) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(gmailService.latestEmails(OAuthSuccessHandler.ACCESS_TOKEN, num));
    }

    @GetMapping("/msgs")
    public ResponseEntity<?> getMsgs() {
        return ResponseEntity.status(HttpStatus.OK).body(gmailService.listMessages(OAuthSuccessHandler.ACCESS_TOKEN));
    }

    @GetMapping("/msgs/{id}")
    public ResponseEntity<?> getMsgs(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.OK).body(gmailService.getMessage(OAuthSuccessHandler.ACCESS_TOKEN, id));
    }

    @GetMapping("/new-emails")
    public ResponseEntity<?> getNewEmails() {
        return ResponseEntity.status(HttpStatus.OK).body(gmailService.getNewerEmails(OAuthSuccessHandler.ACCESS_TOKEN));
    }
}
