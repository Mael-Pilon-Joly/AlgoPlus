package com.filesharing.springjwt.dto;

import com.filesharing.springjwt.models.FileDB;

import java.util.Date;

public class CommentDTO {
    private Long id;
    private String title;
    private FileDB user_avatar;
    private Date published;
    private Date lastEdited;
    private String content;
    private String username;
    private Long usernameId;

    public CommentDTO() {
    }

    public CommentDTO(Long id, FileDB user_avatar, Date published, String content, String username, Long usernameId) {
        this.id = id;
        this.user_avatar = user_avatar;
        this.published = published;
        this.content = content;
        this.username = username;
        this.usernameId = usernameId;
    }

    public CommentDTO(CommentDTO commentDTO) {
        this.id = commentDTO.id;
        this.title = commentDTO.title;
        this.user_avatar = commentDTO.user_avatar;
        this.published = commentDTO.published;
        this.lastEdited = commentDTO.lastEdited;
        this.content = commentDTO.content;
        this.username = commentDTO.username;
        this.usernameId = commentDTO.usernameId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FileDB getUser_avatar() {
        return user_avatar;
    }

    public void setUser_avatar(FileDB user_avatar) {
        this.user_avatar = user_avatar;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getUsernameId() {
        return usernameId;
    }

    public void setUsernameId(Long usernameId) {
        this.usernameId = usernameId;
    }
}
