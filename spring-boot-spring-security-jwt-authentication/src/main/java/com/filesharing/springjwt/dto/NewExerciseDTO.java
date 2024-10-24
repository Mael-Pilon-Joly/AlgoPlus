package com.filesharing.springjwt.dto;

import com.filesharing.springjwt.models.FileDB;

import java.util.Date;
import java.util.Map;

public class NewExerciseDTO {
    private Long   id;
    private String title;
    private FileDB image;
    private String creator_username;
    private String explanation;
    private Date published;

    private Map<String,String> solutions;

    public NewExerciseDTO() {
    }

    public NewExerciseDTO(Long id, String title, String creator_username, Date published) {
        this.id = id;
        this.title = title;
        this.creator_username = creator_username;
        this.published = published;
    }

    public NewExerciseDTO(NewExerciseDTO clone, boolean miniClone) {
        this.id = clone.id;
        this.title = clone.title;
        this.creator_username = clone.creator_username;
        this.published = clone.published;
    }

    public NewExerciseDTO(Long id, String title, FileDB image, String creator_username, String explanation, Date published) {
        this.id = id;
        this.title = title;
        this.image = image;
        this.creator_username = creator_username;
        this.explanation = explanation;
        this.published = published;
    }

    public NewExerciseDTO(NewExerciseDTO clone) {
        this.id = clone.id;
        this.title = clone.title;
        this.image = clone.image;
        this.creator_username = clone.creator_username;
        this.explanation = clone.explanation;
        this.published = clone.published;
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

    public String getCreator_username() {
        return creator_username;
    }

    public void setCreator_username(String creator_username) {
        this.creator_username = creator_username;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public Date getPublished() {
        return published;
    }

    public void setPublished(Date published) {
        this.published = published;
    }

    public Map<String, String> getSolutions() {
        return solutions;
    }

    public void setSolutions(Map<String, String> solutions) {
        this.solutions = solutions;
    }
}
