package com.MeetingRoomScheduler.rabbit.reservation.publisher;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;
import com.MeetingRoomScheduler.rabbit.RabbitMQConfig;

@Component
public class ReservationCreatedPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ReservationCreatedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(ReservationCreatedEvent event) {
        rabbitTemplate.convertAndSend(
                RabbitMQConfig.RESERVATION_CREATED_EXCHANGE,
                RabbitMQConfig.RESERVATION_CREATED_ROUTING_KEY,
                event);
    }
}