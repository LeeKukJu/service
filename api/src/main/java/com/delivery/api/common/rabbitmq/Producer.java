package com.delivery.api.common.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Producer {

    private final RabbitTemplate rabbitTemplate;

    public void producer(String exchange, String routeKey, Object data) {
        rabbitTemplate.convertAndSend(exchange, routeKey, data);
    }
}
