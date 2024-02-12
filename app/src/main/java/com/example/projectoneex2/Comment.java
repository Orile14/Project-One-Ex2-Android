package com.example.projectoneex2;

import android.graphics.Bitmap;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;

@Entity
public class Comment extends AppCompatActivity {
    private boolean like = false;
    private final String author;
    private String content;
    private int likes = 0;
    private Bitmap Authorpic;

    public Comment(String author, String content, Bitmap authorPic) {
        this.author = author;
        this.content = content;
        this.Authorpic = authorPic;
    }

    public Comment(String author, String content) {
        this.author = author;
        this.content = content;

    }
    public int getId() {
        return 0;
    }

    public String getAuthor() {
        return author;
    }


    public Boolean getLike() {
        return like;
    }

    public int getLikes() {
        return likes;
    }

    public void setLike(boolean value) {
        this.like = value;
    }


    public Bitmap getAuthorPic() {
        return Authorpic;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}
