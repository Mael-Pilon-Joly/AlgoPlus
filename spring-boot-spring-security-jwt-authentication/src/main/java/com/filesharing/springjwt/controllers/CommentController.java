package com.filesharing.springjwt.controllers;

import com.filesharing.springjwt.dto.CommentDTO;
import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Comment;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.CommentRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import com.filesharing.springjwt.services.CommentService;
import com.filesharing.springjwt.services.UserDetailsImpl;
import com.filesharing.springjwt.utils.SecurityCipher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://143.198.169.178:4200")
@RestController
@RequestMapping("/api/comments/")
public class CommentController {
    @Autowired
    ArticleRepository articleRepository;
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    UserRepository userRepository;

    @GetMapping("comments")
    public ResponseEntity<List<CommentDTO>> getAllCommentsByArticleId(@RequestParam(value = "articleId") Long articleId) {
        if (!articleRepository.existsById(articleId)) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        List<CommentDTO> comments = commentService.findByArticleId(articleId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @PostMapping("comment")
    public ResponseEntity<CommentDTO> createComment(@RequestHeader(name = "Authorization", required = false) String token,
                                                    @CookieValue(name = "accessToken", required = false) String accessToken,
                                                    @RequestParam(value = "articleId") Long articleId,
                                                 @RequestParam("comment") String content) {
        try {
                if (token!= null && token.length() > 15) {
                    accessToken = token.substring(7);
                }
                String decryptedAccessToken = SecurityCipher.decrypt(accessToken);
            CommentDTO commentDTO = commentService.createComment(articleId, content, decryptedAccessToken);
            return new ResponseEntity<>(commentDTO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("comment")
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

    @DeleteMapping("comment")
    public ResponseEntity<HttpStatus> deleteComment(@RequestHeader(name = "Authorization", required = false) String token,
                                                    @CookieValue(name = "accessToken", required = false) String accessToken,
                                                    @RequestParam("id") long id) {
        try {
            if (token!= null && token.length() > 15) {
                accessToken = token.substring(7);
            }
            String decryptedAccessToken = SecurityCipher.decrypt(accessToken);

            String username_token = jwtUtils.getUserNameFromJwtToken(decryptedAccessToken);
            Optional<User> user = userRepository.findByUsername(username_token);
            Optional<Comment> comment = commentRepository.findById(id);
            if (comment.isEmpty() || user.isEmpty() || (!user.get().getUsername().equals("admin")) && !comment.get().getUser().getUsername().equals(user.get().getUsername())) {
                return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
            }
            commentRepository.delete(comment.get());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}