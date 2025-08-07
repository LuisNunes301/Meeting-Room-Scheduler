package com.MeetingRoomScheduler.controller;

import com.MeetingRoomScheduler.dto.request.RoomRequest;
import com.MeetingRoomScheduler.dto.response.RoomResponse;
import com.MeetingRoomScheduler.entities.room.Room;
import com.MeetingRoomScheduler.service.RoomService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.MeetingRoomScheduler.config.SwaggerConfig.BEARER_KEY_SECURITY_SCHEME;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {

    private final RoomService roomService;

    public RoomController(RoomService roomService) {
        this.roomService = roomService;
    }

    // Conversão de Room para RoomResponse
    private RoomResponse toRoomResponse(Room room) {
        return new RoomResponse(
                room.getId(),
                room.getName(),
                room.getLocation(),
                room.getCapacity(),
                room.getAvailable(),
                room.getDescription(),
                room.getEquipment(),
                room.getPrice(),
                room.getCreatedAt(),
                room.getUpdatedAt());
    }

    // Conversão de RoomRequest para Room (sem Builder)
    private Room toRoom(RoomRequest roomRequest) {
        Room room = new Room();
        room.setName(roomRequest.name());
        room.setLocation(roomRequest.location());
        room.setCapacity(roomRequest.capacity());
        room.setAvailable(roomRequest.available());
        room.setDescription(roomRequest.description());
        room.setPrice(roomRequest.price());
        room.setEquipment(roomRequest.equipment());
        if (roomRequest.price() != null) {
            room.setPrice(roomRequest.price());
        } else {
            room.setPrice(0.0);
        }
        return room;
    }

    // Criar nova sala
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ResponseEntity<RoomResponse> createRoom(@RequestBody RoomRequest roomRequest) {
        if (roomService.roomExistsByName(roomRequest.name())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Room room = toRoom(roomRequest);
        Room createdRoom = roomService.createRoom(room);
        return ResponseEntity.status(HttpStatus.CREATED).body(toRoomResponse(createdRoom));
    }

    // Obter sala por ID
    @GetMapping("/{id}")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ResponseEntity<RoomResponse> getRoomById(@PathVariable Long id) {
        Room room = roomService.getRoomById(id);
        return room != null
                ? ResponseEntity.ok(toRoomResponse(room))
                : ResponseEntity.notFound().build();
    }

    // Listar todas as salas
    @GetMapping
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ResponseEntity<List<RoomResponse>> getAllRooms() {
        List<RoomResponse> rooms = roomService.getAllRooms().stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    // Listar salas disponíveis
    @GetMapping("/available")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms() {
        List<RoomResponse> rooms = roomService.getAvailableRooms().stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    // Listar salas por capacidade mínima
    @GetMapping("/capacity/{minCapacity}")
    public ResponseEntity<List<RoomResponse>> getRoomsByCapacity(@PathVariable Integer minCapacity) {
        List<RoomResponse> rooms = roomService.getRoomsByCapacity(minCapacity).stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    // Buscar salas por localização
    @GetMapping("/location")
    public ResponseEntity<List<RoomResponse>> getRoomsByLocation(@RequestParam String location) {
        List<RoomResponse> rooms = roomService.getRoomsByLocation(location).stream()
                .map(this::toRoomResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(rooms);
    }

    // Atualizar sala
    @PutMapping("/{id}")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long id, @RequestBody RoomRequest roomRequest) {
        Room existingRoom = roomService.getRoomById(id);
        if (existingRoom == null) {
            return ResponseEntity.notFound().build();
        }

        Room roomToUpdate = toRoom(roomRequest);
        roomToUpdate.setId(id);
        roomToUpdate.setCreatedAt(existingRoom.getCreatedAt());

        Room updatedRoom = roomService.updateRoom(roomToUpdate);
        return ResponseEntity.ok(toRoomResponse(updatedRoom));
    }

    // Deletar sala
    @DeleteMapping("/{id}")
    @Operation(security = { @SecurityRequirement(name = BEARER_KEY_SECURITY_SCHEME) })
    public ResponseEntity<Void> deleteRoom(@PathVariable Long id) {
        if (roomService.getRoomById(id) == null) {
            return ResponseEntity.notFound().build();
        }
        roomService.deleteRoom(id);
        return ResponseEntity.noContent().build();
    }
}