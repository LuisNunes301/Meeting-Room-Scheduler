package com.MeetingRoomScheduler.rabbit.user.passwordForgot;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.MeetingRoomScheduler.dto.event.PasswordForgotEvent;
import com.MeetingRoomScheduler.rabbit.RabbitMQConfig;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PasswordForgotPublisher {

    private final RabbitTemplate rabbitTemplate;

    public void publish(PasswordForgotEvent event) {
        rabbitTemplate.convertAndSend(RabbitMQConfig.APP_DIRECT_EXCHANGE,
                RabbitMQConfig.USER_PASSWORD_FORGOT_ROUTING_KEY, event);

    }
}