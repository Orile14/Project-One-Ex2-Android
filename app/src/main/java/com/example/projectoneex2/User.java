package com.example.projectoneex2;

import android.graphics.Bitmap;

public class User {
    private String username;
    private String password;
    private Bitmap profileImage;

    public User(String username, String password, Bitmap pic) {
        this.username = username;
        this.password = password;
        this.profileImage=pic;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    public Bitmap getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(Bitmap profileImage) {
        this.profileImage = profileImage;
    }
}
