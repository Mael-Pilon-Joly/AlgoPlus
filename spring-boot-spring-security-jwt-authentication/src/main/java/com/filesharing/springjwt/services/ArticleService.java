package com.filesharing.springjwt.services;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.ELanguage;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.security.jwt.AuthTokenFilter;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ArticleService {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    JwtUtils jwtUtils;


    public List<Article> createArticle(Article article, FileDB image, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        if(username!=null && jwtUtils.validateJwtToken(token) && username==article.getUser().getUsername()) {
            article.setPublished(new Date());
            article.setImage(image);
            article.setLanguage(article.setLanguage(ELanguage.valueOf(article.getLanguage().toString().toUpperCase())));
            articleRepository.save(article);
            List<Article> articles = new ArrayList<>();
            articles.add(article);
            return articles;
        } else {
            return null;
        }
    }

    public List<Article> updateArticle(Article article, FileDB image, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        if(username!=null && jwtUtils.validateJwtToken(token) && username==article.getUser().getUsername()) {
            article.setLastEdited(new Date());
            article.setImage(image);
            articleRepository.save(article);
            List<Article> articles = new ArrayList<>();
            articles.add(article);
            return articles;
        } else {
            return null;
        }
    }

    public List<Article> deleteArticle(Article article, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        if(username!=null && jwtUtils.validateJwtToken(token) && username==article.getUser().getUsername()) {
            articleRepository.delete(article);
            List<Article> articles = new ArrayList<>();
            articles.add(article);
            return articles;
        } else {
            return null;
        }
    }
}
