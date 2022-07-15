package com.filesharing.springjwt.repository;

import com.filesharing.springjwt.models.Comment;
import com.filesharing.springjwt.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query(value = "select * from comments where `id`=?1 order by published desc", nativeQuery = true)
    Optional<List<Comment>> findByArticleId(Long articleId);
}

