package com.filesharing.springjwt.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.filesharing.springjwt.dto.TokenProvider;
import com.filesharing.springjwt.services.UserDetailsServiceImpl;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


public class AuthTokenFilter extends OncePerRequestFilter {

  private final RequestMatcher ignoredPaths = new AntPathRequestMatcher("/logout");

  @Autowired
  private JwtUtils jwtUtils;

  @Autowired
  private UserDetailsServiceImpl userDetailsService;

  @Value("${filesharing.app.accessTokenCookieName}")
  private String accessTokenCookieName;

  @Value("${filesharing.app.refreshTokenCookieName}")
  private String refreshTokenCookieName;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private UserDetailsServiceImpl userDetailsServiceImpl;

  private static final Logger logger = LoggerFactory.getLogger(AuthTokenFilter.class);

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    List<String> skipFilterUrls = Arrays.asList("/api/user/**", "/*.html", "/favicon.ico", "/**/*.html", "/**/*.css", "/**/*.js");

    return skipFilterUrls.stream().anyMatch(url -> new AntPathRequestMatcher(url).matches(request));
  }



  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    try {
      if (this.ignoredPaths.matches(request)) {
        filterChain.doFilter(request, response);
        return;
      }

      String jwt = getJwtToken(request, true);
      if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
        String username = jwtUtils.getUserNameFromJwtToken(jwt);

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    } catch (Exception e) {
      logger.error("Cannot set user authentication: {}", e);
    }
    try {
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      System.out.println(e.getStackTrace());
    }
  }

  private String getJwtFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      String accessToken = bearerToken.substring(7);
      if (accessToken == null) return null;
      String decryptedToken = SecurityCipher.decrypt(accessToken);
      return decryptedToken;

    }
    return null;
  }

  private String getJwtFromCookie(HttpServletRequest request) {
    Cookie[] cookies = request.getCookies();
    for (Cookie cookie : cookies) {
      if (accessTokenCookieName.equals(cookie.getName())) {
        String accessToken = cookie.getValue();
        if (accessToken == null) return null;
        return SecurityCipher.decrypt(accessToken);
      }
    }
    return null;
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

  private String getJwtToken(HttpServletRequest request, boolean fromCookie) {
    String token = getJwtFromRequest(request);
    if (token != null) {
      return token;
    }
    return getJwtFromCookie(request);
  }
}
