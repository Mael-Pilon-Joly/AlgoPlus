package com.filesharing.springjwt.dto;

import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;


public interface TokenProvider {
    Token generateJwtToken(String username);

    Token generateRefreshJwtToken(String username);

    String getUserNameFromJwtToken(String token);

    LocalDateTime getExpiryDateFromToken(String token);

    boolean validateJwtToken(String token);
}