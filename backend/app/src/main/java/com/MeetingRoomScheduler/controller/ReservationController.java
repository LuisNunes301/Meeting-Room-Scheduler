package com.MeetingRoomScheduler.controller;

import com.MeetingRoomScheduler.dto.request.CreateReservationRequest;
import com.MeetingRoomScheduler.dto.request.UpdateReservationStatusRequest;
import com.MeetingRoomScheduler.entities.Reservation.*;
import com.MeetingRoomScheduler.entities.room.Room;
import com.MeetingRoomScheduler.entities.user.CustomUserDetails;
import com.MeetingRoomScheduler.entities.user.User;
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
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(value = "roomId", required = false) Long roomId,
            @RequestParam(value = "status", required = false) ReservationStatus status) {

        Long userId = currentUser.getId();
        List<Reservation> reservations;

        if (roomId != null && status != null) {
            reservations = reservationService.getAllReservations().stream()
                    .filter(r -> r.getUser().getId().equals(userId))
                    .filter(r -> r.getRoom().getId().equals(roomId))
                    .filter(r -> r.getStatus().equals(status))
                    .toList();
        } else if (roomId != null) {
            reservations = reservationService.getReservationsByRoomId(roomId).stream()
                    .filter(r -> r.getUser().getId().equals(userId))
                    .toList();
        } else if (status != null) {
            reservations = reservationService.getReservationsByStatus(status).stream()
                    .filter(r -> r.getUser().getId().equals(userId))
                    .toList();
        } else {
            reservations = reservationService.getReservationsByUserId(userId);
        }

        return reservations.stream()
                .map(ReservationDto::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ReservationDto createReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @Valid @RequestBody CreateReservationRequest request) {

        User user = userService.validateAndGetUserByUsername(currentUser.getUsername());

        Room room = roomService.validateAndGetRoom(request.roomId());

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
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationStatusRequest request) {

        Reservation reservation = reservationService.validateAndGetReservation(id);

        // Check if user is admin or owns the reservation
        if (!currentUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")) &&
                !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only update your own reservations.");
        }

        Reservation updated = reservationService.updateReservationStatus(id, request.status());
        return ReservationDto.from(updated);
    }

    @PostMapping("/{id}/cancel")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ReservationDto cancelReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);

        // Check if user is admin or owns the reservation
        if (!currentUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")) &&
                !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only cancel your own reservations.");
        }

        reservationService.cancelReservation(reservation);
        return ReservationDto.from(reservation);
    }

    @PostMapping("/{id}/release")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ReservationDto releaseReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);

        // Check if user is admin or owns the reservation
        if (!currentUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")) &&
                !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only release your own reservations.");
        }

        reservationService.releaseReservationSlot(reservation);
        return ReservationDto.from(reservation);
    }

    @PostMapping("/{id}/confirm")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ReservationDto confirmReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);

        // Check if user is admin or owns the reservation
        if (!currentUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")) &&
                !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only confirm your own reservations.");
        }

        reservationService.confirmReservation(reservation);
        return ReservationDto.from(reservation);
    }

    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    @DeleteMapping("/{id}")
    public ReservationDto deleteReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);

        // Check if user is admin or owns the reservation
        if (!currentUser.getAuthorities().stream().anyMatch(auth -> auth.getAuthority().equals("ADMIN")) &&
                !reservation.getUser().getId().equals(currentUser.getId())) {
            throw new IllegalStateException("You can only delete your own reservations.");
        }

        reservationService.deleteReservation(reservation);
        return ReservationDto.from(reservation);
    }
}
