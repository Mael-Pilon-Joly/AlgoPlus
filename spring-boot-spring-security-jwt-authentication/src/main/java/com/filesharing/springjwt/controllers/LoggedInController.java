package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.TokenProvider;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.response.ArticleResponse;
import com.filesharing.springjwt.payload.response.LoginResponse;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.AuthTokenFilter;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import com.filesharing.springjwt.services.UserService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/loggedin")
public class LoggedInController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @GetMapping("/profil")
    public ResponseEntity<RequestResponse> getUserProfil(@RequestHeader(name = "Authorization", required = false) String token, @CookieValue(name = "accessToken", required = false) String accessToken) {
        if (token!= null && token.length() > 15) {
            accessToken = token.substring(7);

        }
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        return userService.getUserProfil(decryptedAccessToken);
    }

    @GetMapping("/articles")
    public ResponseEntity<ArticleResponse> getUserArticles(@RequestHeader(name = "Authorization", required = false) String token, @CookieValue(name = "accessToken", required = false) String accessToken) {
        if (token!= null && token.length() > 15) {
            accessToken = token.substring(7);

        }
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        return userService.getUserArticles(decryptedAccessToken);
    }


    @PostMapping(value = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LoginResponse> refreshToken(@CookieValue(name = "accessToken") String accessToken,
                                                      @CookieValue(name = "refreshToken") String refreshToken) {
        String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
        String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
        return userService.refresh(decryptedAccessToken, decryptedRefreshToken);
    }

    @PutMapping("/updateprofile")
    public ResponseEntity<RequestResponse> updateProfil(@RequestParam("file") MultipartFile file, @RequestParam("username") String username,
                                        @RequestParam("typeofrequest") String typeOfRequest) throws IOException {

            RequestResponse requestResponse = new RequestResponse();
            List<HttpStatus> status = new ArrayList<>();
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                if (typeOfRequest.equals("avatar")) {
                    userService.updateAvatar(file, user.get());
                    userRepository.save(user.get());
                } else if (typeOfRequest.equals("cv")) {
                    userService.updateCV(file, user.get());
                    userRepository.save(user.get());
                } else {
                    // to do: update articles || exercises
                }
            }
            status.add(HttpStatus.OK);
            requestResponse.setHttpsStatus(status);
        return new ResponseEntity<>(requestResponse, HttpStatus.OK);

    }
    }
