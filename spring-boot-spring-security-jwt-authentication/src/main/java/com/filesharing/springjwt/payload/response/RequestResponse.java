package com.filesharing.springjwt.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.filesharing.springjwt.dto.ArticleDTO;
import com.filesharing.springjwt.dto.ExerciseDTO;
import com.filesharing.springjwt.dto.NewExerciseDTO;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.Role;
import com.filesharing.springjwt.models.Solution;
import com.filesharing.springjwt.models.User;
import org.springframework.http.HttpStatus;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RequestResponse {
    private Long id;
    private String username;
    List<ArticleDTO> articlesDTOs;
    List<Solution> solutions;
    List<NewExerciseDTO> exercises;
    List<HttpStatus> httpsStatus;

    private String email;
    private Boolean locked;
    private Boolean enabled;
    private Set<Role> roles = new HashSet<>();
    private FileDB avatar;
    private FileDB CV;

    private int points;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getLocked() {
        return locked;
    }

    public void setLocked(Boolean locked) {
        this.locked = locked;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public FileDB getAvatar() {
        return avatar;
    }

    public void setAvatar(FileDB avatar) {
        this.avatar = avatar;
    }

    public FileDB getCV() {
        return CV;
    }

    public void setCV(FileDB CV) {
        this.CV = CV;
    }

    public List<ArticleDTO> getArticlesDTOs() {
        return articlesDTOs;
    }

    public void setArticlesDTOs(List<ArticleDTO> articlesDTOs) {
        this.articlesDTOs = articlesDTOs;
    }

    public List<Solution> getSolutions() {
        return solutions;
    }

    public void setSolutions(List<Solution> solutions) {
        this.solutions = solutions;
    }

    public List<NewExerciseDTO> getExercises() {
        return exercises;
    }

    public void setExercises(List<NewExerciseDTO> exercices) {
        this.exercises = exercices;
    }

    public List<HttpStatus> getHttpsStatus() {
        return httpsStatus;
    }

    public void setHttpsStatus(List<HttpStatus> httpsStatus) {
        this.httpsStatus = httpsStatus;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
