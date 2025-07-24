package com.meetingroom.meetingroomscheduler.domain.model;

import com.meetingroom.meetingroomscheduler.domain.enums.PaymentStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    private UUID id;
    private UUID reservationId;
    private double amount;
    private PaymentStatus status;
    private String stripePaymentId;
    private LocalDateTime createdAt;

    public Payment(UUID reservationId, double amount) {
        this.id = UUID.randomUUID();
        this.reservationId = reservationId;
        this.amount = amount;
        this.status = PaymentStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }

    public Payment() {

    }

    public UUID getId() {
        return id;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public double getAmount() {
        return amount;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public String getStripePaymentId() {
        return stripePaymentId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void markAsPaid(String stripePaymentId) {
        this.status = PaymentStatus.PAID;
        this.stripePaymentId = stripePaymentId;
    }

    public void markAsFailed() {
        this.status = PaymentStatus.FAILED;
    }
}
