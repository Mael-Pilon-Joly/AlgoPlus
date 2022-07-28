package com.filesharing.springjwt.repositorytests;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.ELanguage;
import com.filesharing.springjwt.models.FileDB;
import com.filesharing.springjwt.models.User;
import com.filesharing.springjwt.repository.ArticleRepository;
import com.filesharing.springjwt.repository.FileDBRepository;
import com.filesharing.springjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ArticleRepositoryTest {
 @Autowired
 ArticleRepository articleRepository;

 @Autowired
 UserRepository userRepository;

 @Autowired
 FileDBRepository fileDBRepository;

    @Test
    void findByLanguageTest() {
        User user = createUser("user1@hotmail.com");
        FileDB image = createImage();
        Article article = new Article(null, "Java", image, "Java", user, ELanguage.JAVA);
        Article article2 = new Article(null, "Python", image, "Python", user, ELanguage.PYTHON);
        Article article3 = new Article(null, "Java", image, "Java", user, ELanguage.JAVA);
        articleRepository.save(article);
        articleRepository.save(article2);
        articleRepository.save(article3);
        Optional<List<Article>> articles = articleRepository.findByLanguage(2);
        boolean expected = (articles.get().size() == 2);
        assert(expected);
    }

    @Test
    void findUsersIdTest() {
        User user1 = createUser("user1@hotmail.com");
        User user2 = createUser("user2@hotmail.com");
        FileDB image = createImage();
        Article article1 = new Article(1L, "java article", image, "java article", user1, ELanguage.JAVA);
        articleRepository.save(article1);
        Article article2 = new Article(2L, "python article", image, "python article", user2, ELanguage.PYTHON);
        articleRepository.save(article2);
        List<Long> ids = articleRepository.findUsersId();
        boolean expected = (ids.size() == 2);
        assert(expected);
        articleRepository.deleteAll();
    }

    @Test
    void findUsersIdByLanguage() {
        User user1 = createUser("user1@hotmail.com");
        User user2 = createUser("user2@hotmail.com");
        FileDB image = createImage();
        Article article1 = new Article(1L, "java article", image, "java article", user1, ELanguage.JAVA);
        articleRepository.save(article1);
        Article article2 = new Article(2L, "python article", image, "python article", user2, ELanguage.PYTHON);
        articleRepository.save(article2);
        List<Long> ids = articleRepository.findUsersIdByLanguage(4);
        boolean expected = (ids.size() == 1);
        assert(expected);
        articleRepository.deleteAll();
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
