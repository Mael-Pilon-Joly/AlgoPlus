package com.filesharing.springjwt.repository;

import com.filesharing.springjwt.models.Article;
import com.filesharing.springjwt.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
}
