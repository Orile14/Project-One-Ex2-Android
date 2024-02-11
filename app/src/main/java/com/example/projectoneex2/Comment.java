package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;

import java.util.List;

@Entity
public class Comment extends AppCompatActivity implements Post {
    private boolean like=false;
    private String author;
    private String content;
    private int likes=0;
    private Bitmap Authorpic;
    private Drawable userpic=null;

    public Comment(String author, String content, Bitmap authorPic) {
        this.author = author;
        this.content=content;
        this.Authorpic=authorPic;
    }

    public Comment(String author, String content, Drawable pic) {
        this.author = author;
        this.content=content;

    }


    @Override
    public int getId() {
        return 0;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public Boolean getLike() {
        return like;
    }

    public int getLikes() {
        return likes;
    }
    public void setLike(boolean value) {
        this.like = value;
    }

    @Override
    public Bitmap getAuthorPic() {
        return Authorpic;
    }

    public boolean getLike(boolean value) {
        return this.like;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    @Override
    public List<Comment> getComments() {
        return null;
    }



    public String getContent() {
        return content;
    }

    public Bitmap getPic() {
        return null;
    }
    public Drawable getuserpick() {
        return userpic;
    }

    @Override
    public void setUserpic(Drawable pic) {

    }


    public void setPic(Bitmap pic) {
        return;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setId(int i) {

    }

    public void setAuthor(String author) {
        this.author = author;
    }



    @Override
    public void addComment(Comment comment) {

    }
}
