package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.util.List;

public interface Post {
     int getId();
    String getAuthor();
    int getAuthorPicId();
    String getTime();
    Boolean getLike();
    void  setLike(boolean value);
    Bitmap getAuthorPic();
    int getLikes();
    void setLikes(int likes);
    List<Comment> getComments();
    String getContent();

    Drawable getuserpic();
     void setUserpic(Drawable pic);

    void setContent(String content);
    void setId(int i);

    void addComment(Comment comment);
}
