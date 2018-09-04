package com.justinlee.jntfirbase;

public class Article {
    String article_title;
    String article_content;
    String article_tag;
    String author;
    String created_time;
    String user_email;

    public Article(String articleTitle, String articleContent, String articleTag, String author, String createdTime, String email) {
        this.article_title = articleTitle;
        this.article_content = articleContent;
        this.article_tag = articleTag;
        this.author = author;
        this.created_time = createdTime;
        this.user_email = email;
    }

    public String getArticle_title() {
        return article_title;
    }

    public String getArticle_content() {
        return article_content;
    }

    public String getArticle_tag() {
        return article_tag;
    }

    public String getAuthor() {
        return author;
    }

    public String getCreated_time() {
        return created_time;
    }

    public String getUser_email() {
        return user_email;
    }
}
