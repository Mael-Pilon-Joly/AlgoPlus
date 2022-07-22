package com.filesharing.springjwt.dto;

import com.filesharing.springjwt.models.FileDB;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long userId;
    private String username;
    private int points;

    private boolean locked;

    private boolean enabled;

    private FileDB avatar;

    private FileDB cv;

    private List<ArticleDTO> articleDTOList;


    public UserDto(Long userId, String username, int points) {
        this.userId = userId;
        this.username = username;
        this.points = points;
    }

    public UserDto(Long userId, String username, int points, FileDB avatar, FileDB cv) {
        this.userId = userId;
        this.username = username;
        this.points = points;
        this.avatar = avatar;
        this.cv = cv;
    }

    public UserDto(UserDto clone) {
        this.userId = clone.userId;
        this.username =  clone.username;
        this.points =  clone.points;
        this.locked =  clone.locked;
        this.enabled =  clone.enabled;
        this.avatar =  clone.avatar;
        this.cv =  clone.cv;
        this.articleDTOList =  clone.articleDTOList;
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

    public FileDB getAvatar() {
        return avatar;
    }

    public void setAvatar(FileDB avatar) {
        this.avatar = avatar;
    }

    public FileDB getCv() {
        return cv;
    }

    public void setCv(FileDB cv) {
        this.cv = cv;
    }

    public List<ArticleDTO> getArticleDTOList() {
        return articleDTOList;
    }

    public void setArticleDTOList(List<ArticleDTO> articleDTOList) {
        this.articleDTOList = articleDTOList;
    }
}