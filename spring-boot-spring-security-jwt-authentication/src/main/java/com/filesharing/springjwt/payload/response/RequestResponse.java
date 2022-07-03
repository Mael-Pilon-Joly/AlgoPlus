package com.filesharing.springjwt.payload.response;

import com.filesharing.springjwt.models.User;
import org.springframework.http.HttpStatus;

import java.util.List;

public class RequestResponse {
    User user;
    List<HttpStatus> httpsStatus;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<HttpStatus> getHttpsStatus() {
        return httpsStatus;
    }

    public void setHttpsStatus(List<HttpStatus> httpsStatus) {
        this.httpsStatus = httpsStatus;
    }
}
