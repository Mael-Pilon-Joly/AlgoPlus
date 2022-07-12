package com.filesharing.springjwt.repository;

import com.filesharing.springjwt.models.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    Optional<List<Article>> findByLanguage(String language);
    Optional<Article> findById(Long id);
}
