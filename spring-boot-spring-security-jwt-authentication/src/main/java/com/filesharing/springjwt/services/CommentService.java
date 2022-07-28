package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.CommentDTO;
import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Comment;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.CommentRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
import com.filesharing.springjwt.repository.UserRepository;
import com.filesharing.springjwt.security.jwt.JwtUtils;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor
@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    FileDBRepository fileDBRepository;

    public CommentService(ArticleRepository articleRepository, UserRepository userRepository) {
        this.articleRepository = articleRepository;
        this.userRepository = userRepository;
    }

    public List<CommentDTO> findByArticleId(Long articleId) {

        Optional<Article> article = articleRepository.findById(articleId);
        List<Comment> comments = article.get().getComments();
        if(comments.size() == 0) {
            return new ArrayList<CommentDTO>();
        }

        List<CommentDTO> commentDTOS = new ArrayList<>();

        for (int i = 0; i<comments.size(); i++) {
            Long userId = userRepository.findUserByCommentId(comments.get(i).getId());
            Optional<User> user = userRepository.findById(userId);
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setId(comments.get(i).getId());
            commentDTO.setContent(comments.get(i).getContent());
            commentDTO.setUsername(user.get().getUsername());
            commentDTO.setUsernameId(user.get().getId());
            commentDTO.setPublished(comments.get(i).getPublished());
            commentDTO.setUser_avatar(user.get().getAvatar());
            CommentDTO clone = new CommentDTO(commentDTO);
            commentDTOS.add(clone);
        }
        return commentDTOS;
    }

    public CommentDTO createComment(Long articleId, String content, String token) {
        String username_token = jwtUtils.getUserNameFromJwtToken(token);
            Optional<Article> article = articleRepository.findById(articleId);
            Optional<User> user = userRepository.findByUsername(username_token);
            if (user.isEmpty() || article.isEmpty()) {
                return new CommentDTO();
            }

            try {
                Comment comment = new Comment();
                comment.setPublished(new Date());
                comment.setUser(user.get());
                comment.setContent(content);
                comment.setArticle(article.get());

                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setUsername(username_token);
                commentDTO.setContent(content);
                commentDTO.setPublished(new Date());
                commentDTO.setUsernameId(user.get().getId());
                commentRepository.save(comment);

                return commentDTO;

            } catch (Exception e) {
                return null;
            }
    }
}
