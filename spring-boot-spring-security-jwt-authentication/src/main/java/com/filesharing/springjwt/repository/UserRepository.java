package com.filesharing.springjwt.repository;

import java.util.List;
import java.util.Optional;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  @Query(value = "select * from User where id = :id", nativeQuery = true)
  Optional<User> findById(@Param("id")Long id);

  @Query(value = "SELECT id, username, email, avatar_id, cv_id, points  FROM User WHERE id = :id", nativeQuery = true)
  UserProjection findByNativeQuery(@Param("id")Long id);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  Optional<User> findByEmail(String email);

  public interface UserProjection {
    String getUsername();
    String getEmail();
    Long getId();
    FileDB getAvatar_Id();
    FileDB getCV_Id();
    List<Article> getArticles();
    int getPoints();
  }


  @Transactional
  @Modifying
  @Query("UPDATE User a " +
          "SET a.enabled = TRUE WHERE a.email = ?1")
  int enableUser(String email);
}
