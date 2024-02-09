package com.example.projectoneex2;

import static com.example.projectoneex2.MainActivity.userList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    private ImageView profilePic;
    private Button Logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        profilePic=findViewById(R.id.profileImage);
        profilePic.setImageBitmap(userList.get(0).getProfileImage());
        TextView textView=findViewById(R.id.username);
        Logout=findViewById(R.id.Logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home();
            }
        });
        textView.setText(userList.get(0).getUsername());

    }

    private void home() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}