package com.filesharing.springjwt.security;

import com.filesharing.springjwt.security.jwt.AuthEntryPointJwt;
import com.filesharing.springjwt.security.jwt.AuthTokenFilter;
import com.filesharing.springjwt.services.UserDetailsServiceImpl;
import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  UserDetailsServiceImpl userDetailsService;

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler;

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {

    return new AuthTokenFilter();
  }

  @Value("${filesharing.app.accessTokenCookieName}")
  private String accessTokenCookieName;

  @Value("${filesharing.app.refreshTokenCookieName}")
  private String refreshTokenCookieName;

  @Override
  public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web
            .ignoring()
            .antMatchers("api/user/**");
  }
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.cors().and().csrf().disable()
      .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
      .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
      .authorizeRequests()
      .antMatchers("/api/user/**").permitAll()
            .and()
            .authorizeRequests()
      .antMatchers("/api/auth/**").permitAll()
      .antMatchers("/api/loggedin/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.GET, "/api/article/**").permitAll()
      .antMatchers(HttpMethod.POST, "/api/article/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.PUT, "/api/article/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.DELETE, "/api/article/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.GET, "/api/comments/**").permitAll()
      .antMatchers(HttpMethod.POST, "/api/comments/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.PUT, "/api/comments/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.DELETE, "/api/comments/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.GET, "/api/event/**").permitAll()
      .antMatchers(HttpMethod.POST, "/api/event/**").hasAnyRole("ADMIN", "USER")
      .antMatchers(HttpMethod.DELETE, "/api/event/**").hasAnyRole("ADMIN", "USER")
      .antMatchers("/api/exercise/**").hasAnyRole("ADMIN", "USER")
      .antMatchers("/api/admin/**").hasRole("ADMIN")
      .antMatchers("/api/mailbox/**").permitAll()
      .anyRequest().authenticated()
            .and()
        .logout()
            .logoutSuccessUrl("/api/auth/home")
        .deleteCookies(accessTokenCookieName)
        .deleteCookies(refreshTokenCookieName);
    http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

  }

}
