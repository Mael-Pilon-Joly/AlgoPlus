package com.filesharing.springjwt.services;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.ELanguage;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.payload.request.ArticleRequest;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.AuthTokenFilter;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    @Autowired
    FileStorageDBService fileStorageDBService;

    @Autowired
    FileDBRepository fileDBRepository;

    @Autowired
    UserRepository userRepository;

    public Article createArticle(ArticleRequest request, MultipartFile image, String token) throws IOException {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        if(username!=null && jwtUtils.validateJwtToken(token) && username.equals(request.getUsername())) {
            FileDB temp = fileStorageDBService.store(image);
            FileDB article_image = fileDBRepository.getById(temp.getId());
            Article article = new Article();
            Optional<User> user = userRepository.findByUsername(request.getUsername());
            article.setUser(user.get());
            article.setTitle(request.getTitle());
            article.setPublished(new Date());
            article.setImage(article_image);
            article.setContent(request.getContent());
            article.setLanguage(ELanguage.valueOf(request.getLanguage().toUpperCase()));
            articleRepository.save(article);
            return article;
        } else {
            return null;
        }
    }

    public List<Article> updateArticle(ArticleRequest request, MultipartFile image, String token) throws IOException {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        if(username!=null && jwtUtils.validateJwtToken(token) && username.equals(request.getUsername())) {
            FileDB temp = fileStorageDBService.store(image);
            FileDB article_image = fileDBRepository.getById(temp.getId());
            Optional<Article>  article_opt = articleRepository.findById(request.getId());
            if (article_opt.isEmpty()) {
                return null;
            }
            article_opt.get().setTitle(request.getTitle());
            article_opt.get().setLastEdited(new Date());
            article_opt.get().setImage(article_image);
            article_opt.get().setLanguage(ELanguage.valueOf(request.getLanguage().toUpperCase()));
            articleRepository.save(article_opt.get());
            List<Article> articles = new ArrayList<>();
            articles.add(article_opt.get());
            return articles;
        } else {
            return null;
        }
    }

    public List<Article> deleteArticle(Article article, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        if(username.equals("admin") || (username!=null && jwtUtils.validateJwtToken(token) && username.equals(article.getUser().getUsername()))) {
            articleRepository.delete(article);
            List<Article> articles = new ArrayList<>();
            articles.add(article);
            return articles;
        } else {
            return null;
        }
    }
}
