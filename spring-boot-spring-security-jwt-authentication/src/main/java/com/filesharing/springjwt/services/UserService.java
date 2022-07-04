package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.UserDto;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.LoginRequest;
import com.filesharing.springjwt.payload.response.LoginResponse;
import com.filesharing.springjwt.payload.response.RequestResponse;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Optional;


public interface UserService {
    ResponseEntity<LoginResponse> login(LoginRequest loginRequest, String accessToken, String refreshToken);

    ResponseEntity<LoginResponse> refresh(String accessToken, String refreshToken);

    ResponseEntity<RequestResponse> getUserProfil(String accessToken);

    public void createPasswordResetTokenForUser(User user, String token);

    ResponseEntity<RequestResponse> constructAndSendPasswordResetEmail (User user, String token, String dest);

    boolean validatePasswordResetToken ( String token);

    public User updateAvatar(MultipartFile file, User user) throws IOException;

    public User updateCV(MultipartFile file, User user) throws IOException;
}




