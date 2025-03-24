package com.aquariux.crypto_trading_system.entity;

import java.time.ZonedDateTime;
import java.util.UUID;

public class User {
    private final UUID userId;
    private final String username;
    private final String password;
    private final String email;
    private final String role;
    private final int version;
    private final ZonedDateTime createdAt;
    private final ZonedDateTime updatedAt;

    public User(UUID userId, String username, String password, String email, String role, int version, ZonedDateTime createdAt, ZonedDateTime updatedAt) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.version = version;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getRole() {
        return role;
    }

    public int getVersion() {
        return version;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }
}
