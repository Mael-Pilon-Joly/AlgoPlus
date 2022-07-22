package com.filesharing.springjwt.repository;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.ELanguage;
import com.filesharing.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query(value="select * from Article where language=:index ORDER BY published desc",  nativeQuery = true)
    Optional<List<Article>> findByLanguage(int index);

    Optional<List<Article>> findByTitle(String title);

    Optional<Article> findById(Long id);

    @Query(value="select user_id from Article ORDER BY published desc",  nativeQuery = true)
    List<Long> findUsersId();

    @Query(value="select user_id from Article where language=:index ORDER BY published desc",  nativeQuery = true)
    List<Long> findUsersIdByLanguage(int index);


}
