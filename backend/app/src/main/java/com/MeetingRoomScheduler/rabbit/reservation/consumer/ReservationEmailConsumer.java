package com.MeetingRoomScheduler.rabbit.reservation.consumer;

import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.rabbit.RabbitMQConfig;
import com.MeetingRoomScheduler.service.impl.EmailService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReservationEmailConsumer {

    private final EmailService emailService;

    public ReservationEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.RESERVATION_CREATED_QUEUE)
    public void handleReservationCreated(ReservationCreatedEvent event) {
        Map<String, Object> model = new HashMap<>();
        model.put("userName", event.getUserName());
        model.put("roomName", event.getRoomName());
        model.put("roomLocation", event.getRoomLocation());
        model.put("startTime", event.getStartTime());
        model.put("endTime", event.getEndTime());

        try {
            // Email para o usuário
            emailService.sendHtmlEmail(
                    event.getUserEmail(),
                    "Confirmação de Reserva",
                    model,
                    "email-cliente");

            // Email para o admin
            emailService.sendHtmlEmail(
                    event.getAdminEmail(),
                    "Nova Reserva Realizada",
                    model,
                    "email-admin");
        } catch (Exception e) {
            e.printStackTrace(); // Ou use um logger
        }
    }
}