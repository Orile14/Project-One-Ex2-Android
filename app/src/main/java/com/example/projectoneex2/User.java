package com.example.projectoneex2;

import android.graphics.Bitmap;

// Class representing a user in the system
public class User {
    // User attributes
    private final String username;
    private final String nickname;
    private final String password;
    private Bitmap profileImage;

    // Constructor to initialize a User object
    public User(String username, String password, Bitmap pic,String nickname) {
        this.username = username;
        this.password = password;
        this.profileImage=pic;
        this.nickname=nickname;
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
        return profileImage;
    }
}
