package com.filesharing.springjwt.repositorytests;

import com.filesharing.springjwt.models.*;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.CommentRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
import com.filesharing.springjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class UserRepositoryTest {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    FileDBRepository fileDBRepository;

    @Autowired
    CommentRepository commentRepository;

    @Test
    void findUserByCommentId(){
    User user1 = createUser("user@hotmail.com");
    User user2 = createUser("user2@hotmail.com");
    User user3 = createUser("user3@hotmail.com");
    FileDB image = createImage();
    Article article1 = new Article(1L, "article", image, "article", user1, ELanguage.ANGULAR);
    articleRepository.save(article1);
    Article article2 = new Article(2L, "article", image, "article", user1, ELanguage.ANGULAR);
    articleRepository.save(article2);
    Comment comment1 = new Comment(1L, user1, article1);
    Comment comment2= new Comment(2L, user2, article1);
    Comment comment3= new Comment(3L, user3, article2);
    commentRepository.save(comment1);
    commentRepository.save(comment2);
    commentRepository.save(comment3);
    Long user_id = userRepository.findUserByCommentId(2L);
    Optional<User> user_found = userRepository.findById(user_id);
    boolean expected = (user_found.get().getId() == user2.getId());
    assert(expected);
    }

    public User createUser(String email){
        Optional<User> user_opt = userRepository.findByEmail("user@hotmail.com");
        if (user_opt.isEmpty()){
            User userMock = new User(null, email, "password");
            return userRepository.save(userMock);
        } else {
            return user_opt.get();
        }
    }

    public FileDB createImage() {
        FileDB image = new FileDB();
        return fileDBRepository.save(image);
    }
}
