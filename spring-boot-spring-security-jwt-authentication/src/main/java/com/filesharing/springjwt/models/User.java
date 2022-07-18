package com.filesharing.springjwt.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.awt.image.BufferedImage;
import java.security.AuthProvider;
import java.util.*;
import java.util.stream.Collectors;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "user",
    uniqueConstraints = { 
      @UniqueConstraint(columnNames = "username"),
      @UniqueConstraint(columnNames = "email") 
    })
public class User  {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  private String username;

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  @NotBlank
  @Size(max = 120)
  private String password;

  @NotBlank
  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String token;

  private Boolean locked = false;
  private Boolean enabled = false;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(  name = "user_roles", 
        joinColumns = @JoinColumn(name = "user_id"), 
        inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @OneToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "avatar_id", referencedColumnName = "id")
  private FileDB avatar;

  @OneToOne(cascade = {CascadeType.ALL})
  @JoinColumn(name = "cv_id", referencedColumnName = "id")
  private FileDB CV;

  @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
  private List<Article> articles;

  @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
  private List<Comment> comments;

  @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
  private List<Exercise> exercises;

  private int points;

  public int getPoints() {
    return points;
  }

  public void setPoints(int points) {
    this.points = points;
  }

  public List<Article> getArticles() {
    return articles;
  }

  public void setArticles(ArrayList<Article> articles) {
    this.articles = articles;
  }

  public FileDB getCV() {
    return CV;
  }

  public void setCV(FileDB CV) {
    this.CV = CV;
  }



  public FileDB getAvatar() {
    return avatar;
  }

  public void setAvatar(FileDB avatar) {
    this.avatar = avatar;
  }



  public User() {
    this.points = 0;
  }

  public User(String username, String email, String password) {
    this.username = username;
    this.email = email;
    this.password = password;
    this.points = 0;
  }


  public User(Long id, String username, String email, String password,
              Set<Role> roles) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.roles = roles;
    this.points = 0;
  }


  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Boolean isLocked() {
    return locked;
  }

  public void setLocked(Boolean locked) {
    this.locked = locked;
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public void setArticles(List<Article> articles) {
    this.articles = articles;
  }

  public List<Comment> getComments() {
    return comments;
  }

  public void setComments(List<Comment> comments) {
    this.comments = comments;
  }

  public List<Exercise> getExercises() {
    return exercises;
  }

  public void setExercises(List<Exercise> exercises) {
    this.exercises = exercises;
  }
}
