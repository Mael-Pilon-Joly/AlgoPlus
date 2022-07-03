package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.payload.response.LoginResponse;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.services.UserService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/loggedin")
public class LoggedInController {

    @Autowired
    UserService userService;


    @GetMapping("/profil")
    public ResponseEntity<RequestResponse> getUserProfil(@CookieValue(name = "accessToken", required=false) String accessToken) {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        return userService.getUserProfil(decryptedAccessToken);
    }

    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = "accessToken") String accessToken,
                                                      @CookieValue(name = "refreshToken") String refreshToken) {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return userService.refresh(decryptedAccessToken, decryptedRefreshToken);
    }
}
