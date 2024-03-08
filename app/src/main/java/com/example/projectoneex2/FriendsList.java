package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectoneex2.adapters.FriendsAdapter;
import com.example.projectoneex2.viewmodel.FriendsViewModel;
// Activity for displaying the list of friends
public class FriendsList extends AppCompatActivity  {
    private FriendsViewModel viewModel;
    private FriendsAdapter adapter;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dark mode setting
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_friends_list);
        String token = getIntent().getStringExtra("TOKEN_KEY");
        String ID = getIntent().getStringExtra("ID");
        this.token = token;
        // Initialize views
        ImageButton home= findViewById(R.id.floatingActionButton);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView friendsList = findViewById(R.id.lstPosts);
        FriendsAdapter adapter = new FriendsAdapter(this);
        this.adapter = adapter;
        // Set the adapter
        friendsList.setAdapter(adapter);
        friendsList.setLayoutManager(new LinearLayoutManager(this));
        FriendsViewModel viewModel;
        viewModel = new ViewModelProvider(this).get(FriendsViewModel.class);
        this.viewModel = viewModel;
        viewModel.setToken(token);
        //observe the friend list
        viewModel.getFriends(token).observe(this, friends -> {
            adapter.setFriends(friends);
            adapter.notifyDataSetChanged();
        });
        // Set the listener for the swipe to refresh layout
        home.setOnClickListener(v -> {
            finish();
        });
        refreshLayout.setOnRefreshListener(() -> {
            //stop refreshing
            refreshLayout.setRefreshing(false);
        });

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