package com.filesharing.springjwt.controllers;

import java.time.LocalDateTime;
import java.util.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.filesharing.springjwt.models.ERole;
import com.filesharing.springjwt.models.Role;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.LoginRequest;
import com.filesharing.springjwt.payload.request.SignupRequest;
import com.filesharing.springjwt.payload.response.LoginResponse;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.registration.email.EmailSender;
import com.filesharing.springjwt.registration.reg.RegistrationService;
import com.filesharing.springjwt.registration.token.ConfirmationToken;
import com.filesharing.springjwt.registration.token.ConfirmationTokenRepository;
import com.filesharing.springjwt.registration.token.ConfirmationTokenService;
import com.filesharing.springjwt.repository.RoleRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import com.filesharing.springjwt.services.UserDetailsServiceImpl;
import com.filesharing.springjwt.services.UserService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")


public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  UserService userService;

  @Autowired
  JwtUtils jwtUtils;

  @Autowired
  ConfirmationTokenRepository confirmationTokenRepository;

  @Autowired
  RegistrationService registrationService;

  @Autowired
  private ConfirmationTokenService confirmationTokenService;

  @Autowired
  private EmailSender emailSender;

  @GetMapping("/home")
    public ResponseEntity<RequestResponse> home() {
      List<HttpStatus> httpsStatus = new ArrayList<>();
      httpsStatus.add(HttpStatus.OK);
      RequestResponse requestResponse = new RequestResponse();
      requestResponse.setHttpsStatus(httpsStatus);
      return new ResponseEntity<>(requestResponse, HttpStatus.OK);

  }


  @PostMapping(value = "/signin", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<LoginResponse> login( HttpServletRequest req,
                                              HttpServletResponse resp,
          @CookieValue(name = "accessToken", required = false) String accessToken,
          @CookieValue(name = "refreshToken", required = false) String refreshToken,
          @Valid @RequestBody LoginRequest loginRequest
  ) {

    eraseCookie(req, resp);
    accessToken = null;
    refreshToken = null;
    String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
    String decryptedRefreshToken = SecurityCipher.decrypt(refreshToken);
    return userService.login(loginRequest, decryptedAccessToken, decryptedRefreshToken);
  }

  @PostMapping("/signup")
  public ResponseEntity<RequestResponse> registerUser(@Valid @RequestBody SignupRequest signUpRequest, BindingResult bindingResult) {
    RequestResponse responseObj = new RequestResponse();
    List<HttpStatus> response = new ArrayList<>();


    if (bindingResult.hasErrors()) {
      for(FieldError fe: bindingResult.getFieldErrors()) {
        switch(fe.getField()) {
          case "username" : case "password":
            response.add(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
            break;
          case "email":
            response.add(HttpStatus.NOT_ACCEPTABLE);
            break;
        }
      }
      responseObj.setUser(null);
      responseObj.setHttpsStatus(response);
      return new ResponseEntity<>(responseObj, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    boolean register=true;
    boolean deleteUser= false;
    Optional<User> user_username = userRepository.findByUsername(signUpRequest.getUsername());
    Optional<User> user_email = userRepository.findByEmail(signUpRequest.getEmail());

    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(user_username.get().getToken());
      // Check if token is expired
      if (confirmationToken.isEmpty()) {
        response.add(HttpStatus.INTERNAL_SERVER_ERROR);
        register = false;
      } else {
        // Token is not expired
        Optional<User> user = userRepository.findByUsername(signUpRequest.getUsername());
        if (confirmationToken.get().getExpiresAt().compareTo(LocalDateTime.now()) > 0 || user.get().isEnabled() ) {
          register = false;
          response.add(HttpStatus.LOCKED);
        } else {
          // Token is expired
          deleteUser = true;
        }
      }
    }


    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      Optional<ConfirmationToken> confirmationToken = confirmationTokenRepository.findByToken(user_email.get().getToken());
      // Check if token is expired
      if (confirmationToken.isEmpty()) {
        response.add(HttpStatus.INTERNAL_SERVER_ERROR);
        register = false;
      } else {
        // Token is not expired
        Optional<User> user = userRepository.findByEmail(signUpRequest.getEmail());
        if (user.get().isEnabled()) {
          register = false;
          response.add(HttpStatus.CONFLICT);
        } else if (confirmationToken.get().getExpiresAt().compareTo(LocalDateTime.now()) > 0 ) {
          register = false;
          response.add(HttpStatus.LOCKED);
        } else {
          // Token is expired
          deleteUser = true;
        }
      }
    }

      // Delete user if token is expired
      if (deleteUser) {

        if (user_username.isPresent()) {
          userRepository.delete(user_username.get());
        } else {
          userRepository.delete(user_email.get());
        }
      }
      // Create new user's account
      // Create new user's account
      if (register) {
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        try {
          if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
            roles.add(userRole);
          } else {
            for (String role : strRoles) {
              switch (role) {
                case "admin":
                  Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN).get();
                  roles.add(adminRole);
                  break;
                default:
                  Role userRole = roleRepository.findByName(ERole.ROLE_USER).get();
                  roles.add(userRole);
              }
            }
          }
        } catch (Exception e) {
          response.add(HttpStatus.INTERNAL_SERVER_ERROR);
          register = false;
        }

        if (register) {
          try {
            Set<Role> setRoles = new HashSet<>();
            for (Role role : roles) {
              setRoles.add(role);
            }

            user.setRoles(setRoles);
            response.add(HttpStatus.OK);
            responseObj.setUser(user);
            responseObj.setHttpsStatus(response);
            user.setToken("tmptoken");
            userRepository.save(user);
            //Confirmation email
            String token = userDetailsService.signUpUser(user);

            user.setToken(token);
            userRepository.save(user);

            String link = "http://localhost:8080/api/auth/confirm?token=" + token + "&username=" + user.getUsername();
            emailSender.send(signUpRequest.getEmail(),
                    registrationService.buildEmail(signUpRequest.getUsername(), link), "Please confirm your email account");

            return new ResponseEntity<>(responseObj, HttpStatus.OK);
          } catch (Exception e) {
            response.add(HttpStatus.INTERNAL_SERVER_ERROR);

          }
        }
      }


    responseObj.setHttpsStatus(response);
    return new ResponseEntity<>(responseObj, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @GetMapping(path = "confirm")
  public void confirm(@RequestParam("token") String token,
                      @RequestParam("username") String username,
                      HttpServletResponse httpServletResponse) {

    if(!registrationService.confirmToken(token)) {
      httpServletResponse.setHeader("Location", "http://localhost:4200/invalidtoken");
      httpServletResponse.setStatus(302);
     }

    httpServletResponse.setHeader("Location", "http://localhost:4200/emailconfirmed");
    httpServletResponse.setStatus(302);
  }

  private void eraseCookie(HttpServletRequest req, HttpServletResponse resp) {
    Cookie[] cookies = req.getCookies();
    if (cookies != null)
      for (Cookie cookie : cookies) {
        cookie.setValue("");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
      }
  }
}
