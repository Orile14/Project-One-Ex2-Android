package com.example.projectoneex2;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

// Entity annotation to define this class as an entity for Room database
public class ImagePost extends AppCompatActivity implements Post {
    // Define your request code here
    private List<Comment> comments = new ArrayList<>(); // List to store comments for this post
    private boolean like = false; // Indicates if the post is liked
    private int id = -1;
    int commentsNum = 0;
    private final String author;
    private String content;
    private final String time;
    private int likes = 0;
    private Bitmap AuthorPic;
    private int profileImage;
    private Drawable userpic = null;

    // Constructor for posts with an existing ID and profile image resource ID
    public ImagePost(String author, String content, int id, int profileImage, String time) {
        this.author = author;
        this.id = id;
        this.content = content;
        this.profileImage = profileImage;
        this.time = time;
    }

    // Constructor for posts with profile picture as Bitmap
    public ImagePost(String author, String content, Drawable pic, Bitmap profileImage, String time) {
        this.author = author;
        this.content = content;
        this.userpic = pic;
        this.AuthorPic = profileImage;
        this.time = time;
    }

    // Getter method for post ID
    public int getId() {
        return id;
    }

    // Setter method for post ID
    public void setId(int i) {
        this.id = i;
    }

    // Setter method for author's profile picture as Bitmap
    public void setAuthorPic(Bitmap authorPic) {
        this.AuthorPic = authorPic;
    }

    // Getter method for post author's name
    public String getAuthor() {
        return author;
    }

    // Getter method for profile image resource ID
    @Override
    public int getAuthorPicId() {
        return this.profileImage;
    }

    // Getter method for post creation time
    public String getTime() {
        return time;
    }

    // Getter method for like status of the post
    @Override
    public Boolean getLike() {
        return like;
    }

    // Getter method for number of likes for the post
    public int getLikes() {
        return likes;
    }

    // Setter method for like status of the post
    public void setLike(boolean value) {
        this.like = value;
    }

    // Getter method for author's profile picture as Bitmap
    @Override
    public Bitmap getAuthorPic() {
        return this.AuthorPic;
    }

    // Setter method for number of likes for the post
    public void setLikes(int likes) {
        this.likes = likes;
    }

    // Getter method for post content
    public String getContent() {
        return content;
    }

    // Getter method for user's profile picture as Drawable
    public Drawable getUserPic() {
        return userpic;
    }

    // Setter method for user's profile picture as Drawable
    public void setUserPic(Drawable pic) {
        this.userpic = pic;
    }

    // Setter method for post content
    public void setContent(String content) {
        this.content = content;
    }

    // Getter method for list of comments for the post
    public List<Comment> getComments() {
        return comments;
    }

    // Method to add a comment to the post
    public void addComment(Comment comment) {
        // Check if the comments list is null
        if (this.comments == null) {
            this.comments = new ArrayList<>();
        }
        // Increment the number of comments
        commentsNum += 1;
        // Add the comment to the beginning of the comments list
        this.comments.add(0, comment);
    }
}
