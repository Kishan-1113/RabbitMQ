package com.example.rabbit_config.Controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.rabbit_config.OAuth.OAuthSuccessHandler;
import com.example.rabbit_config.Service.GmailService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class Hello {

    private GmailService gService;

    Hello(GmailService gmailService) {
        this.gService = gmailService;
    }

    @GetMapping("/hello")
    public String getMethodName() {
        return new String("Hello sir, Welcome !");
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile() {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(gService.getProfile(OAuthSuccessHandler.ACCESS_TOKEN));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Server Error");
        }
    }
}
