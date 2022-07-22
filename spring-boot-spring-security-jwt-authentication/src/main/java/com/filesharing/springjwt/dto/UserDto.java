package com.filesharing.springjwt.dto;

import lombok.Data;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private int points;

    private boolean locked;

    private boolean enabled;

    public UserDto(Long userId, String username, int points) {
        this.userId = userId;
        this.username = username;
        this.points = points;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}