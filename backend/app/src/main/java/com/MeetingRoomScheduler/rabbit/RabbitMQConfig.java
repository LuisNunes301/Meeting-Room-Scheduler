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
    public static final String APP_DIRECT_EXCHANGE = "app.direct.exchange";

    // User registration
    public static final String USER_REGISTERED_QUEUE = "user.registered";

    public static final String USER_REGISTERED_ROUTING_KEY = "user.registered";
    // Change password
    public static final String USER_PASSWORD_FORGOT_QUEUE = "user.password.forgot";
    public static final String USER_PASSWORD_FORGOT_EXCHANGE = "user.password.forgot.exchange";
    public static final String USER_PASSWORD_FORGOT_ROUTING_KEY = "user.password.forgot";

    // Reservation creation
    public static final String RESERVATION_CREATED_QUEUE = "reservation.created.queue";

    public static final String RESERVATION_CREATED_ROUTING_KEY = "reservation.created";
    // Reservation Update
    public static final String RESERVATION_STATUS_UPDATED_QUEUE = "reservation.status.updated.queue";

    public static final String RESERVATION_STATUS_UPDATED_ROUTING_KEY = "reservation.status.updated";

    @Bean
    public Queue forgotQueue() {
        return QueueBuilder.durable(USER_PASSWORD_FORGOT_QUEUE).build();
    }

    @Bean
    public Binding forgotBinding() {
        return BindingBuilder
                .bind(forgotQueue())
                .to(appDirectExchange())
                .with(USER_PASSWORD_FORGOT_ROUTING_KEY);
    }

    @Bean
    public Queue userRegisteredQueue() {
        return QueueBuilder.durable(USER_REGISTERED_QUEUE).build();
    }

    @Bean
    public Binding userRegisteredBinding() {
        return BindingBuilder
                .bind(userRegisteredQueue())
                .to(appDirectExchange())
                .with(USER_REGISTERED_ROUTING_KEY);
    }

    @Bean
    public Queue reservationCreatedQueue() {
        return QueueBuilder.durable(RESERVATION_CREATED_QUEUE).build();
    }

    @Bean
    public Binding reservationCreatedBinding() {
        return BindingBuilder
                .bind(reservationCreatedQueue())
                .to(appDirectExchange())
                .with(RESERVATION_CREATED_ROUTING_KEY);
    }

    @Bean
    public Queue reservationUpdatedQueue() {
        return QueueBuilder.durable(RESERVATION_STATUS_UPDATED_QUEUE).build();
    }

    @Bean
    public DirectExchange appDirectExchange() {
        return new DirectExchange(APP_DIRECT_EXCHANGE);
    }

    @Bean
    public Binding reservationUpdatedBinding() {
        return BindingBuilder
                .bind(reservationUpdatedQueue())
                .to(appDirectExchange())
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