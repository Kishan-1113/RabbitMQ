package com.example.rabbit_config.Service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import com.google.api.services.gmail.model.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartHeader;

import com.example.rabbit_config.Config.GmailFactory;
import com.example.rabbit_config.Model.ReceivedEmail;
import com.example.rabbit_config.Model.DB_Tables.UserToken;
import com.example.rabbit_config.Repository.UserTokenRepo;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.History;
import com.google.api.services.gmail.model.HistoryMessageAdded;
import com.google.api.services.gmail.model.ListHistoryResponse;

@Service
public class GmailService {

    public static final String BASE_URL = "https://gmail.googleapis.com/gmail/v1/users/me";

    private final RestClient restClient;
    private final GmailFactory gmailFactory;
    private final UserTokenRepo userTokenRepo;

    public GmailService(RestClient restClient, GmailFactory gmailFactory, UserTokenRepo userTokenRepo) {
        this.restClient = restClient;
        this.gmailFactory = gmailFactory;
        this.userTokenRepo = userTokenRepo;
    }

    public List<ReceivedEmail> getNewEmails(
            String emailAddress,
            String previousHistoryId) throws Exception {

        UserToken token = userTokenRepo.findByEmail(emailAddress)
                .orElseThrow(() -> new RuntimeException("User not found in User Token DB Table"));

        Gmail gmail = gmailFactory.getClient(
                token.getAccessToken(),
                token.getRefreshToken(),
                token.getExpirationTimeMillis());

        List<ReceivedEmail> emails = new ArrayList<>();

        ListHistoryResponse response = gmail.users()
                .history()
                .list(emailAddress)
                .setStartHistoryId(new BigInteger(previousHistoryId))
                .execute();

        if (response.getHistory() == null)
            return emails;

        for (History history : response.getHistory()) {

            if (history.getMessagesAdded() == null)
                continue;

            for (HistoryMessageAdded added : history.getMessagesAdded()) {

                String messageId = added.getMessage().getId();

                Message message = gmail.users()
                        .messages()
                        .get(emailAddress, messageId)
                        .setFormat("full")
                        .execute();

                emails.add(convert(message));
            }
        }

        return emails;
    }

    private ReceivedEmail convert(Message message) {

        String subject = "";
        String from = "";
        String to = "";
        String date = "";

        for (MessagePartHeader header : message.getPayload().getHeaders()) {

            switch (header.getName()) {

                case "Subject":
                    subject = header.getValue();
                    break;

                case "From":
                    from = header.getValue();
                    break;

                case "To":
                    to = header.getValue();
                    break;

                case "Date":
                    date = header.getValue();
                    break;
            }
        }

        String body = "";

        if (message.getPayload().getBody() != null &&
                message.getPayload().getBody().getData() != null) {

            body = new String(
                    Base64.getUrlDecoder()
                            .decode(message.getPayload().getBody().getData()));
        } else {

            body = extractBody(message.getPayload().getParts());
        }

        return new ReceivedEmail(
                message.getId(),
                subject,
                from,
                to,
                date,
                body);
    }

    private String extractBody(List<MessagePart> parts) {

        if (parts == null)
            return "";

        for (MessagePart part : parts) {

            if ("text/plain".equals(part.getMimeType())) {
                if (part.getBody() != null &&
                        part.getBody().getData() != null) {

                    return new String(
                            Base64.getUrlDecoder()
                                    .decode(part.getBody().getData()));
                }
            }
            if (part.getParts() != null) {
                String result = extractBody(part.getParts());

                if (!result.isBlank())
                    return result;
            }
        }
        return "";
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