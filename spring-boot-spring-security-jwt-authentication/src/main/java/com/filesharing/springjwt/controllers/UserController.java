package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.PasswordDto;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.LoginRequest;
import com.filesharing.springjwt.payload.request.ResetPasswordRequest;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.registration.token.PasswordResetToken;
import com.filesharing.springjwt.repository.PasswordTokenRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.services.UserService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordTokenRepository passwordTokenRepository;

    @Autowired
    PasswordEncoder encoder;

    @PostMapping("/resetpassword")
    public ResponseEntity<RequestResponse> resetPassword( @Valid @RequestBody ResetPasswordRequest resetRequest) {
        RequestResponse requestResponse = new RequestResponse();
        List<HttpStatus> status = new ArrayList<>();
        Optional<User> user = userRepository.findByEmail(resetRequest.getEmail());
        if (user.isEmpty()) {
            status.add(HttpStatus.BAD_REQUEST);
            requestResponse.setHttpsStatus(status);
            return new ResponseEntity<>(requestResponse, HttpStatus.UNAUTHORIZED);
        }
        String token = UUID.randomUUID().toString();
        userService.createPasswordResetTokenForUser(user.get(), token);
        status.add(HttpStatus.OK);
        requestResponse.setUser(user.get());
        requestResponse.setHttpsStatus(status);
        userService.constructAndSendPasswordResetEmail( user.get(), user.get().getEmail(), token);
        return new ResponseEntity<>(requestResponse, HttpStatus.OK);
    }

    @GetMapping("/validatepassword")
    public void passwordValidation( @RequestParam("token") String token, HttpServletResponse response) throws IOException {

        boolean result = userService.validatePasswordResetToken( token);
        if (result == false) {
            response.sendRedirect("http://localhost:3000/failedpasswordresetvalidation");
        } else {
            response.sendRedirect("http://localhost:3000/updatepassword?token=" + token);
        }
    }

    @PostMapping("/savepassword")
    public RequestResponse savePassword(@Valid @RequestBody PasswordDto passwordDto) {

        boolean validatePasswordResetToken = userService.validatePasswordResetToken(passwordDto.getToken());
        RequestResponse requestResponse = new RequestResponse();
        List<HttpStatus> status = new ArrayList<>();

        if (!validatePasswordResetToken) {
            status.add(HttpStatus.NOT_ACCEPTABLE);
            return requestResponse;
        } else {
            Optional<PasswordResetToken> passwordResetToken = passwordTokenRepository.findByToken(passwordDto.getToken());
            if (passwordResetToken.isEmpty()){
                status.add(HttpStatus.INTERNAL_SERVER_ERROR);
                return requestResponse;
            }
            try {
                User user = passwordResetToken.get().getUser();
                user.setPassword(encoder.encode(passwordDto.getPassword()));
                userRepository.save(user);
                status.add(HttpStatus.OK);
                return requestResponse;
            } catch (Exception e) {
                status.add(HttpStatus.INTERNAL_SERVER_ERROR);
                return requestResponse;
            }
        }
    }
}
