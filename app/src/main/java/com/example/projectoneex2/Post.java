package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import java.util.List;

// Interface representing a post
public interface Post {
    // Method to get the ID of the post
    int getId();

    // Method to get the author of the post
    String getAuthor();

    // Method to get the resource ID of the author's profile picture
    int getAuthorPicId();

    // Method to get the time of the post
    String getTime();

    // Method to check if the post is liked
    Boolean getLike();

    // Method to set the like status of the post
    void setLike(boolean value);

    // Method to get the author's profile picture as Bitmap
    Bitmap getAuthorPic();

    // Method to get the number of likes for the post
    int getLikes();

    // Method to set the number of likes for the post
    void setLikes(int likes);

    // Method to get the list of comments for the post
    List<Comment> getComments();

    // Method to get the content of the post
    String getContent();

    // Method to get the user's profile picture as Drawable
    Drawable getUserPic();

    // Method to set the user's profile picture as Drawable
    void setUserPic(Drawable pic);

    // Method to set the content of the post
    void setContent(String content);

    // Method to set the ID of the post
    void setId(int i);

    // Method to add a comment to the post
    void addComment(Comment comment);
}
