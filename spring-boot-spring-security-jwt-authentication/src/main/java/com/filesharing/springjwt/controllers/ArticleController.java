package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.ArticleRequest;
import com.filesharing.springjwt.payload.response.ArticleResponse;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.services.ArticleService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200", maxAge = 3600)
@RestController
@RequestMapping("/api/article")
public class ArticleController {
    @Autowired
    ArticleRepository articleRepostitory;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleService articleService;

    @GetMapping("/articles")
    public ResponseEntity<ArticleResponse> getAllArticles() {
        try {
            List<Article> articles = articleRepostitory.findAll();
            return new ResponseEntity<>(new ArticleResponse(articles), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArticleResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/articles/{language}")
    public ResponseEntity<ArticleResponse> getAllArticlesByLanguage(@RequestParam String language) {
        try {
            Optional<List<Article>> articles = articleRepostitory.findByLanguage(language);
            return new ResponseEntity<>(new ArticleResponse(articles.get()), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArticleResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/article")
    public ResponseEntity<ArticleResponse> createArticle(@RequestHeader(name = "Authorization", required = false) String token,
                                                         @CookieValue(name = "accessToken", required = false) String accessToken,
                                                         @RequestBody ArticleRequest article, @RequestParam MultipartFile image) {
        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            List<Article> articles = articleService.createArticle(article, image, decryptedAccessToken);
            if (articles == null) {
                return new ResponseEntity<>(new ArticleResponse(), HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ArticleResponse(articles), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(new ArticleResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

    @PutMapping("/article")
    public ResponseEntity<ArticleResponse> updateArticle(@RequestHeader(name = "Authorization", required = false) String token,
                                                         @CookieValue(name = "accessToken", required = false) String accessToken,
                                                         @RequestBody ArticleRequest article, @RequestParam MultipartFile image) {
        Optional<User> user = userRepository.findByUsername(article.getUsername());
        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            List<Article> articles = articleService.updateArticle(article, image, decryptedAccessToken);
            if (articles == null) {
                return new ResponseEntity<>(new ArticleResponse(), HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ArticleResponse(articles), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(new ArticleResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/article")
    public ResponseEntity<ArticleResponse> deleteArticle(@RequestHeader(name = "Authorization", required = false) String token, @CookieValue(name = "accessToken", required = false) String accessToken,
                                                         @RequestBody ArticleRequest article) {
        Optional<User> user = userRepository.findByUsername(article.getUsername());
        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            List<Article> articles = articleService.deleteArticle(article, decryptedAccessToken);
            if (articles == null) {
                return new ResponseEntity<>(new ArticleResponse(), HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ArticleResponse(articles), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(new ArticleResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    }
