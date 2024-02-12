package com.example.projectoneex2;

import android.graphics.Bitmap;

public class User {
    private final String username;
    private final String nickname;
    private final String password;
    private Bitmap profileImage;

    public User(String username, String password, Bitmap pic,String nickname) {
        this.username = username;
        this.password = password;
        this.profileImage=pic;
        this.nickname=nickname;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
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