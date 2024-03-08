package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectoneex2.adapters.PostsListAdapter;
import com.example.projectoneex2.viewmodel.FriendRequestViewModel;
import com.example.projectoneex2.viewmodel.ProfilePostsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
// Activity for displaying the user profile
public class Profile extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load and apply theme preference(dark mode)
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_profile);
        String token = getIntent().getStringExtra("TOKEN_KEY");
        // Initialize views
        TextView username = findViewById(R.id.username);
        ImageView profilePic = findViewById(R.id.profileImage);
        username.setText(FeedActivity.currentNickname);
        Drawable profilePicDrawable =stringToDrawable(FeedActivity.currentProfilePic);
        profilePic.setImageDrawable(profilePicDrawable);
        Button req= findViewById(R.id.button4);
        Button delete= findViewById(R.id.Delete);
        //we don't know if the user is a friend or not so we hide the delete button
        delete.setVisibility(View.INVISIBLE);
        Button friends= findViewById(R.id.button5);
        //we don't know if the user is a friend or not so we hide the friends button
        friends.setVisibility(View.INVISIBLE);
        FloatingActionButton back= findViewById(R.id.floatingActionButton);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        // Set the adapter
        PostsListAdapter adapter = new PostsListAdapter(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        ProfilePostsViewModel viewModel;
        FriendRequestViewModel friendRequestViewModel;
        friendRequestViewModel=new ViewModelProvider(this).get(FriendRequestViewModel.class);
        viewModel=new ViewModelProvider(this).get(ProfilePostsViewModel.class);
        viewModel.setToken(token);
        //observe the user's posts
        viewModel.getProfilePosts(token).observe(this, imagePosts -> {
            adapter.setPosts(imagePosts);
            req.setVisibility(View.INVISIBLE);
            delete.setVisibility(View.VISIBLE);
            friends.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();

        });
        // Send friend request
        req.setOnClickListener(v -> {
           viewModel.sendFriendRequest(token,FeedActivity.currentId);
                req.setVisibility(View.INVISIBLE);
            Toast.makeText(this, "friend request have been sent", Toast.LENGTH_SHORT).show();

            });
        //back to the previous activity
        back.setOnClickListener(v -> {
            finish();
        });
        //open friends list
        friends.setOnClickListener(v -> {
            Intent intent = new Intent(this, FriendsList.class);
            intent.putExtra("TOKEN_KEY", token);
            startActivity(intent);
        });
        //delete the user
        delete.setOnClickListener(v -> {
            friendRequestViewModel.declineRequest(token,FeedActivity.currentId,FeedActivity.userId);
            Toast.makeText(this, "friend deleted!", Toast.LENGTH_SHORT).show();
            finish();
        });
        refreshLayout.setOnRefreshListener(() -> {
            //stop refreshing
            refreshLayout.setRefreshing(false);
        });


    }
    // Method to convert a base64 string to a drawable
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
    // Method to load the theme preference from SharedPreferences
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