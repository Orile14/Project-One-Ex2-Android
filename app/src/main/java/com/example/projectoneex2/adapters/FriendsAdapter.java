package com.example.projectoneex2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectoneex2.Friend;
import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.R;
import com.example.projectoneex2.Request;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {


    private final FriendsAdapter adapter;

    // Interface for defining post action listener methods
    // View holder class for managing individual post views
    class FriendViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        public ImageView AuthorPic;


        // Constructor to initialize view elements
        private FriendViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.authorName);
            AuthorPic = itemView.findViewById(R.id.imageViewPic);

        }
    }

    private final LayoutInflater mInflater;
    private List<Friend> friends; // List of posts to display

    // Constructor to initialize the adapter
    public FriendsAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.adapter = this;
    }


    // Method to create a view holder for a post item
    @Override
    public FriendsAdapter.FriendViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.friend_layout, parent, false);
        return new FriendsAdapter.FriendViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendViewHolder holder, int position) {
        holder.tvAuthor.setText(friends.get(position).getUsername());
        holder.AuthorPic.setImageBitmap(friends.get(position).getImgBitmap());
    }

    // Method to bind data to a post item view

    public void onBindViewHolder(PostsListAdapter.PostViewHolder holder, int position) {

    }

    // Method to set the list of posts
    public void setFriends(List<Friend> s) {

        friends = s;
        notifyDataSetChanged();

    }


    // Method to get the total number of posts
    @Override
    public int getItemCount() {
        return (friends != null) ? friends.size() : 0;
    }

    // Method to get the list of posts
    public List<Friend> getRequests() {
        return friends;
    }
}

