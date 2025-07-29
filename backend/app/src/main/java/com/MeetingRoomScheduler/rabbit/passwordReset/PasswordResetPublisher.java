// package com.MeetingRoomScheduler.rabbit.passwordReset;

// import org.springframework.amqp.rabbit.core.RabbitTemplate;
// import org.springframework.stereotype.Component;

// import com.MeetingRoomScheduler.dto.event.PasswordResetEvent;

// import lombok.RequiredArgsConstructor;

// @RequiredArgsConstructor
// @Component
// public class PasswordResetPublisher {

// private final RabbitTemplate rabbitTemplate;

// public void publish(PasswordResetEvent event) {
// rabbitTemplate.convertAndSend("app.direct.exchange", "user.password.reset",
// event);
// }
// }