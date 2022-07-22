package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.ArticleDTO;
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
import java.util.*;

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

    public List<ArticleDTO> findArticlesByUser(String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> user = userRepository.findByUsername(username);

        if(user.isEmpty()){
            return null;
        }

        List<Article> articles = user.get().getArticles();
        List<ArticleDTO> articleDTOS = new ArrayList<>();

        for (int i = 0; i<articles.size() ; i++){
            ArticleDTO articleDTO = new ArticleDTO();
            articleDTO.setId(articles.get(i).getId());
            articleDTO.setUsername(username);
            articleDTO.setContent(articles.get(i).getContent());
            articleDTO.setImage(articles.get(i).getImage());
            articleDTO.setTitle(articles.get(i).getTitle());
            articleDTO.setPublished(articles.get(i).getPublished());
            articleDTO.setLastEdited(articles.get(i).getLastEdited());
            ArticleDTO clone = new ArticleDTO(articleDTO);
            articleDTOS.add(articleDTO);
        }
        return articleDTOS;
    }

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
            user.get().setPoints(user.get().getPoints()+5);
            userRepository.save(user.get());
            return article;
        } else {
            return null;
        }
    }

    public Article updateArticle(ArticleRequest request, MultipartFile image, String token, boolean keepSameImage) throws IOException {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Article>  article_opt = articleRepository.findById(request.getId());
        if (article_opt.isEmpty()) {
            return null;
        }

        if(user.isPresent() && jwtUtils.validateJwtToken(token) && Objects.equals(user.get().getId(), article_opt.get().getUser().getId())) {
            if (!keepSameImage) {
                FileDB temp = fileStorageDBService.store(image);
                FileDB article_image = fileDBRepository.getById(temp.getId());
                article_opt.get().setImage(article_image);
            }
            article_opt.get().setTitle(request.getTitle());
            article_opt.get().setLastEdited(new Date());
            article_opt.get().setLanguage(ELanguage.valueOf(request.getLanguage().toUpperCase()));
            article_opt.get().setContent(request.getContent());
            return (articleRepository.save(article_opt.get()));
        } else {
            return null;
        }
    }

    public void deleteArticle(Article article, String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        Optional<User> user = userRepository.findByUsername(username);
        Optional<Article> article_opt = articleRepository.findById(article.getId());
        if (user.isPresent() && article_opt.isPresent()) {
            if ((jwtUtils.validateJwtToken(token) && user.get().getUsername().equals(article_opt.get().getUser().getUsername())) || user.get().getUsername().equals("admin")) {
                articleRepository.delete(article);
            }
        }
    }
}
