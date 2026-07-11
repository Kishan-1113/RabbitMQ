package com.example.rabbit_config.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Email System
    public static final String EMAIL_EXCHANGE = "Email_exchange";

    public static final String EMAIL_QUEUE = "Email_queue";
    public static final String EMAIL_ROUTING_KEY = "email_routing";

    public static final String EMAIL_RETRY_QUEUE = "Email_Retry_queue";
    public static final String EMAIL_RETRY_ROUTING_KEY = "email_retry_routing";

    // Gemini System
    public static final String GEMINI_EXCHANGE = "Gemini_exchange";

    public static final String GEMINI_QUEUE = "Gemini_queue";
    public static final String GEMINI_ROUTING_KEY = "gemini_routing";

    public static final String GEMINI_RETRY_QUEUE = "Gemini_Retry_queue";
    public static final String GEMINI_RETRY_KEY = "gemini_retry_routing";

    // Dead letter
    public static final String DEAD_LETTER_EXCNG = "Dead_Letter_exchange";
    public static final String DEAD_LETTER_QUEUE = "Dead_Letter_queue";
    public static final String DEAD_LETTER_KEY = "Dead_Letter_key";

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    @Bean
    public Queue emailRetryQueue() {
        return QueueBuilder
                .durable(EMAIL_RETRY_QUEUE)
                .ttl(45000)
                .deadLetterExchange(EMAIL_EXCHANGE)
                .deadLetterRoutingKey(EMAIL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue geminiQueue() {
        return QueueBuilder.durable(GEMINI_QUEUE).build();
    }

    @Bean
    public Queue geminiRetryQueue() {
        return QueueBuilder
                .durable(GEMINI_RETRY_QUEUE)
                .ttl(45000)
                .deadLetterExchange(GEMINI_EXCHANGE)
                .deadLetterRoutingKey(GEMINI_ROUTING_KEY)
                .build();
    }

    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder
                .durable(DEAD_LETTER_QUEUE)
                .build();
    }

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange(EMAIL_EXCHANGE);
    }

    @Bean
    public DirectExchange geminiExchange() {
        return new DirectExchange(GEMINI_EXCHANGE);
    }

    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DEAD_LETTER_EXCNG);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding emailRetryQuBinding() {
        return BindingBuilder
                .bind(emailRetryQueue())
                .to(emailExchange())
                .with(EMAIL_RETRY_ROUTING_KEY);
    }

    @Bean
    public Binding geminiBinding() {
        return BindingBuilder
                .bind(geminiQueue())
                .to(geminiExchange())
                .with(GEMINI_ROUTING_KEY);
    }

    @Bean
    public Binding geminiRetryQuBinding() {
        return BindingBuilder
                .bind(geminiRetryQueue())
                .to(geminiExchange())
                .with(GEMINI_RETRY_KEY);
    }

    @Bean
    public Binding deadLetterQuBinding() {
        return BindingBuilder
                .bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(DEAD_LETTER_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter converter) {

        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(converter);
        return template;
    }
}