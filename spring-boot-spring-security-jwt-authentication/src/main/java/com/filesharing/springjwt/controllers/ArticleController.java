package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.ArticleDTO;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

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
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        try {
            List<Article> articles = articleRepostitory.findAll();
            List<Long> ids = articleRepostitory.findUsersId();
            Collections.reverse(articles);
            Collections.reverse(ids);

            for (int i=0; i<articles.size() && i<7 ; i++) {
              UserRepository.UserProjection up = userRepository.findByNativeQuery(ids.get(i));
              User user = new User();
              user.setId(up.getId());
              user.setUsername(up.getUsername());
              user.setEmail(up.getEmail());
              user.setAvatar(up.getAvatar_Id());
              user.setCV(up.getCV_Id());
              user.setPoints(up.getPoints());
              articles.get(i).setUser(user);
            }

            List<ArticleDTO> articleDTOS = new ArrayList<>();
            ArticleDTO articleDTO = new ArticleDTO();

            for (int i=0; i<articles.size() && i<7 ; i++) {
               articleDTO.setId(articles.get(i).getId());
               articleDTO.setUsernameId(articles.get(i).getUser().getId());
               articleDTO.setUsername(articles.get(i).getUser().getUsername());
               articleDTO.setPublished(articles.get(i).getPublished());
               articleDTO.setLastEdited(articles.get(i).getLastEdited());
               articleDTO.setTitle(articles.get(i).getTitle());
               articleDTO.setContent(articles.get(i).getContent());
               articleDTO.setImage(articles.get(i).getImage());
               articleDTO.setLanguage(articles.get(i).getLanguage());
               ArticleDTO copyArticleDTO = new ArticleDTO(articleDTO);
               articleDTOS.add(copyArticleDTO);
            }
            return new ResponseEntity<>(articleDTOS, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<ArticleDTO> createArticle(@RequestHeader(name = "Authorization", required = false) String token,
                                                    @CookieValue(name = "accessToken", required = false) String accessToken,
                                                    @RequestParam("username") String username, @RequestParam("title") String title, @RequestParam("content") String content,
                                                    @RequestParam("language") String language, @RequestBody MultipartFile image) {

        ArticleRequest article = new ArticleRequest( username, image, language, content, title);

        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
             Article ret = articleService.createArticle(article, image, decryptedAccessToken);
            if (ret == null) {
                return new ResponseEntity<>(new ArticleDTO(), HttpStatus.UNAUTHORIZED);
            }

            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(ret.getId());
            articleDTO.setUsernameId(ret.getUser().getId());
            articleDTO.setUsername(ret.getUser().getUsername());
            articleDTO.setImage(ret.getImage());
            articleDTO.setTitle(ret.getTitle());
            articleDTO.setLanguage(ret.getLanguage());
            articleDTO.setContent(ret.getContent());
            articleDTO.setPublished(ret.getPublished());

            return new ResponseEntity<>(articleDTO, HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(new ArticleDTO(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        }

    @PutMapping("/article")
    public ResponseEntity<ArticleResponse> updateArticle(@RequestHeader(name = "Authorization", required = false) String token,
                                                         @CookieValue(name = "accessToken", required = false) String accessToken,
                                                         @RequestParam Long id, @RequestParam String username,@RequestParam String title, @RequestParam String content,
                                                         @RequestParam String language, @RequestParam MultipartFile image) {
        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            ArticleRequest article = new ArticleRequest( username, image, language, content, title);
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
    public ResponseEntity<ArticleResponse> deleteArticle(@RequestHeader(name = "Authorization", required = false) String token,
                                                         @CookieValue(name = "accessToken", required = false) String accessToken,
                                                         @RequestParam Long id) {
        Optional<Article> article = articleRepostitory.findById(id);
        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            List<Article> articles = articleService.deleteArticle(article.get(), decryptedAccessToken);
            if (articles == null) {
                return new ResponseEntity<>(new ArticleResponse(), HttpStatus.UNAUTHORIZED);
            }
            return new ResponseEntity<>(new ArticleResponse(articles), HttpStatus.OK);
        } catch(Exception e) {
            return new ResponseEntity<>(new ArticleResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    }
