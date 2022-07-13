package com.filesharing.springjwt.payload.request;

import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import org.springframework.web.multipart.MultipartFile;

public class ArticleRequest {

    Long id;
    String username;
    String title;
    MultipartFile image;
    String language;
    String content;

    public ArticleRequest( String username, MultipartFile image, String language, String content, String title) {
        this.username = username;
        this.image = image;
        this.language = language;
        this.content = content;
        this.title = title;
    }

    public ArticleRequest( Long id, String username, MultipartFile image, String language, String content, String title) {
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

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
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
