// package com.MeetingRoomScheduler.rabbit.passwordReset;

// import java.util.HashMap;
// import java.util.Map;

// import org.springframework.amqp.rabbit.annotation.RabbitListener;
// import org.springframework.stereotype.Component;

// import com.MeetingRoomScheduler.dto.event.PasswordResetEvent;
// import com.MeetingRoomScheduler.service.impl.EmailService;

// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// @Component
// public class PasswordResetConsumer {
// private final EmailService emailService;

// @RabbitListener(queues = "user.password.reset")
// public void handle(PasswordResetEvent event) {
// Map<String, Object> model = new HashMap<>();

// model.put("userName", event.getUserName());
// model.put("token", event);
// model.put("resetUrl", "http://localhost:3000/reset-password?token=" + event);

// emailService.sendHtmlEmail(
// event.getEmail(),
// "Redefinição de Senha",
// model,
// "reset-password");
// }
// }
