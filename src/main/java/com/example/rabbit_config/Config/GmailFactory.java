package com.example.rabbit_config.Config;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.gmail.Gmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GmailFactory {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public Gmail getClient(
            String accessToken,
            String refreshToken,
            Long expirationTimeMillis) throws Exception {

        NetHttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();

        GoogleCredential credential = new GoogleCredential.Builder()
                .setTransport(transport)
                .setJsonFactory(GsonFactory.getDefaultInstance())
                .setClientSecrets(clientId, clientSecret)
                .build();

        credential.setAccessToken(accessToken);
        credential.setRefreshToken(refreshToken);
        credential.setExpirationTimeMilliseconds(expirationTimeMillis);

        return new Gmail.Builder(
                transport,
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName("AI Email Processor")
                .build();
    }
}