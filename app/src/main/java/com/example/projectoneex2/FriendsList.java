package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;
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

import com.example.projectoneex2.adapters.FriendRequestAdapter;
import com.example.projectoneex2.adapters.FriendsAdapter;
import com.example.projectoneex2.adapters.PostsListAdapter;
import com.example.projectoneex2.viewmodel.FriendRequestViewModel;
import com.example.projectoneex2.viewmodel.FriendsViewModel;
import com.example.projectoneex2.viewmodel.ProfilePostsViewModel;

import java.util.List;

public class FriendsList extends AppCompatActivity  {
    private FriendsViewModel viewModel;
    private FriendsAdapter adapter;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_friends_list);
        String token = getIntent().getStringExtra("TOKEN_KEY");
        String ID = getIntent().getStringExtra("ID");
        this.token = token;
        ImageButton home= findViewById(R.id.floatingActionButton);
        //marking darkbutton state
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        FriendsAdapter adapter = new FriendsAdapter(this);
        this.adapter = adapter;
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        FriendsViewModel viewModel;
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        this.viewModel = viewModel;
        viewModel.setToken(token);
        viewModel.getFriends(token).observe(this, friends -> {
            adapter.setFriends(friends);
            adapter.notifyDataSetChanged();
        });

        home.setOnClickListener(v -> {
            finish();
        });

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