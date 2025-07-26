package com.MeetingRoomScheduler.service.impl;

import com.MeetingRoomScheduler.domain.room.Room;
import com.MeetingRoomScheduler.repository.RoomRepository;
import com.MeetingRoomScheduler.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;

    @Override
    public Room createRoom(Room room) {
        return roomRepository.save(room);
    }

    @Override
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public List<Room> getAvailableRooms() {
        return roomRepository.findByAvailableTrue();
    }

    @Override
    public List<Room> getRoomsByCapacity(Integer minCapacity) {
        return roomRepository.findByCapacityGreaterThanEqual(minCapacity);
    }

    @Override
    public List<Room> getRoomsByLocation(String location) {
        return roomRepository.findByLocationContainingIgnoreCase(location);
    }

    @Override
    public Room updateRoom(Room room) {
        if (room.getId() == null || !roomRepository.existsById(room.getId())) {
            return null;
        }
        return roomRepository.save(room);
    }

    @Override
    public void deleteRoom(Long id) {
        roomRepository.deleteById(id);
    }

    @Override
    public boolean roomExistsByName(String name) {
        return roomRepository.existsByName(name);
    }
}