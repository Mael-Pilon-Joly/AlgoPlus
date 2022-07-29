package com.filesharing.springjwt.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpCookie;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;


@Component
public class CookieUtil {
    @Value("${filesharing.app.accessTokenCookieName}")
    private String accessTokenCookieName;

    @Value("${filesharing.app.refreshTokenCookieName}")
    private String refreshTokenCookieName;

    public HttpCookie createAccessTokenCookie(String token, Long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);

        return ResponseCookie.from(accessTokenCookieName, encryptedToken)
                .maxAge(duration)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();

    }

    public HttpCookie createRefreshTokenCookie(String token, Long duration) {
        String encryptedToken = SecurityCipher.encrypt(token);

        return ResponseCookie.from(refreshTokenCookieName, encryptedToken)
                .maxAge(duration)
                .maxAge(duration)
                .httpOnly(true)
                .sameSite("None")
                .secure(true)
                .path("/")
                .build();
    }

    public HttpCookie deleteAccessTokenCookie() {
        return ResponseCookie.from(accessTokenCookieName, "").maxAge(0).httpOnly(true).path("/api").build();
    }

    public HttpCookie deleteRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenCookieName, "").maxAge(0).httpOnly(true).path("/api").build();
    }

}
