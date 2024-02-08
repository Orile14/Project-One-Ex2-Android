package com.example.projectoneex2;

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
    private int pic=0;
    private Drawable userpic=null;

    public Comment(String author, String content) {
        this.author = author;
        this.content=content;
    }

    public Comment(String author, String content, Drawable pic) {
        this.author = author;
        this.content=content;
        this.userpic=pic;

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

    public int getPic() {
        return pic;
    }
    public Drawable getuserpick() {
        return userpic;
    }


    public void setPic(int pic) {
        this.pic = pic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }



    @Override
    public void addComment(Comment comment) {

    }
}
