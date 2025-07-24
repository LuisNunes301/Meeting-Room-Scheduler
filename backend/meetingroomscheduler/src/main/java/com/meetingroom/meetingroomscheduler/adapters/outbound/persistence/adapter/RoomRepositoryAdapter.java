package com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.adapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.meetingroom.meetingroomscheduler.adapters.outbound.persistence.respository.SpringRoomRepository;
import com.meetingroom.meetingroomscheduler.domain.model.Room;
import com.meetingroom.meetingroomscheduler.ports.out.RoomRepositoryPort;

@Component
public class RoomRepositoryAdapter implements RoomRepositoryPort {

    private final SpringRoomRepository roomRepository;

    public RoomRepositoryAdapter(SpringRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room save(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> findById(UUID roomId) {
        return roomRepository.findById(roomId);
    }

    @Override
    public List<Room> findAll() {
        return roomRepository.findAll();
    }
}