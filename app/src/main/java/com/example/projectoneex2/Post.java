package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

public interface Post {
     int getId();
    String getAuthor();
    Boolean getLike();
    void  setLike(boolean value);
    Bitmap getAuthorPic();
    int getLikes();
    void setLikes(int likes);
    public List<Comment> getComments();
    String getContent();
    Bitmap getPic();
    Drawable getuserpick();
    void setPic(Bitmap pic);
    void setContent(String content);
    void setAuthor(String author);

    void addComment(Comment comment);
}
