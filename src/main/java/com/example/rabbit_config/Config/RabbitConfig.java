package com.example.rabbit_config.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Testing
    private static final String LISTENER_QUEUE = "Listener_queue";
    private static final String LISTENER_EXCHANGE = "Listener_Exchange";
    private static final String LISTENER_ROUTING_KEY = "The_Routing_Key";

    // Working queues
    private static final String EMAIL_QUEUE = "Email_queue";
    private static final String GEMINI_QUEUE = "Gemini_queue";

    // Retry queues
    private static final String EMAIL_RETRY_QUEUE = "Email_queue";
    private static final String GEMINI_RETRY_QUEUE = "Gemini_queue";

    // Main exchanges
    private static final String EMAIL_EXCHANGE = "Email_exchange";
    private static final String GEMINI_EXCHANGE = "Gemini_exchange";

    // Main routing keys
    private static final String EMAIL_ROUTING_KEY = "email_routing";
    private static final String GEMINI_ROUTING_KEY = "gemini_routing";

    // Retry routing keys
    private static final String EMAIL_RETRY_ROUTING_KEY = "email_retry_routing";
    private static final String GEMINI_RETRY_ROUTING_KEY = "gemini_retry_routing";

    @Bean
    public Queue listenerQueue() {
        return QueueBuilder.durable(LISTENER_QUEUE).build();
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
    }

    @Bean
    public Queue geminiQueue() {
        return QueueBuilder.durable(GEMINI_QUEUE).build();
    }

    @Bean
    public DirectExchange listenerExchange() {
        return new DirectExchange(LISTENER_EXCHANGE);
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
    public Binding listenerBinding() {
        return BindingBuilder
                .bind(listenerQueue())
                .to(listenerExchange())
                .with(LISTENER_ROUTING_KEY);
    }

    @Bean
    public Binding emailBinding() {
        return BindingBuilder
                .bind(emailQueue())
                .to(emailExchange())
                .with(EMAIL_ROUTING_KEY);
    }

    @Bean
    public Binding geminiBinding() {
        return BindingBuilder
                .bind(geminiQueue())
                .to(geminiExchange())
                .with(GEMINI_ROUTING_KEY);
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