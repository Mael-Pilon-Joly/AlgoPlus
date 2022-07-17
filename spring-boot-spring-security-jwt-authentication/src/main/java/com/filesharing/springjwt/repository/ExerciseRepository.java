package com.filesharing.springjwt.repository;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

}