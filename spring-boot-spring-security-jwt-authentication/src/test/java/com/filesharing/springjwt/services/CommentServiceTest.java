package com.filesharing.springjwt.services;

import com.filesharing.springjwt.dto.ArticleDTO;
import com.filesharing.springjwt.dto.CommentDTO;
import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Comment;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class CommentServiceTest {
    @Mock
    ArticleRepository articleRepository =  Mockito.mock(ArticleRepository.class);

    @Mock
    UserRepository userRepository =  Mockito.mock(UserRepository.class);
    private AutoCloseable autoCloseable;
    private CommentService commentService;

    @BeforeEach
    void setUp() {
        autoCloseable =  MockitoAnnotations.openMocks(this);
        commentService = new CommentService(articleRepository, userRepository);
    }

    @AfterEach
    void tearDown() throws Exception {
        autoCloseable.close();
    }
    @Test
    void findByArticleId() {
        User user = new User("user", "email", "password" );
        user.setAvatar(new FileDB());
        Article article = new Article();
        Comment comment = new Comment(1L, user, article);
        comment.setPublished(new Date());
        Comment comment2 = new Comment(2L, user, article);
        comment.setPublished(new Date());
        List<Comment> comments = new ArrayList<>();
        comments.add(comment);
        comments.add(comment2);
        article.setComments(comments);
        when(articleRepository.findById(1L)).thenReturn(Optional.of(article));
        when(userRepository.findUserByCommentId(1L)).thenReturn(1L);
        when(userRepository.findUserByCommentId(2L)).thenReturn(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        List<CommentDTO> commentDTO = commentService.findByArticleId(1L);
        boolean expected = (commentDTO.size() == 2);
        assert(expected);
    }
}