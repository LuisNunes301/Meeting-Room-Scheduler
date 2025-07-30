package com.MeetingRoomScheduler.rabbit.reservation.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.MeetingRoomScheduler.dto.event.ReservationStatusUpdatedEvent;
import com.MeetingRoomScheduler.rabbit.RabbitMQConfig;

@Component
public class ReservationStatusUpdatedPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ReservationStatusUpdatedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(ReservationStatusUpdatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_STATUS_UPDATED_EXCHANGE,
                RabbitMQConfig.RESERVATION_STATUS_UPDATED_ROUTING_KEY,
                event);
    }
}