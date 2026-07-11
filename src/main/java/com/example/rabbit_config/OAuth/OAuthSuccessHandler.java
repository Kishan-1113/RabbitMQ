package com.example.rabbit_config.OAuth;

import java.io.IOException;

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

@Component
public class OAuthSuccessHandler implements AuthenticationSuccessHandler {

    private final Logger log = LoggerFactory.getLogger(OAuthSuccessHandler.class);
    public static String ACCESS_TOKEN;

    private final OAuth2AuthorizedClientService authorizedClientService;

    public OAuthSuccessHandler(
            OAuth2AuthorizedClientService authorizedClientService) {
        this.authorizedClientService = authorizedClientService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication)
            throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;

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
        log.info("==============================");
        log.info(client.getAccessToken().getTokenValue());

        // log.info("\nTOKEN TYPE");
        // log.info(client.getAccessToken().getTokenType().getValue());

        // log.info("\nEXPIRES AT");
        // log.info(client.getAccessToken().getExpiresAt());

        // log.info("\nSCOPES");
        // log.info(client.getAccessToken().getScopes());

        response.getWriter().write("Login Successful");
    }
}