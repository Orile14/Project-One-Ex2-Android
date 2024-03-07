package com.example.projectoneex2;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
// Class to represent a post with an image
// Entity annotation to define this class as an entity for Room database
@Entity(tableName = "image_posts")
public class ImagePost   {
    //room id
    @PrimaryKey (autoGenerate = true)
    private int id;
    //server id
    private String _id;
    //dao id(based on the user)
    private String daoID;
    private String comments ;
    private boolean like = false; // Indicates if the post is liked
    private int picID = -1;
    private String postOwnerID;
    int commentsNum = 0; private  String author;
    private String content;
    private int profileImage;
    private String userpic = null;
    private  String time;
    private int likes = 0;
    private String AuthorPic;


    // Constructor for posts with an existing ID and profile image resource ID
    public ImagePost(String author, String content, int id, int profileImage, String time) {
        this.author = author;
        this.picID = id;
        this.content = content;
        this.profileImage = profileImage;
        this.time = time;
    }
    // Constructor for posts with profile picture as Bitmap
    public ImagePost(String author, String content, String pic, String profileImage, String time) {
        this.author = author;
        this.content = content;
        this.userpic = pic;
        this.AuthorPic = profileImage;
        this.time = time;
    }
    public ImagePost(String author, String content, String pic, String profileImage, String time,String id) {
        this.author = author;
        this.content = content;
        this.userpic = pic;
        this.AuthorPic = profileImage;
        this.time = time;
        this._id=id;
    }
    public ImagePost() {

    }
    //setter fot he id
    public void setId(int id) {
        this.id = id;
    }
    // get the number of comments
    public int getCommentsNum() {
        return commentsNum;
    }
    // set the number of comments
    public void setCommentsNum(int commentsNum) {
        this.commentsNum = commentsNum;
    }
    //set the author of the post
    public void setAuthor(String author) {
        this.author = author;
    }
    //set time of the post
    public void setTime(String time) {
        this.time = time;
    }
    //set the profile image of the author
    public void setProfileImage(int profileImage) {
        this.profileImage = profileImage;
    }
    //get the id
    public String get_id() {
        return _id;
    }
    //set the id
    public void set_id(String _id) {
        this._id = _id;
    }
    //get the comments
    @TypeConverter
    public String fromList(List<Comment> countryLang) {
        if (countryLang == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        String json = gson.toJson(countryLang, type);
        return json;
    }
    //set the comments
    @TypeConverter
    public List<Comment> fromString(String countryLangString) {
        if (countryLangString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        List<Comment> countryLangList = gson.fromJson(countryLangString, type);
        return countryLangList;
    }

    // Getter method for pic ID
    public int getPicID() {
        return picID;
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
    // Method to convert a drawable to a string
    public static String drawableToString(Drawable drawable) {
        Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    // Method to convert a string to a drawable
    public static Drawable stringToDrawable(String encodedString) {
        if (encodedString == null) {
            return null;
        }
        if (encodedString.startsWith("data")) {
            String[] parts = encodedString.split(",");
            if (parts.length != 2) {
                // Handle invalid base64 string
                return null;
            }
            String base64Data = parts[1];
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return new BitmapDrawable(bitmap);
        } else {
            byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return new BitmapDrawable(bitmap);
        }
    }

    // Setter method for pic ID
    public void setPicID(int i) {
        this.picID = i;
    }
    // Getter method for author's profile picture as Bitmap
    public String getAuthorPic() {
        return AuthorPic;
    }
    // Getter method for id
    public int getId() {
        return id;
    }
    // get the dao id
    public String getDaoID() {
        return daoID;
    }
    // set the dao id
    public void setDaoID(String daoID) {
        this.daoID = daoID;
    }

    // Setter method for author's profile picture as Bitmap
    public void setAuthorPicBit(Bitmap authorPic) {
        this.AuthorPic = bitmapToString(authorPic);
    }
    // Setter method for author's profile picture as Bitmap
    public void setAuthorPic(String authorPic) {
        this.AuthorPic = authorPic;
    }

    // Getter method for post author's name
    public String getAuthor() {
        return author;
    }

    // Getter method for profile image resource ID
    public int getAuthorPicId() {
        return this.profileImage;
    }

    // Getter method for post creation time
    public String getTime() {
        return time;
    }

    // Getter method for like status of the post

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

    public Bitmap getAuthorPicBit() {
        return stringToBitmap(this.AuthorPic);
    }

    // Setter method for number of likes for the post
    public void setLikes(int likes) {
        this.likes = likes;
    }

    // Getter method for post content
    public String getContent() {
        return content;
    }
    // Getter method for user's profile picture as Bitmap
    public int getProfileImage() {
        return profileImage;
    }

    // Getter method for user's profile picture as Drawable
    public Drawable getUserPicDraw() {
        return stringToDrawable(userpic);
    }
    // Getter method for user's profile picture as Drawable
    public String getUserpic() {
        return userpic;
    }

    // Setter method for user's profile picture as Drawable
    public void setUserpicDraw(Drawable pic) {
        this.userpic = drawableToString(pic);
    }
    // Setter method for user's picture
    public void setUserpic(String pic) {
        this.userpic = pic;
    }
    // Method to convert a list of comments to a string
    public List<Comment> listToComments(String s){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        List<Comment> countryLangList = gson.fromJson(s, type);
        return countryLangList;
    }
    // Method to convert a list of comments to a string
    public String commentsToList(List<Comment> c){
        Gson gson = new Gson();
        Type type = new TypeToken<List<Comment>>() {}.getType();
        String json = gson.toJson(c, type);
        return json;
    }

    // Setter method for post content
    public void setContent(String content) {
        this.content = content;
    }
    // Getter method for comments
    public String getComments(){
        return comments;
    }
    // Getter method for comments

    public List<Comment> getCommentsList() {
        return listToComments(comments);
    }
    // Method to edit a comment
    public void editComment(int id,String content){
        // Add the comment to the beginning of the comments list
        Comment c= listToComments(comments).get(id);
        c.setContent(content);
    }

    // Method to add a comment to the post
    public void addComment(Comment comment) {
        // Check if the comments list is null
        if (this.comments == null) {
            List<Comment> comments = new ArrayList<>();
            comments.add(0, comment);
            commentsNum += 1;
            this.comments = commentsToList(comments);
            return;
        }
        // Increment the number of comments
        commentsNum += 1;
        List<Comment> comments = listToComments(this.comments);
        // Add the comment to the beginning of the comments list
        comments.add(0, comment);
        this.comments = commentsToList(comments);
    }
    // Method to set the comments
    public void setComments(String comments) {
        this.comments = comments;
    }
    // Method to get the post owner id
    public String getPostOwnerID() {
        return postOwnerID;
    }
    // Method to set the post owner id
    public void setPostOwnerID(String postOwnerID) {
        this.postOwnerID = postOwnerID;
    }
}