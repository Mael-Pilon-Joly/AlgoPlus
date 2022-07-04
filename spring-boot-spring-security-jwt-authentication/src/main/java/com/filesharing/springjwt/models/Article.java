package com.filesharing.springjwt.models;

import javax.persistence.*;

@Entity
@Table(name = "article")
public class Article {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @OneToOne
    @JoinColumn(name = "id")
    private FileDB image;

    public FileDB getImage() {
        return image;
    }

    public void setImage(FileDB image) {
        this.image = image;
    }

    @OneToOne
    @JoinColumn(name = "id")
    private FileDB content;

    public FileDB getContent() {
        return content;
    }

    public void setContent(FileDB content) {
        this.content = content;
    }

}