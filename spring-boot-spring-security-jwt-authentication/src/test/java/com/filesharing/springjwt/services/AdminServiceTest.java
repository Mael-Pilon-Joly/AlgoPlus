package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.Token;
import com.filesharing.springjwt.dto.TokenProvider;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.utils.CookieUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AdminServiceTest {


    @Mock
    AuthenticationManager authenticationManager= Mockito.mock(AuthenticationManager.class);

    @Mock
    private UserRepository userRepository= Mockito.mock(UserRepository.class);

    @Mock
    private TokenProvider tokenProvider= Mockito.mock(TokenProvider.class);

    @Mock
    private CookieUtil cookieUtil = Mockito.mock(CookieUtil.class);

private AutoCloseable autoCloseable;
private AdminService adminService;


    @BeforeEach
    void setUp() {
        autoCloseable =  MockitoAnnotations.openMocks(this);
        adminService = new AdminService(authenticationManager, userRepository, tokenProvider, cookieUtil );
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }

    @Test
    @WithUserDetails("admin")
    void validateUsernameFromToken(){
      User user = new User ("user1", "user1@hotmail.com", "password");
      when(tokenProvider.validateJwtToken("token")).thenReturn(true);
      when(tokenProvider.getUserNameFromJwtToken("token")).thenReturn("user1");
      when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
      ResponseEntity<RequestResponse> responseEntity = adminService.validateAdminRole("token");
      boolean expected = (responseEntity.getStatusCode() == HttpStatus.OK);
      assert(expected);
    }

    @Test
    void invalidateUsernameFromWrongToken(){
        User user = new User ("user1", "user1@hotmail.com", "password");

        when(tokenProvider.validateJwtToken("token")).thenReturn(false);
        when(tokenProvider.getUserNameFromJwtToken("token")).thenReturn("user1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user));
        ResponseEntity<RequestResponse> responseEntity = adminService.validateAdminRole("token");
        boolean expected = (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST);
        assert(expected);
    }

}