package com.filesharing.springjwt.models;

import java.util.List;

public interface UserProjection {
    String getUsername();
    String getEmail();
    Long getId();
    FileDB getAvatar();
    FileDB getCV();
    List<Article> getArticles();
    int getPoints();
}
