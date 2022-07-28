package com.filesharing.springjwt.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.filesharing.springjwt.dto.TokenProvider;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.utils.CookieUtil;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class AdminService {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenProvider tokenProvider;

    @Autowired
    private CookieUtil cookieUtil;

    public ResponseEntity<RequestResponse> validateAdminRole(String accessToken) {
        Boolean tokenValid = tokenProvider.validateJwtToken(accessToken);
        if (!tokenValid) {
            return new ResponseEntity<>(new RequestResponse(), HttpStatus.BAD_REQUEST);
        }

        String currentUserUsername = tokenProvider.getUserNameFromJwtToken(accessToken);
        RequestResponse requestResponse = new RequestResponse();
        List<HttpStatus> httpStatus = new ArrayList<>();
        try {
            Optional<User> user = userRepository.findByUsername(currentUserUsername);
            if (user.isPresent()) {
                requestResponse.setUsername(user.get().getUsername());
                httpStatus.add(HttpStatus.OK);
                requestResponse.setHttpsStatus(httpStatus);
                return new ResponseEntity<>(requestResponse, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


}
