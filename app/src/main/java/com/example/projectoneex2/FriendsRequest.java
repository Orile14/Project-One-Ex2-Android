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
import com.example.projectoneex2.adapters.PostsListAdapter;
import com.example.projectoneex2.viewmodel.FriendRequestViewModel;
import com.example.projectoneex2.viewmodel.ProfilePostsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

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
        //marking darkbutton state
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        FloatingActionButton back= findViewById(R.id.floatingActionButton);
       FriendRequestAdapter adapter = new FriendRequestAdapter(this);
       this.adapter = adapter;
       adapter.setReqActionListener(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        FriendRequestViewModel viewModel;
        viewModel = new ViewModelProvider(this).get(FriendRequestViewModel.class);
        this.viewModel = viewModel;
        viewModel.setToken(token);
        viewModel.getRequests(token).observe(this, requests -> {
            adapter.setReq(requests);
            adapter.notifyDataSetChanged();
        });
        back.setOnClickListener(v -> {
            finish();
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

    @Override
    public void onDeletsButtonClick(int position, FriendRequestAdapter adapter) {
        viewModel.declineRequest(token, adapter.getRequests().get(position).getID(),FeedActivity.userId);
        adapter.getRequests().remove(position);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onApproveButtonClick(int position, FriendRequestAdapter adapter) {
        viewModel.approveRequest(token, adapter.getRequests().get(position).getID(),FeedActivity.userId);
        adapter.getRequests().remove(position);
        adapter.notifyDataSetChanged();
    }
}