package com.example.rabbit_config.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DecodedGmailPayload {

    private String emailAddress;
    private String historyId;
}
