package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Entity;

import java.util.ArrayList;
import java.util.List;

@Entity
public class imagePost extends AppCompatActivity implements Post {
    // Define your request code here
    private List<Comment> comments=new ArrayList<>();
    private boolean like=false;
    private int id=-1;
    int commentsNum=0;
    private final String author;
    private String content;
    private final String time;
    private int likes=0;
    private Bitmap AuthorPic;
    private int profileImage;
    private Drawable userpic=null;

    public imagePost(String author, String content, int id, int profileImage,String time) {
        this.author = author;
        this.id=id;
        this.content=content;
        this.profileImage=profileImage;
        this.time=time;
    }

    public imagePost(String author, String content, Drawable pic, Bitmap profileImage,String time) {
        this.author = author;
        this.content=content;
        this.userpic=pic;
        this.AuthorPic=profileImage;
        this.time=time;

    }


    public int getId() {
        return id;
    }
    public void setId(int i) {
        this.id=i;
    }
    public void setAuthorPic(Bitmap authorPic){
        this.AuthorPic=authorPic;
    }


    public String getAuthor() {
        return author;
    }

    @Override
    public int getAuthorPicId() {
        return this.profileImage;
    }

    public String getTime(){
        return time;
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

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getContent() {
        return content;
    }

    public Drawable getuserpic() {
        return userpic;
    }


    public void setUserpic(Drawable pic) {
        this.userpic=pic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void addComment(Comment comment) {
        if (this.comments==null){
            this.comments=new ArrayList<>();

        }
        commentsNum+=1;
        this.comments.add(0,comment);
    }
}