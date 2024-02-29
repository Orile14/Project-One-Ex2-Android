package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;
import static com.example.projectoneex2.Login.userList;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;

// Activity class for the menu screen
public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load and apply theme preference(dark mode)
        loadThemePreference();
        applyTheme();
        // Set layout
        setContentView(R.layout.activity_menu);
        // Initialize views
        ImageView profilePic = findViewById(R.id.profileImage);
        ImageButton home = findViewById(R.id.imageButton2);
        ToggleButton darkModeToggle = findViewById(R.id.toggleButton2);
        TextView textView = findViewById(R.id.username);
        Button logout = findViewById(R.id.Logout);
        // Set toggle button state
        darkModeToggle.setChecked(isDarkTheme);
        // Set profile picture and username
//        profilePic.setImageBitmap(userList.get(0).getProfileImage());
//        textView.setText(userList.get(0).getNickname());
        // Set OnClickListener for logout button
        logout.setOnClickListener(view -> home());
        // Set OnClickListener for home button
        home.setOnClickListener(view -> feed());
        // Set OnCheckedChangeListener for the dark mode toggle button
        darkModeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkTheme = isChecked;
            saveThemePreference();
            recreate(); // Restart activity to apply theme changes
        });
    }
    // Method to navigate to the login screen
    private void home() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }
    // Method to navigate to the feed activity
    private void feed() {
        Intent i = new Intent(this, FeedActivity.class);
        startActivity(i);
    }
    // Method to load theme preference from SharedPreferences
    private void loadThemePreference() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(PREF_THEME_KEY, false); // Default is light theme
    }

    // Method to save theme preference to SharedPreferences
    private void saveThemePreference() {
        sharedPreferences.edit().putBoolean(PREF_THEME_KEY, isDarkTheme).apply();
    }
    // Method to apply the selected theme
    private void applyTheme() {
        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }
}
