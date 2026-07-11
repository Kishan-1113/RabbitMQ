package com.example.rabbit_config.OAuth;

import lombok.Data;

@Data
public class GoogleTokenResponse {

    private String access_token;

    private String refresh_token;

    private Integer expires_in;

    private String scope;

    private String token_type;

}