package com.meetingroom.meetingroomscheduler.adapters.inbound.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meetingroom.meetingroomscheduler.domain.model.Reservation;
import com.meetingroom.meetingroomscheduler.ports.in.ReservationUseCase;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationUseCase reservationUseCase;

    public ReservationController(ReservationUseCase reservationUseCase) {
        this.reservationUseCase = reservationUseCase;
    }

    @PostMapping
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        return ResponseEntity.ok(reservationUseCase.createReservation(reservation));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservation(@PathVariable UUID id) {
        Reservation reservation = reservationUseCase.getReservationById(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Reservation>> getReservationsByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(reservationUseCase.getReservationsByUser(userId));
    }
}