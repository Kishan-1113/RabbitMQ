package com.example.rabbit_config.Config;

public final class MessageHeaders {

    public static final String RETRY_COUNT = "x-retry-count";
    public static final String LAST_ERROR = "x-last-error";
    public static final String ORIGINAL_ROUTING_KEY = "x-original-routing-key";

    private MessageHeaders() {
    }
}