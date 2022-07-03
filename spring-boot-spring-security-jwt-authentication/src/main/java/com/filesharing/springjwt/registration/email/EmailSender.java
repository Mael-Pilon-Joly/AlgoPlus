package com.filesharing.springjwt.registration.email;

import org.springframework.stereotype.Component;

@Component
public interface EmailSender {
    public void send(String dest, String email, String subject);
}
