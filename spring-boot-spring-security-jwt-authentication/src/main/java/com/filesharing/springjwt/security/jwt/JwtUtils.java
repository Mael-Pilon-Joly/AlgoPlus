package com.filesharing.springjwt.security.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.filesharing.springjwt.dto.Token;
import com.filesharing.springjwt.dto.TokenProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;

@Component
public class JwtUtils implements TokenProvider {
  private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

  @Value("${filesharing.app.tokenSecret}")
  private String tokenSecret;

  @Value("${filesharing.app.jwtSecret}")
  private String jwtSecret;

  @Value("${filesharing.app.jwtExpirationMsec}")
  private int jwtExpirationMsec;


  @Value("${filesharing.app.jwtRefreshExpirationMsec}")
  private Long refreshTokenExpirationMsec;

  @Override
  public Token generateJwtToken(String username) {
    Date now = new Date();
    Long duration = now.getTime() + jwtExpirationMsec;
    Date expiryDate = new Date(duration);
    String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, tokenSecret)
            .compact();
    return new Token(Token.TokenType.ACCESS, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
  }

  @Override
  public Token generateRefreshJwtToken(String username) {
    Date now = new Date();
    Long duration = now.getTime() + refreshTokenExpirationMsec;
    Date expiryDate = new Date(duration);
    String token = Jwts.builder()
            .setSubject(username)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(SignatureAlgorithm.HS512, tokenSecret)
            .compact();
    return new Token(Token.TokenType.REFRESH, token, duration, LocalDateTime.ofInstant(expiryDate.toInstant(), ZoneId.systemDefault()));
  }

  @Override
  public String getUserNameFromJwtToken(String token) {
    return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
  }

  @Override
  public boolean validateJwtToken(String authToken) {
    try {
      Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
      return true;
    } catch (SignatureException e) {
      logger.error("Invalid JWT signature: {}", e.getMessage());
    } catch (MalformedJwtException e) {
      logger.error("Invalid JWT token: {}", e.getMessage());
    } catch (ExpiredJwtException e) {
      logger.error("JWT token is expired: {}", e.getMessage());
    } catch (UnsupportedJwtException e) {
      logger.error("JWT token is unsupported: {}", e.getMessage());
    } catch (IllegalArgumentException e) {
      logger.error("JWT claims string is empty: {}", e.getMessage());
    }

    return false;
  }

  @Override
  public LocalDateTime getExpiryDateFromToken(String token){
    Claims claims = Jwts.parser().setSigningKey(tokenSecret).parseClaimsJws(token).getBody();
    return LocalDateTime.ofInstant(claims.getExpiration().toInstant(), ZoneId.systemDefault());
  }
}
