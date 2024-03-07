package com.example.projectoneex2;


import static com.example.projectoneex2.ImagePost.stringToDrawable;
import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectoneex2.viewmodel.UserViewModel;

// Activity class for the menu screen
public class Menu extends AppCompatActivity {
    String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load and apply theme preference(dark mode)
        loadThemePreference();
        applyTheme();
        // Set layout
        setContentView(R.layout.activity_menu);
        this.token = getIntent().getStringExtra("TOKEN_KEY");
        // Initialize views
        ImageView profilePic = findViewById(R.id.profileImage);
        profilePic.setImageDrawable(stringToDrawable(Login.profilePic));
        ImageButton home = findViewById(R.id.imageButton2);
        ToggleButton darkModeToggle = findViewById(R.id.toggleButton2);
        TextView textView = findViewById(R.id.username);
        TextView edit=findViewById(R.id.edit);
        TextView friends=findViewById(R.id.friends);
        textView.setText(Login.nickname);
        Button logout = findViewById(R.id.Logout);
         UserViewModel viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        Button deleteAccount = findViewById(R.id.Delete);
        TextView friendRequestButton = findViewById(R.id.textView7);
        // Set toggle button state
        darkModeToggle.setChecked(isDarkTheme);
        // Set profile picture and username
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
        // Set OnClickListener for the edit profile button
        edit.setOnClickListener(view -> {
            Intent i = new Intent(this, editProfile.class);
            i.putExtra("TOKEN_KEY", token);
            startActivity(i);
        });
        // Set OnClickListener for the friends button
        friends.setOnClickListener(view -> {
            FeedActivity.currentId = FeedActivity.userId;
            Intent i = new Intent(this, FriendsList.class);
            i.putExtra("TOKEN_KEY", token);
            startActivity(i);
        });
        // Set OnClickListener for the friend request button
        friendRequestButton.setOnClickListener(view -> {
            Intent i = new Intent(this, FriendsRequest.class);
            i.putExtra("TOKEN_KEY", token);
            startActivity(i);
        });
        // Set OnClickListener for the delete account button
        deleteAccount.setOnClickListener(view -> {
           viewModel.deleteAccount(token);
                home();
        });
    }
    // Method to navigate to the login screen
    private void home() {
        Intent i = new Intent(this, Login.class);
        startActivity(i);
    }
    // Method to navigate to the feed activity
    private void feed() {
        finish();
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
