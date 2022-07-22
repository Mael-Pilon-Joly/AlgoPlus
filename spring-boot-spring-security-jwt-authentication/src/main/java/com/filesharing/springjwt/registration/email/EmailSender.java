package com.filesharing.springjwt.registration.email;

import org.springframework.stereotype.Component;

@Component
public interface EmailSender {
    public void send(String dest, String email, String subject);

    public void receive(String source, String name, String subject);

    public void messageBetweenUser(String username, String source, String dest, String subject);
}
