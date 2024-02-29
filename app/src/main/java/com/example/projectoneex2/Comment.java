package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;


import java.io.ByteArrayOutputStream;


public class Comment  {

    private String id;

    public String getCommentOwnerID() {
        return commentOwnerID;
    }

    public void setCommentOwnerID(String commentOwnerID) {
        this.commentOwnerID = commentOwnerID;
    }

    private String commentOwnerID;
    // Comment attributes
    private boolean like = false;
    private final String author;
    private String content;
    private String date;
    private int likes = 0;
    private String authorPic;

    // Constructor to initialize a Comment object with a profile picture
    public Comment(String author, String content, Bitmap authorPic) {
        this.author = author;
        this.content = content;
        this.authorPic = bitmapToString(authorPic);
    }

    // Constructor to initialize a Comment object without a profile picture
    public Comment(String author, String content,String date) {
        this.author = author;
        this.content = content;
        this.date=date;
    }
    public String getId() {
        return id;
    }
    public void setId(String picID) {
        this.id = picID;
    }
    public static String bitmapToString(Bitmap bitmap) {
        if (bitmap==null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public static Bitmap stringToBitmap(String encodedString) {
        if (encodedString==null) {return null;}
            byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

    }
    // Getter method for retrieving the comment's ID

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
    public Bitmap getAuthorPicBit() {
        return stringToBitmap(authorPic);
    }
    public String getAuthorPic() {
        return authorPic;
    }

    public void setAuthorPic(String authorPic) {
        this.authorPic = authorPic;
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
