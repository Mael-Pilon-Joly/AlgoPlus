package com.filesharing.springjwt.services;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.ELanguage;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ArticleServiceTest {

    @Mock
    JwtUtils jwtUtils =  Mockito.mock(JwtUtils.class);;

    @Mock
    UserRepository userRepository =  Mockito.mock(UserRepository.class);
    private AutoCloseable autoCloseable;
    private ArticleService articleService;

    @BeforeEach
    void setUp() {
        autoCloseable =  MockitoAnnotations.openMocks(this);
        articleService = new ArticleService(jwtUtils, userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
    @Test
    void findArticlesByUser() {
        List<Article> articles= new ArrayList<>();
        User user1 = new User();
        byte[] data = new byte[3];
        FileDB image = new FileDB(1L, "name", "type", data);
        Article article1 = new Article(1L, "title", image, "content", user1, ELanguage.CSHARP);
        Article article2 = new Article(1L, "title", image, "content", user1, ELanguage.CSHARP);
        Article article3 = new Article(1L, "title", image, "content", user1, ELanguage.CSHARP);
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        user1.setArticles(articles);
        when(jwtUtils.getUserNameFromJwtToken("token")).thenReturn("user1");
        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(user1));
        boolean expected = (articleService.findArticlesByUser("token").size() == 3);
        assert(expected);
    }

    @Test
    void findArticlesByNonExistantUser() {
        when(jwtUtils.getUserNameFromJwtToken("token")).thenReturn(null);
        when(userRepository.findByUsername("user1")).thenReturn(null);
        boolean expected = (articleService.findArticlesByUser("token") == null);
        assert(expected);
    }
}