package com.envifo_backend_java.Envifo_backend_java.infrastructure.config;


import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_NAME = "materials_queue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, true);
    }
}