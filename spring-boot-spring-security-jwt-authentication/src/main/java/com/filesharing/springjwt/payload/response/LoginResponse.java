package com.filesharing.springjwt.payload.response;

import com.filesharing.springjwt.dto.ArticleDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class LoginResponse {
    private Long id;
    private String username;
    private String email;

    private List<ArticleDTO> articleDTOS;
    private List<String> roles;
    private SuccessFailure status;
    private String message;
    private String sessionToken;
    private String refreshToken;

    public enum SuccessFailure {
        SUCCESS, FAILURE
    }

    public LoginResponse(Long id, String username, String email, List<String> roles,  String sessionToken, String refreshToken, SuccessFailure status, String message, List<ArticleDTO> articleDTOs) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.articleDTOS = articleDTOS;
        this.roles = roles;
        this.status = status;
        this.message = message;
        this.sessionToken = sessionToken;
        this.refreshToken = refreshToken;
    }

    public LoginResponse(SuccessFailure status, String message) {
        this.status = status;
        this.message = message;
    }

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

    public List<ArticleDTO> getArticleDTOS() {
        return articleDTOS;
    }

    public void setArticleDTOS(List<ArticleDTO> articleDTOS) {
        this.articleDTOS = articleDTOS;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public SuccessFailure getStatus() {
        return status;
    }

    public void setStatus(SuccessFailure status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSessionToken() {
        return sessionToken;
    }

    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
