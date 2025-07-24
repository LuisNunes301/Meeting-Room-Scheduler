package com.meetingroom.meetingroomscheduler.ports.out;

import com.meetingroom.meetingroomscheduler.domain.model.Payment;

public interface PaymentGatewayPort {
    Payment processPayment(Payment payment);
}