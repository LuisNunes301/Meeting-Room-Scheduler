package com.MeetingRoomScheduler.rabbit.reservation.consumer;

import com.MeetingRoomScheduler.dto.event.ReservationStatusUpdatedEvent;
import com.MeetingRoomScheduler.rabbit.RabbitMQConfig;
import com.MeetingRoomScheduler.service.impl.EmailService;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class ReservationStatusUpdatedEmailConsumer {

    private final EmailService emailService;

    public ReservationStatusUpdatedEmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.RESERVATION_STATUS_UPDATED_QUEUE)
    public void handleReservationStatusUpdated(ReservationStatusUpdatedEvent event) {
        Map<String, Object> model = new HashMap<>();
        model.put("reservationId", event.getReservationId());
        model.put("roomName", event.getRoomName());
        model.put("oldStatus", event.getOldStatus().name());
        model.put("newStatus", event.getNewStatus().name());
        model.put("updatedAt", event.getUpdatedAt());

        try {
            // Email para o usuário
            emailService.sendHtmlEmail(
                    event.getUserEmail(),
                    "Status da Reserva Atualizado",
                    model,
                    "reservation-update-client");

            // Email para o admin (caso tenha um destinatário fixo ou adicional)
            emailService.sendHtmlEmail(
                    "admin@meetingroom.com", // Substitua por e-mail real ou pegue via propriedade
                    "Reserva #" + event.getReservationId() + " Atualizada",
                    model,
                    "reservation-update-admin");
        } catch (Exception e) {
            e.printStackTrace(); // ou logger.warn(...)
        }
    }
}
