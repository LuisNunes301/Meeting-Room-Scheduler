package com.MeetingRoomScheduler.entities.room;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Data
@Setter
@Getter
@NoArgsConstructor
@Table(name = "rooms")
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(nullable = false)
    private Integer capacity; // Alterado para Integer

    @Column(nullable = false)
    private Boolean available; // Nome mais semântico

    @Column(length = 500)
    private String description;

    @Column(length = 100)
    private String equipment; // Ex: "Projetor, Quadro Branco"

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    private Double price;

    public Room(String name, String location, Integer capacity, Boolean available, Double price) {
        this.name = name;
        this.location = location;
        this.capacity = capacity;
        this.available = available;
        this.price = price;
    }

}
