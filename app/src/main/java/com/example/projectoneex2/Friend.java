package com.example.projectoneex2;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

public class Friend {
    private String username;
    private String img;


    public Friend(String username, String img) {
        this.username = username;
        this.img = img;
    }

    public String getUsername() {
        return username;
    }

    public String getImg() {
        return img;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Bitmap getImgBitmap() {
        return stringToBitmap(img);
    }
}
