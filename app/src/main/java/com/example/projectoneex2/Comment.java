package com.example.projectoneex2;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import java.io.ByteArrayOutputStream;

// Class to represent a comment on a post
public class Comment  {
    private String id;
    private String commentOwnerID;
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
    // Method to retrieve the comment's owner id
    public String getCommentOwnerID() {
        return commentOwnerID;
    }
    // Method to set the comment's owner id
    public void setCommentOwnerID(String commentOwnerID) {
        this.commentOwnerID = commentOwnerID;
    }
   // Method to retrieve the comment's id
    public String getId() {
        return id;
    }
    // Method to set the comment's ID
    public void setId(String picID) {
        this.id = picID;
    }
    // Method to convert a bitmap to a string
    public static String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    // Method to convert a string to a bitmap
    public static Bitmap stringToBitmap(String encodedString) {
        if (encodedString != null) {
            if (encodedString.startsWith("data")) {
                String[] parts = encodedString.split(",");
                if (parts.length != 2) {
                    // Handle invalid base64 string
                    return null;
                }
                String base64Data = parts[1];
                byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
            else {
                byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        }
        return null;
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
    public Bitmap getAuthorPicBit() {
        return stringToBitmap(authorPic);
    }
    // Getter method for retrieving the comment author's profile picture
    public String getAuthorPic() {
        return authorPic;
    }
    // Getter method for setting the author pic of the comment
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
