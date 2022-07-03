package com.filesharing.springjwt.services;

import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.registration.token.ConfirmationToken;
import com.filesharing.springjwt.registration.token.ConfirmationTokenService;
import com.filesharing.springjwt.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@AllArgsConstructor

public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Autowired
  UserDetailsImpl userDetails;

  private final ConfirmationTokenService confirmationTokenService;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return userDetails.build(user);
  }

  public String signUpUser(User user) {

    String token = UUID.randomUUID().toString();

    ConfirmationToken confirmationToken = new ConfirmationToken(
            token,
            LocalDateTime.now(),
            LocalDateTime.now().plusMinutes(15),
            user
    );

    confirmationTokenService.saveConfirmationToken(
            confirmationToken);

    return token;
  }

  public int enableUser(String email) {
    return userRepository.enableUser(email);
  }

}
