package com.example.projectoneex2;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
public class Comment extends AppCompatActivity {
    // Comment attributes
    private boolean like = false;
    private final String author;
    private String content;
    private int likes = 0;
    private Bitmap authorPic;

    // Constructor to initialize a Comment object with a profile picture
    public Comment(String author, String content, Bitmap authorPic) {
        this.author = author;
        this.content = content;
        this.authorPic = authorPic;
    }

    // Constructor to initialize a Comment object without a profile picture
    public Comment(String author, String content) {
        this.author = author;
        this.content = content;
    }

    // Getter method for retrieving the comment's ID
    public int getId() {
        return 0; // Placeholder return value; actual implementation may vary
    }

    // Getter method for retrieving the comment author's username
    public String getAuthor() {
        return author;
    }

    // Getter method for retrieving the comment's like status
    public Boolean getLike() {
        return like;
    }

    // Getter method for retrieving the number of likes for the comment
    public int getLikes() {
        return likes;
    }

    // Setter method to set the comment's like status
    public void setLike(boolean value) {
        this.like = value;
    }

    // Getter method for retrieving the comment author's profile picture
    public Bitmap getAuthorPic() {
        return authorPic;
    }

    // Setter method to set the number of likes for the comment
    public void setLikes(int likes) {
        this.likes = likes;
    }

    // Getter method for retrieving the content of the comment
    public String getContent() {
        return content;
    }

    // Setter method to set the content of the comment
    public void setContent(String content) {
        this.content = content;
    }
}
