package com.example.rabbit_config.Config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static final String LISTENER_QUEUE = "Listener_queue";
    private static final String EMAIL_QUEUE = "Email_queue";

    private static final String LISTENER_EXCHANGE = "Listener_Exchange";
    private static final String EMAIL_EXCHANGE = "Email_exchange";

    private static final String LISTENER_ROUTING_KEY = "The_Routing_Key";
    private static final String EMAIL_ROUTING_KEY = "email_routing";

    @Bean
    public Queue listenerQueue() {
        return QueueBuilder.durable(LISTENER_QUEUE).build();
    }

    @Bean
    public Queue emailQueue() {
        return QueueBuilder.durable(EMAIL_QUEUE).build();
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