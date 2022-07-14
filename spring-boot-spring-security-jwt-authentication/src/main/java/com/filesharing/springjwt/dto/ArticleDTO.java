package com.filesharing.springjwt.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.filesharing.springjwt.models.ELanguage;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;

public class ArticleDTO {
    private Long id;

    private String title;

    private FileDB image;

    private Date published;

    private Date lastEdited;

    private String content;

    private String username;

    private Long usernameId;

    private ELanguage language;

    public ArticleDTO(){};

    public ArticleDTO(ArticleDTO articleDTO){
        this.id = articleDTO.id;
        this.usernameId = articleDTO.usernameId;
        this.username = articleDTO.username;
        this.title = articleDTO.title;
        this.image = articleDTO.image;
        this.content = articleDTO.content;
        this.language = articleDTO.language;
        this.published = articleDTO.published;
        this.lastEdited = articleDTO.lastEdited;
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

    public FileDB getImage() {
        return image;
    }

    public void setImage(FileDB image) {
        this.image = image;
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

    public ELanguage getLanguage() {
        return language;
    }

    public void setLanguage(ELanguage language) {
        this.language = language;
    }

}
