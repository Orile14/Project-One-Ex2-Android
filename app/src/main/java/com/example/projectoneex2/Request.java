package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Request {


    private String nickname;
    private String ID;
    private String img;


    public Request(String nickname, String ID, String img) {
        this.nickname = nickname;
        this.ID = ID;
        this.img = img;
    }
    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
    public String getImg() {
        return img;
    }
    public Bitmap getImgBitmap() {
        return stringToBitmap(img);
    }
    public void setImg(String img) {
        this.img = img;
    }
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

}
