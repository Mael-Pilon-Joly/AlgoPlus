package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.MessageDTO;
import com.filesharing.springjwt.registration.email.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/mailbox")
public class MailBoxController {

    @Autowired
    EmailService emailService;

    @PostMapping("/receive")
    public ResponseEntity<Object> receiveMessage(@RequestBody MessageDTO messageDTO){
        try{
            emailService.receive(messageDTO.getSource(), messageDTO.getName(), messageDTO.getMessage());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
