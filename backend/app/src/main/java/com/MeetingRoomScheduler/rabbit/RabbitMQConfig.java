package com.MeetingRoomScheduler.rabbit;

import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQConfig {

    // User registration
    public static final String USER_REGISTERED_QUEUE = "user.registered";
    public static final String USER_CREATED_EXCHANGE = "user.created.exchange";
    public static final String USER_REGISTERED_ROUTING_KEY = "user.registered";

    // Reservation creation
    public static final String RESERVATION_CREATED_QUEUE = "reservation.created.queue";
    public static final String RESERVATION_CREATED_EXCHANGE = "reservation.exchange";
    public static final String RESERVATION_CREATED_ROUTING_KEY = "reservation.created";
    // Ressrvation Update
    public static final String RESERVATION_STATUS_UPDATED_QUEUE = "reservation.status.updated.queue";
    public static final String RESERVATION_STATUS_UPDATED_EXCHANGE = "reservation.status.updated.exchange";
    public static final String RESERVATION_STATUS_UPDATED_ROUTING_KEY = "reservation.status.updated";

    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable(USER_REGISTERED_QUEUE).build();
    }

    @Bean
    public DirectExchange userCreatedExchange() {
        return new DirectExchange(USER_CREATED_EXCHANGE);
    }

    @Bean
    public Binding userRegisteredBinding() {
        return BindingBuilder
                .bind(userRegisteredQueue())
                .to(userCreatedExchange())
                .with(USER_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Queue reservationCreatedQueue() {
        return QueueBuilder.durable(RESERVATION_CREATED_QUEUE).build();
    }

    @Bean
    public DirectExchange reservationCreatedExchange() {
        return new DirectExchange(RESERVATION_CREATED_EXCHANGE);
    }

    @Bean
    public Binding reservationCreatedBinding() {
        return BindingBuilder
                .bind(reservationCreatedQueue())
                .to(reservationCreatedExchange())
                .with(RESERVATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue reservationUpdatedQueue() {
        return QueueBuilder.durable(RESERVATION_STATUS_UPDATED_QUEUE).build();
    }

    @Bean
    public DirectExchange reservationUpdatedExchange() {
        return new DirectExchange(RESERVATION_STATUS_UPDATED_EXCHANGE);
    }

    @Bean
    public Binding reservationUpdatedBinding() {
        return BindingBuilder
                .bind(reservationUpdatedQueue())
                .to(reservationUpdatedExchange())
                .with(RESERVATION_STATUS_UPDATED_ROUTING_KEY);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jsonMessageConverter());
        return template;
    }
}