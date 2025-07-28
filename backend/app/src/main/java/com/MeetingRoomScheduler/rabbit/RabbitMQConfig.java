package com.MeetingRoomScheduler.rabbit;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.*;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jsonMessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Queue reservationCreatedQueue() {
        return new Queue("reservation.created", true);
    }

    @Bean
    public DirectExchange reservationExchange() {
        return new DirectExchange("reservation.exchange");
    }

    @Bean
    public Binding reservationBinding(Queue reservationCreatedQueue, DirectExchange reservationExchange) {
        return BindingBuilder.bind(reservationCreatedQueue)
                .to(reservationExchange)
                .with("reservation.created");
    }
}