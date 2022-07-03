package com.filesharing.springjwt.payload.response;

import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.registration.token.ConfirmationToken;
import org.springframework.http.HttpStatus;

import java.util.List;

public class ConfirmationTokenResponse {

    User user;
    List<HttpStatus> httpsStatus;
    String token;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<HttpStatus> getHttpsStatus() {
        return httpsStatus;
    }

    public void setHttpsStatus(List<HttpStatus> httpsStatus) {
        this.httpsStatus = httpsStatus;
    }
}
