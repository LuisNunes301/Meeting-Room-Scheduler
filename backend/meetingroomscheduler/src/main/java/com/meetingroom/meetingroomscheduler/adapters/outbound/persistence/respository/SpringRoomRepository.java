package com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.respository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.meetingroom.meetingroomscheduler.domain.model.Room;

public interface SpringRoomRepository extends JpaRepository<Room, UUID> {

}
