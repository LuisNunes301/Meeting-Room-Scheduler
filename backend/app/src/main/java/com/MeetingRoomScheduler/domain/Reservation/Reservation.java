package com.MeetingRoomScheduler.domain.Reservation;

import jakarta.persistence.*;
import lombok.*;

import com.MeetingRoomScheduler.domain.room.Room;
import com.MeetingRoomScheduler.domain.user.User;
import com.MeetingRoomScheduler.dto.request.CreateReservationRequest;

import java.time.Instant;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDateTime startTime;

    @Column(nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    private Instant createdAt;

    @PrePersist
    public void onPrePersist() {
        createdAt = Instant.now();
    }

}
