package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;

// Class representing a user in the system
public class User {
    // User attributes
    @PrimaryKey (autoGenerate = true)
    private String id;
    private final String username;
    private final String nickname;
    private final String password;
    private String profileImage;

    private String friends=null;

    private String posts=null;

    // Constructor to initialize a User object
    public User(String username, String password, Bitmap pic,String nickname) {
        this.username = username;
        this.password = password;
        this.profileImage=bitmapToString(pic);
        this.nickname=nickname;
    }
    //converts a bitmap to a string
    public static String bitmapToString(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    public String getFriends() {
        return friends;
    }
    // Method to get the posts
    public String getPosts() {
        return posts;
    }
    // Method to set the posts
    public void setPosts(String posts) {
        this.posts = posts;
    }
    // Method to convert a string to a bitmap
    public static Bitmap stringToBitmap(String encodedString) {
        String[] parts = encodedString.split(",");
        if (parts.length != 2) {
            // Handle invalid base64 string
            return null;
        }
        String base64Data = parts[1];
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    // Getter method for retrieving the username
    public String getUsername() {
        return username;
    }

    // Getter method for retrieving the nickname
    public String getNickname() {
        return nickname;
    }

    // Getter method for retrieving the password
    public String getPassword() {
        return password;
    }

    // Getter method for retrieving the profile image
    public Bitmap getProfileImage() {
        return stringToBitmap(profileImage);
    }
    // Getter method for retrieving the profile image
    public String getProfile() {
        return profileImage;
    }
    // Getter method for retrieving the user id
    public String getId() {
        return id;
    }
}
