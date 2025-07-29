package com.MeetingRoomScheduler.rabbit.userRegister;

import com.MeetingRoomScheduler.dto.event.UserRegisteredEvent;
import com.MeetingRoomScheduler.rabbit.RabbitMQConfig;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class UserCreatedPublisher {

    private final RabbitTemplate rabbitTemplate;

    public UserCreatedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(UserRegisteredEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.USER_CREATED_EXCHANGE,
                RabbitMQConfig.USER_REGISTERED_ROUTING_KEY,
                event);
    }
}