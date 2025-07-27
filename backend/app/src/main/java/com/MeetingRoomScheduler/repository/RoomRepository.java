package com.MeetingRoomScheduler.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.MeetingRoomScheduler.domain.Reservation.Reservation;
import com.MeetingRoomScheduler.domain.room.Room;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findByAvailableTrue();

    List<Room> findByCapacityGreaterThanEqual(Integer minCapacity);

    List<Room> findByLocationContainingIgnoreCase(String location);

    boolean existsByName(String name);

    List<Reservation> findByRoomId(Long roomId);
}
