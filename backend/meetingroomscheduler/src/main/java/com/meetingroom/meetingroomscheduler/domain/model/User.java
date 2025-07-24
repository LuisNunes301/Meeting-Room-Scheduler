package com.meetingroom.meetingroomscheduler.domain.model;

import java.util.UUID;

import com.meetingroom.meetingroomscheduler.domain.enums.Role;

public class User {
    private UUID id;
    private String name;
    private String email;
    private String password;
    private Role role;

    public User(UUID id, String name, String email, String password, Role role) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User() {
    }

    public User(UUID id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }
}