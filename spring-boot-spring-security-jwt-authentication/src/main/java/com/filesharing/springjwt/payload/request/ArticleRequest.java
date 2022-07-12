package com.filesharing.springjwt.payload.request;

import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;

public class ArticleRequest {
    Long id;
    String username;
    String title;
    FileDB image;
    String language;
    String content;

    public ArticleRequest(Long id, String username, FileDB image, String language, String content, String title) {
        this.id = id;
        this.username = username;
        this.image = image;
        this.language = language;
        this.content = content;
        this.title = title;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public FileDB getImage() {
        return image;
    }

    public void setImage(FileDB image) {
        this.image = image;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
