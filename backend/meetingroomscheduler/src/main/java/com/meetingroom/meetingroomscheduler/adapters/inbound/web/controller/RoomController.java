package com.meetingroom.meetingroomscheduler.adapters.inbound.web.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.meetingroom.meetingroomscheduler.domain.model.Room;
import com.meetingroom.meetingroomscheduler.ports.in.RoomUseCase;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomUseCase roomUseCase;

    public RoomController(RoomUseCase roomUseCase) {
        this.roomUseCase = roomUseCase;
    }

    @PostMapping
    public ResponseEntity<Room> createRoom(@RequestBody Room room) {
        return ResponseEntity.ok(roomUseCase.createRoom(room));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Room> getRoom(@PathVariable UUID id) {
        Room room = roomUseCase.getRoomById(id);
        return ResponseEntity.ok(room);
    }

    @GetMapping
    public ResponseEntity<List<Room>> getAllRooms() {
        return ResponseEntity.ok(roomUseCase.getAllRooms());
    }
}