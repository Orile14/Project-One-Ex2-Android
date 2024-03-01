package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.projectoneex2.adapters.PostsListAdapter;
import com.example.projectoneex2.viewmodel.PostsViewModel;
import com.example.projectoneex2.viewmodel.ProfilePostsViewModel;

import java.util.List;

public class Profile extends AppCompatActivity {
    public static List<ImagePost> profilePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_profile);
        String token = getIntent().getStringExtra("TOKEN_KEY");
        TextView username = findViewById(R.id.username);
        ImageView profilePic = findViewById(R.id.profileImage);
        ImageButton home= findViewById(R.id.imageButton2);
        ToggleButton darkModeToggle = findViewById(R.id.toggleButton2);
        darkModeToggle.setChecked(isDarkTheme);
        //marking darkbutton state
        ImageButton Menu= findViewById(R.id.menuButton);
        username.setText(FeedActivity.currentNickname);
        Drawable profilePicDrawable =stringToDrawable(FeedActivity.currentProfilePic);
        profilePic.setImageDrawable(profilePicDrawable);
        Button req= findViewById(R.id.button4);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        ProfilePostsViewModel viewModel;
        viewModel=new ViewModelProvider(this).get(ProfilePostsViewModel.class);
        viewModel.setToken(token);
        viewModel.getProfilePosts(token).observe(this, imagePosts -> {
            adapter.setPosts(imagePosts);
                req.setVisibility(View.INVISIBLE);
            adapter.notifyDataSetChanged();

        });
        darkModeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkTheme = isChecked;
            saveThemePreference();
            recreate(); // Restart activity to apply theme changes
        });
        home.setOnClickListener(v -> {
            finish();
        });
        Menu.setOnClickListener(v -> {
            Intent i = new Intent(this, Menu.class);
            startActivity(i);
        });

    }

    public  Drawable stringToDrawable(String encodedString) {
        if (encodedString == null) {
            return null;
        }
        if (encodedString.startsWith("data")) {
            String[] parts = encodedString.split(",");
            if (parts.length != 2) {
                // Handle invalid base64 string
                return null;
            }
            String base64Data = parts[1];
            byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return new BitmapDrawable(bitmap);
        } else {
            byte[] bytes = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            return new BitmapDrawable(bitmap);
        }
    }
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