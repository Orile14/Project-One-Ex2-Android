package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class imagePost extends AppCompatActivity implements Post {
    private static final int EDIT_POST_REQUEST = 1; // Define your request code here
    private List<Comment> comments=new ArrayList<>();
    private boolean like=false;
    private int id=-1;
    int commentsNum=0;
    private String author;
    private String content;
    private int likes=0;
    private Bitmap pic;
    private Bitmap AuthorPic;
    private Drawable userpic=null;

    public imagePost(String author, String content, int id) {
        this.author = author;
        this.id=id;
        this.content=content;


    }

    public imagePost(String author, String content, Drawable pic) {
        this.author = author;
        this.content=content;
        this.userpic=pic;

    }


    public int getId() {
        return id;
    }
    public void setAuthorPic(Bitmap authorPic){
        this.AuthorPic=authorPic;
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
        return this.AuthorPic;
    }

    public boolean getLike(boolean value) {
        return this.like;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public Bitmap getPic() {
        return pic;
    }
    public Drawable getuserpick() {
        return userpic;
    }


    public void setPic(Bitmap pic) {
        this.pic = pic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public List<Comment> getComments() {
        return comments;
    }
    public int getCommentsNum(){
        return commentsNum;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }
    public void addComment(Comment comment) {
        if (this.comments==null){
            this.comments=new ArrayList<>();

        }
        commentsNum+=1;
        this.comments.add(0,comment);
    }
}
