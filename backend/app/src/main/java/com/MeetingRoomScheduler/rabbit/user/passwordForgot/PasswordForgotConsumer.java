package com.MeetingRoomScheduler.rabbit.user.passwordForgot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.MeetingRoomScheduler.dto.event.PasswordForgotEvent;

import com.MeetingRoomScheduler.service.impl.EmailService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Component
public class PasswordForgotConsumer {
    private final EmailService emailService;

    @RabbitListener(queues = "user.password.forgot")
    public void handle(PasswordForgotEvent event) {
        Map<String, Object> model = new HashMap<>();

        model.put("userName", event.getUserName());
        model.put("token", event.getToken());
        // ajustar a rota do reset de senha conforme necessário
        model.put("resetUrl", "http://localhost:3000/reset-password?token=" + event.getToken());

        emailService.sendHtmlEmail(
                event.getEmail(),
                "Redefinição de Senha",
                model,
                "reset-password");
    }
}
