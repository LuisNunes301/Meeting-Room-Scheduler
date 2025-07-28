package com.MeetingRoomScheduler.rabbit;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.MeetingRoomScheduler.dto.event.ReservationCreatedEvent;

@Component
public class ReservationCreatedPublisher {

    private final RabbitTemplate rabbitTemplate;

    public ReservationCreatedPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publish(ReservationCreatedEvent event) {
        rabbitTemplate.convertAndSend("reservation.exchange", "reservation.created", event);
    }
}
