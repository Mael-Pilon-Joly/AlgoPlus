package com.filesharing.springjwt.payload.response;

import com.filesharing.springjwt.models.Article;

import java.util.List;

public class ArticleResponse {
    List<Article> articles;

    public ArticleResponse(List<Article> articles) {
        this.articles = articles;
    }

    public ArticleResponse() {

    }

    public List<Article> getArticle() {
        return articles;
    }

    public void setArticle(List<Article> article) {
        this.articles = article;
    }
}
