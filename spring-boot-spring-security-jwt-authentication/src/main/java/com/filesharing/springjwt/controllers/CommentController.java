package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.models.Comment;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.CommentRepository;
import com.filesharing.springjwt.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/comments/")
public class CommentController {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;
    @GetMapping("{articleId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByArticleId(@PathVariable(value = "articleId") Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<Comment> comments = commentRepository.findByArticleId(articleId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("/comments/comment")
    public ResponseEntity<Comment> createComment(@RequestParam(value = "articleId") Long articleId,
                                                 @RequestBody Comment commentRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (commentRequest.getUser().getUsername() != ((UserDetailsImpl)auth.getPrincipal()).getUsername()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Comment comment = articleRepository.findById(articleId).map(article -> {
            commentRequest.setArticle(article);
            return commentRepository.save(commentRequest);
        }).orElseThrow(RuntimeException::new);
        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }
    @PutMapping("/comments")
    public ResponseEntity<Comment> updateComment(@RequestParam("id") long id, @RequestBody Comment commentRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (commentRequest.getUser().getUsername() != ((UserDetailsImpl)auth.getPrincipal()).getUsername()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        Comment comment = commentRepository.findById(id)
                .orElseThrow(RuntimeException::new);
        comment.setContent(commentRequest.getContent());
        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.OK);
    }
    @DeleteMapping("/comments/comment")
    public ResponseEntity<HttpStatus> deleteComment(@RequestParam("id") long id, @RequestBody Comment commentRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (commentRequest.getUser().getUsername() != ((UserDetailsImpl)auth.getPrincipal()).getUsername()) {
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        }
        commentRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}