package com.MeetingRoomScheduler.controller;

import com.MeetingRoomScheduler.domain.Reservation.*;
import com.MeetingRoomScheduler.domain.room.Room;
import com.MeetingRoomScheduler.domain.user.CustomUserDetails;
import com.MeetingRoomScheduler.domain.user.User;
import com.MeetingRoomScheduler.dto.request.CreateReservationRequest;
import com.MeetingRoomScheduler.service.ReservationService;
import com.MeetingRoomScheduler.service.RoomService;
import com.MeetingRoomScheduler.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.MeetingRoomScheduler.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final UserService userService;
    private final ReservationService reservationService;
    private final RoomService roomService;

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @GetMapping
    public List<ReservationDto> getReservations(
            @RequestParam(value = "roomId", required = false) Long roomId,
            @RequestParam(value = "status", required = false) ReservationStatus status) {

        List<Reservation> reservations;

        if (roomId != null && status != null) {
            reservations = reservationService.getReservationsByRoomAndStatus(roomId, status);
        } else if (roomId != null) {
            reservations = reservationService.getReservationsByRoomId(roomId);
        } else if (status != null) {
            reservations = reservationService.getReservationsByStatus(status);
        } else {
            reservations = reservationService.getAllReservations();
        }

        return reservations.stream()
                .map(ReservationDto::from)
                .collect(Collectors.toList());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ReservationDto createReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateReservationRequest request) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        Room room = roomService.validateAndGetRoom(request.roomId()); // agora existe o campo roomId()

        Reservation reservation = Reservation.builder()
                .user(user)
                .room(room)
                .startTime(request.startTime())
                .endTime(request.endTime())
                .status(ReservationStatus.PENDING)
                .build();

        return ReservationDto.from(reservationService.saveReservation(reservation));
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @PutMapping("/{id}/status")
    public ReservationDto updateReservationStatus(
            @PathVariable Long id,
            @RequestParam ReservationStatus status) {

        Reservation reservation = reservationService.validateAndGetReservation(id);
        reservation.setStatus(status);
        return ReservationDto.from(reservationService.saveReservation(reservation));
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @DeleteMapping("/{id}")
    public ReservationDto deleteReservation(@PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);
        reservationService.deleteReservation(reservation);
        return ReservationDto.from(reservation);
    }
}
