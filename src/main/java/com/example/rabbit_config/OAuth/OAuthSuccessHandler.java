package com.example.rabbit_config.OAuth;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.example.rabbit_config.Config.GmailFactory;
import com.example.rabbit_config.Service.UserTokenService;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.WatchRequest;
import com.google.api.services.gmail.model.WatchResponse;

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(OAuthSuccessHandler.class);
    public static String ACCESS_TOKEN;

    private final OAuth2AuthorizedClientService authorizedClientService;
    private final UserTokenService userTokenService;
    private final GmailFactory gmailFactory;

    public OAuthSuccessHandler(
            OAuth2AuthorizedClientService authorizedClientService, GmailFactory gmailFactory,
            UserTokenService userTokenService) {
        this.authorizedClientService = authorizedClientService;
        this.gmailFactory = gmailFactory;
        this.userTokenService = userTokenService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

        String currEmail = oauthToken.getPrincipal().getAttribute("email");

        log.info("Logged in user : {}", currEmail);

        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                oauthToken.getAuthorizedClientRegistrationId(),
                oauthToken.getName());

        if (client == null) {
            response.getWriter().write("Authorized client not found.");
            return;
        }

        ACCESS_TOKEN = client.getAccessToken().getTokenValue();

        log.info("\n==============================");
        log.info("ACCESS TOKEN");
        log.info(client.getAccessToken().getTokenValue());
        log.info("==============================");

        String accessToken = client.getAccessToken().getTokenValue();

        String refreshToken = null;

        if (client.getRefreshToken() != null) {
            refreshToken = client.getRefreshToken().getTokenValue();
        }

        long expirationMillis = client.getAccessToken()
                .getExpiresAt()
                .toEpochMilli();

        Gmail gmail = null;
        try {
            gmail = gmailFactory.getClient(
                    accessToken,
                    refreshToken,
                    expirationMillis);
        } catch (Exception e) {
            log.error("Error creating client");
            e.getMessage();
        }

        WatchRequest request1 = new WatchRequest()
                .setTopicName(
                        "projects/ai-email-processor-501920/topics/email-analyser-topic")
                .setLabelIds(List.of("INBOX"))
                .setLabelFilterBehavior("INCLUDE");

        WatchResponse watchResponse = gmail.users()
                .watch("me", request1)
                .execute();

        userTokenService.saveUserTokenData(currEmail, accessToken, refreshToken,
                watchResponse.getHistoryId().toString(), expirationMillis);

        response.getWriter().write("Login Successful");
    }
}