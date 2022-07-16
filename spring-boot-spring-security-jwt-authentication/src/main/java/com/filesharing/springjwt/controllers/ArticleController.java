package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.ArticleDTO;
import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.ELanguage;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.ArticleRequest;
import com.filesharing.springjwt.payload.response.ArticleResponse;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
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

    @Autowired
    FileDBRepository fileDBRepository;

    @GetMapping("/articles")
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        try {
            List<Article> articles = articleRepostitory.findAll();
            List<Long> ids = articleRepostitory.findUsersId();

            for (int i=0; i<articles.size() && i<7 ; i++) {
              UserRepository.UserProjection up = userRepository.findByNativeQuery(ids.get(i));
              User user = new User();
              user.setId(up.getId());
              user.setUsername(up.getUsername());
              user.setEmail(up.getEmail());
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

    @GetMapping("/articlesbylanguage")
    public ResponseEntity<List<ArticleDTO>> getAllArticlesByLanguage(@RequestParam String language) {
        try {
            ELanguage elanguage = ELanguage.valueOf(language.toUpperCase());
            int elanguage_value = elanguage.getValue();
            Optional<List<Article>> articles = articleRepostitory.findByLanguage(elanguage_value);
            List<Long> idUsers = articleRepostitory.findUsersIdByLanguage(elanguage_value);

            for (int i=0; i<articles.get().size() && i<7 ; i++) {
                UserRepository.UserProjection up = userRepository.findByNativeQuery(idUsers.get(i));
                User user = new User();
                user.setId(up.getId());
                user.setUsername(up.getUsername());
                user.setEmail(up.getEmail());
                user.setPoints(up.getPoints());
                articles.get().get(i).setUser(user);
            }

            List<ArticleDTO> articleDTOS = new ArrayList<>();
            ArticleDTO articleDTO = new ArticleDTO();

            for (int i=0; i<articles.get().size() && i<7 ; i++) {
                articleDTO.setId(articles.get().get(i).getId());
                articleDTO.setUsernameId(articles.get().get(i).getUser().getId());
                articleDTO.setUsername(articles.get().get(i).getUser().getUsername());
                articleDTO.setPublished(articles.get().get(i).getPublished());
                articleDTO.setLastEdited(articles.get().get(i).getLastEdited());
                articleDTO.setTitle(articles.get().get(i).getTitle());
                articleDTO.setContent(articles.get().get(i).getContent());
                articleDTO.setImage(articles.get().get(i).getImage());
                articleDTO.setLanguage(articles.get().get(i).getLanguage());
                ArticleDTO copyArticleDTO = new ArticleDTO(articleDTO);
                articleDTOS.add(copyArticleDTO);
            }


            return new ResponseEntity<>(articleDTOS, HttpStatus.OK);


        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/articlesbyuser")
    public ResponseEntity<List<ArticleDTO>> findArticlesByUser(@RequestHeader(name = "Authorization", required = false) String token,
                                                               @CookieValue(name = "accessToken", required = false) String accessToken) {
        try {
            if (token != null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            return new ResponseEntity<>(articleService.findArticlesByUser(decryptedAccessToken), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ArrayList<ArticleDTO>(), HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<ArticleDTO> updateArticle(@RequestHeader(name = "Authorization", required = false) String token,
                                                         @CookieValue(name = "accessToken", required = false) String accessToken,
                                                         @RequestParam("id") String id, @RequestParam("title") String title, @RequestParam("content") String content,
                                                         @RequestParam("language") String language, @RequestParam("image") MultipartFile image, @RequestParam("keepsameimage") String keepSameImage) {
        boolean keepSameImageBool = false;
        if (keepSameImage.equals("true")) {
            keepSameImageBool = true;
        }
        Long id_long = Long.parseLong(id);
        ArticleRequest article = new ArticleRequest( id_long, null, image, language, content, title);

        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            Article ret = articleService.updateArticle(article, image, decryptedAccessToken, keepSameImageBool);
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
