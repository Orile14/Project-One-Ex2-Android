package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;
import static com.example.projectoneex2.Login.userList;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class Menu extends AppCompatActivity {
    private ImageView profilePic;
    private Button Logout;
    private ImageButton home;
    private ToggleButton darkModeToggle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_menu);
        profilePic = findViewById(R.id.profileImage);
        home=findViewById(R.id.imageButton2);
        darkModeToggle = findViewById(R.id.toggleButton2);
        darkModeToggle.setChecked(isDarkTheme);
        profilePic.setImageBitmap(userList.get(0).getProfileImage());
        TextView textView = findViewById(R.id.username);
        Logout = findViewById(R.id.Logout);
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                home();
            }
        });
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feed();
            }
        });
        textView.setText(userList.get(0).getNickname());


        darkModeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDarkTheme = isChecked;
                saveThemePreference();
                recreate(); // Restart activity to apply theme changes
            }
        });
    }

    private void home() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }
    private void feed() {
        Intent i = new Intent(this, feedActivity.class);
        startActivity(i);
    }
    private void loadThemePreference() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(PREF_THEME_KEY, false); // Default is light theme
    }

    private void saveThemePreference() {
        sharedPreferences.edit().putBoolean(PREF_THEME_KEY, isDarkTheme).apply();
    }

    private void applyTheme() {
        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }
}