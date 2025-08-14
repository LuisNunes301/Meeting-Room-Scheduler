package com.MeetingRoomScheduler.controller;

import com.MeetingRoomScheduler.dto.request.CreateReservationRequest;
import com.MeetingRoomScheduler.dto.request.UpdateReservationRequest;
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
import org.springframework.security.access.prepost.PreAuthorize;
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
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @RequestParam(value = "roomId", required = false) Long roomId,
            @RequestParam(value = "status", required = false) ReservationStatus status) {
        List<Reservation> reservations = reservationService.getReservationsByFilters(roomId, status)
                .stream()
                .filter(r -> r.getUser().getId().equals(currentUser.getId()))
                .collect(Collectors.toList());

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
        Reservation reservation = Reservation.builder()
                .user(user)
                .room(roomService.validateAndGetRoom(request.roomId()))
                .startTime(request.startTime())
                .endTime(request.endTime())
                .status(ReservationStatus.PENDING)
                .build();

        return ReservationDto.from(reservationService.saveReservation(reservation));
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ReservationDto updateReservationAdmin(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationRequest request) {
        return ReservationDto.from(reservationService.updateReservationAsAdmin(id, request));
    }

    @PutMapping("/user/{id}")
    @PreAuthorize("hasRole('USER')")
    public ReservationDto updateReservationUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReservationRequest request,
            @AuthenticationPrincipal CustomUserDetails currentUser) {
        return ReservationDto.from(reservationService.updateReservationAsUser(id, request, currentUser.getUsername()));
    }

    @PostMapping("/{id}/cancel")
    public ReservationDto cancelReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);
        checkOwnershipOrAdmin(currentUser, reservation);
        return ReservationDto.from(reservationService.cancelReservation(reservation));
    }

    @PostMapping("/{id}/confirm")
    public ReservationDto confirmReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);
        checkOwnershipOrAdmin(currentUser, reservation);
        return ReservationDto.from(reservationService.confirmReservation(reservation));
    }

    @PostMapping("/{id}/release")
    public ReservationDto releaseReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);
        checkOwnershipOrAdmin(currentUser, reservation);
        return ReservationDto.from(reservationService.releaseReservationSlot(reservation));
    }

    @DeleteMapping("/{id}")
    public ReservationDto deleteReservation(
            @AuthenticationPrincipal CustomUserDetails currentUser,
            @PathVariable Long id) {
        Reservation reservation = reservationService.validateAndGetReservation(id);
        checkOwnershipOrAdmin(currentUser, reservation);
        reservationService.deleteReservation(reservation);
        return ReservationDto.from(reservation);
    }

    private void checkOwnershipOrAdmin(CustomUserDetails currentUser, Reservation reservation) {
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
        boolean isOwner = reservation.getUser().getId().equals(currentUser.getId());
        if (!isAdmin && !isOwner) {
            throw new IllegalStateException("You can only modify your own reservations.");
        }
    }
}