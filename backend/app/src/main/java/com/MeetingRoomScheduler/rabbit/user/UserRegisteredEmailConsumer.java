package com.MeetingRoomScheduler.rabbit.user;

import com.MeetingRoomScheduler.dto.event.UserRegisteredEvent;
import com.MeetingRoomScheduler.rabbit.RabbitMQConfig;
import com.MeetingRoomScheduler.service.impl.EmailService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class UserRegisteredEmailConsumer {

    private final EmailService emailService;

    public UserRegisteredEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.USER_REGISTERED_QUEUE)

    public void handleUserRegistered(UserRegisteredEvent event) {
        Map<String, Object> model = new HashMap<>();
        model.put("userName", event.getUserName());

        emailService.sendHtmlEmail(
                event.getUserEmail(),
                "Bem-vindo ao Sistema!",
                model,
                "welcome");
    }
}