package com.example.projectoneex2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;

import com.example.projectoneex2.adapters.PostsListAdapter;
import com.example.projectoneex2.viewmodel.PostsViewModel;
import com.example.projectoneex2.viewmodel.ProfilePostsViewModel;

import java.util.List;

public class Profile extends AppCompatActivity {
    public static List<ImagePost> profilePosts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String token = getIntent().getStringExtra("TOKEN_KEY");
        setContentView(R.layout.activity_profile);
        //layout for the posts
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
            adapter.notifyDataSetChanged();

        });

    }
}