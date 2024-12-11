package com.delivery.api.config.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("delivery.exchange");
    }

    @Bean
    public Queue queue() {
        return new Queue("delivery.queue");
    }

    // Exchange 와 Queue 를 바인딩
    @Bean
    public Binding binding(
        DirectExchange directExchange,
        Queue queue
    ) {
        return BindingBuilder.bind(queue)
            .to(directExchange)
            .with("delivery.key");
    }

    @Bean
    public RabbitTemplate rabbitTemplate(
        ConnectionFactory connectionFactory,
        MessageConverter messageConverter
    ) {
        var rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        return rabbitTemplate;
    }

    // objectmapper 패키지 > ObjectMapperConfig 클래스에서 @Bean 으로 등록한 ObjectMapper 를 MessageConverter 에 주입됨
    @Bean
    public MessageConverter messageConverter(ObjectMapper objectMapper) {
        return new Jackson2JsonMessageConverter(objectMapper);
    }
}
