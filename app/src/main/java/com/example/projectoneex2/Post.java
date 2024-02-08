package com.example.projectoneex2;

import android.graphics.drawable.Drawable;

import java.util.List;

public interface Post {
     int getId();
    String getAuthor();
    Boolean getLike();
    void  setLike(boolean value);
    int getLikes();
    void setLikes(int likes);
    public List<Comment> getComments();
    String getContent();
    int getPic();
    Drawable getuserpick();
    void setPic(int pic);
    void setContent(String content);
    void setAuthor(String author);

    void addComment(Comment comment);
}
