package com.example.rabbit_config.Model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReceivedEmail {

    private String messageId;
    private String subject;
    private String from;
    private String to;
    private String date;
    private String body;

    public ReceivedEmail(
            String messageId,
            String subject,
            String from,
            String to,
            String date,
            String body) {

        this.messageId = messageId;
        this.subject = subject;
        this.from = from;
        this.to = to;
        this.date = date;
        this.body = body;
    }

}