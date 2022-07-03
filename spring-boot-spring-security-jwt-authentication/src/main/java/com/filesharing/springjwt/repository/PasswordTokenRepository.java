package com.filesharing.springjwt.repository;

import com.filesharing.springjwt.registration.token.ConfirmationToken;
import com.filesharing.springjwt.registration.token.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository  extends JpaRepository<PasswordResetToken, Long> {

Optional<PasswordResetToken> findByToken(String token);
}
