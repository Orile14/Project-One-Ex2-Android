package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectoneex2.adapters.FriendRequestAdapter;
import com.example.projectoneex2.viewmodel.FriendRequestViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
// Activity for displaying the list of friend requests
public class FriendsRequest extends AppCompatActivity implements FriendRequestAdapter.ReqActionListener {
    private FriendRequestViewModel viewModel;
    private FriendRequestAdapter adapter;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_friends_request);
        String token = getIntent().getStringExtra("TOKEN_KEY");
        this.token = token;
        // Initialize views
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        // Set the listener for the swipe to refresh layout
        RecyclerView reqList = findViewById(R.id.lstPosts);
        FloatingActionButton back= findViewById(R.id.floatingActionButton);
       FriendRequestAdapter adapter = new FriendRequestAdapter(this);
       this.adapter = adapter;
       adapter.setReqActionListener(this);
       // Set the adapter
        reqList.setAdapter(adapter);
        reqList.setLayoutManager(new LinearLayoutManager(this));
        FriendRequestViewModel viewModel;
        viewModel = new ViewModelProvider(this).get(FriendRequestViewModel.class);
        this.viewModel = viewModel;
        viewModel.setToken(token);
        //observe the requests list
        viewModel.getRequests(token).observe(this, requests -> {
            adapter.setReq(requests);
            adapter.notifyDataSetChanged();
        });
        back.setOnClickListener(v -> {
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
    // Method to handle the delete button click
    @Override
    public void onDeletsButtonClick(int position, FriendRequestAdapter adapter) {
        viewModel.declineRequest(token, adapter.getRequests().get(position).getID(),FeedActivity.userId);
        adapter.getRequests().remove(position);
        adapter.notifyDataSetChanged();
    }
    // Method to handle the approve button click
    @Override
    public void onApproveButtonClick(int position, FriendRequestAdapter adapter) {
        viewModel.approveRequest(token, adapter.getRequests().get(position).getID(),FeedActivity.userId);
        adapter.getRequests().remove(position);
        adapter.notifyDataSetChanged();
    }
}