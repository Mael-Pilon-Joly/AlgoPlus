package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.ArticleDTO;
import com.filesharing.springjwt.dto.CommentDTO;
import com.filesharing.springjwt.dto.NewExerciseDTO;
import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Comment;
import com.filesharing.springjwt.models.Exercise;
import com.filesharing.springjwt.payload.response.RequestResponse;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.ExerciseRepository;
import com.filesharing.springjwt.services.AdminService;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/admin")
public class AdminController {

  @Autowired
  AdminService adminService;

  @Autowired
  ArticleRepository articleRepository;

  @Autowired
  ExerciseRepository exerciseRepository;

  @PostMapping("/auth")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<RequestResponse> adminAccess(@RequestHeader(name = "Authorization", required = false) String token, @CookieValue(name = "accessToken", required=false) String accessToken) {
    if (token != null && token.length() > 15) {
      accessToken = token.substring(7);
    }
    String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
    return adminService.validateAdminRole(decryptedAccessToken);
  }

  @GetMapping("/article")
  public ResponseEntity<List<ArticleDTO>> getArticleByTitle(@RequestParam("title") String title) {
    try {
      Optional<List<Article>> articles = articleRepository.findByTitle(title);
      List<ArticleDTO> articlesDTOS = new ArrayList<>();
      if(articles.isEmpty()) {
        return new ResponseEntity<>(new ArrayList<ArticleDTO>(), HttpStatus.INTERNAL_SERVER_ERROR);
      }

      for (Article article: articles.get()){
        List<Comment> comments = article.getComments();
        List<CommentDTO> commentDTOS = new ArrayList<CommentDTO>();
        for (Comment comment: comments){
          CommentDTO commentDTO = new CommentDTO(comment.getId(), comment.getUser().getAvatar(), comment.getPublished(), comment.getContent(), comment.getUser().getUsername(), comment.getUser().getId());
          CommentDTO clone = new CommentDTO(commentDTO);
          commentDTOS.add(clone);
        }
        List<CommentDTO> newList = new ArrayList<CommentDTO>(commentDTOS);
        ArticleDTO articleDTO = new ArticleDTO(article.getId(), article.getTitle(), article.getImage(), article.getPublished(), article.getLastEdited(), article.getContent(), article.getUser().getUsername(), article.getUser().getId(), article.getLanguage(), newList);
        ArticleDTO cloneArticle = new ArticleDTO(articleDTO);
        articlesDTOS.add(cloneArticle);
      }
      return new ResponseEntity<>(articlesDTOS, HttpStatus.OK);
    } catch (Exception e){
      return new ResponseEntity<>(new ArrayList<ArticleDTO>(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @GetMapping("/exercise")
  public ResponseEntity<Object>  getExerciseByTitle(@RequestParam("title") String title){
    try {
      Optional<List<Exercise>> exercises = exerciseRepository.findByTitle(title);
      List<NewExerciseDTO> listDTOs = new ArrayList<>();
      if (exercises.isEmpty()) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
      }
      for (Exercise exercise : exercises.get()) {
        NewExerciseDTO newExerciseDTO = new NewExerciseDTO(exercise.getId(), exercise.getTitle(), exercise.getCreator().getUsername(), exercise.getPublished());
        NewExerciseDTO clone = new NewExerciseDTO(newExerciseDTO, true);
        listDTOs.add(clone);
      }
      return new ResponseEntity<>(listDTOs, HttpStatus.OK);
    } catch (Exception e){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  @DeleteMapping("/exercise")
  public ResponseEntity<Object> deleteExercise(@RequestParam("id") Long id){
    try{
      Optional<Exercise> exercise = exerciseRepository.findById(id);
      exerciseRepository.delete(exercise.get());
      return new ResponseEntity<>(null, HttpStatus.OK);
    } catch(Exception e){
      return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }
}
