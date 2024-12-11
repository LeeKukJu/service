package com.delivery.storeadmin.domain.userorder.consumer;

import com.delivery.common.message.model.UserOrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class UserOrderConsumer {

    @RabbitListener(queues = "delivery.queue")
    public void userOrderConsumer(
        UserOrderMessage userOrderMessage
    ) {
        log.info("message queue >> {}", userOrderMessage);
    }
}
