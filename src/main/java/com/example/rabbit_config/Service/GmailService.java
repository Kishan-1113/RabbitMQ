package com.example.rabbit_config.Service;

import java.util.Base64;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GmailService {

    public static final String BASE_URL = "https://gmail.googleapis.com/gmail/v1/users/me";

    private final RestClient restClient;

    public GmailService(RestClient restClient) {
        this.restClient = restClient;
    }

    public String getProfile(String accessToken) {

        return restClient.get()
                .uri(BASE_URL + "/profile")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }

    public String listMessages(String accessToken) {

        return restClient.get()
                .uri(BASE_URL + "/messages")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }

    public String getMessage(String accessToken, String messageId) {

        return restClient.get()
                .uri(BASE_URL + "/messages/{id}", messageId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }

    public String latestEmails(String accessToken, int numEmails) {

        return restClient.get()
                .uri(BASE_URL + "/messages?maxResults=" + numEmails)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }

    public String getUnreadEmails(String accessToken) {

        return restClient.get()
                .uri(BASE_URL + "/messages?q=is:unread")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }

    public String emailsFromSpecificSender(String accessToken, String sender) {

        return restClient.get()
                .uri(BASE_URL + "/messages?q=from:" + sender)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }

    public String getNewerEmails(String accessToken) {

        return restClient.get()
                .uri(BASE_URL + "/messages?q=newer_than:1d")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .body(String.class);
    }
}