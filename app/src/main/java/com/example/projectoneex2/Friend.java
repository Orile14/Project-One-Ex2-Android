package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
//class to represent a friend
public class Friend {
    private String username;
    private String img;

//constructor to initialize a friend object
    public Friend(String username, String img) {
        this.username = username;
        this.img = img;
    }
//method to retrieve the friend's username
    public String getUsername() {
        return username;
    }
//method to retrieve the friend's profile picture
    public String getImg() {
        return img;
    }
//method to set the friend's username
    public void setUsername(String username) {
        this.username = username;
    }
//method to set the friend's profile picture
    public void setImg(String img) {
        this.img = img;
    }
//method to convert a bitmap to a string
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
//method to convert a string to a bitmap
    public Bitmap getImgBitmap() {
        return stringToBitmap(img);
    }
}
