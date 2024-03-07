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

import com.example.projectoneex2.ImagePost;
import com.example.projectoneex2.R;
import com.example.projectoneex2.Request;

import java.util.List;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ReqViewHolder> {

    private FriendRequestAdapter.ReqActionListener reqActionListener; // Listener for post actions

    private final FriendRequestAdapter adapter;

    // Interface for defining post action listener methods
    public interface ReqActionListener {
        void onDeletsButtonClick(int position, FriendRequestAdapter adapter);
        void onApproveButtonClick(int position, FriendRequestAdapter adapter);
    }

    // View holder class for managing individual post views
    class ReqViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        public ImageView AuthorPic;
        private Button approveButton;
        private Button declineButton;

        // Constructor to initialize view elements
        private ReqViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.authorName);
            AuthorPic = itemView.findViewById(R.id.imageViewPic);
            approveButton = itemView.findViewById(R.id.Approve);
            declineButton = itemView.findViewById(R.id.Decline);
        }
    }

    private final LayoutInflater mInflater;
    private List<Request> requests; // List of posts to display

    // Constructor to initialize the adapter
    public FriendRequestAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.adapter = this;
    }

    // Method to set the post action listener
    public void setReqActionListener(FriendRequestAdapter.ReqActionListener listener) {
        this.reqActionListener = listener;
    }

    // Method to create a view holder for a post item
    @Override
    public FriendRequestAdapter.ReqViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.friend_activity, parent, false);
        return new FriendRequestAdapter.ReqViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReqViewHolder holder, int position) {
        holder.tvAuthor.setText(requests.get(position).getNickname());
        holder.AuthorPic.setImageBitmap(requests.get(position).getImgBitmap());
        holder.approveButton.setOnClickListener(v -> reqActionListener.onApproveButtonClick(position, adapter));
        holder.declineButton.setOnClickListener(v -> reqActionListener.onDeletsButtonClick(position, adapter));
    }

    // Method to bind data to a post item vie

    // Method to set the list of posts
    public void setReq(List<Request> s) {

        requests = s;
        notifyDataSetChanged();

    }


    // Method to get the total number of posts
    @Override
    public int getItemCount() {
        return (requests != null) ? requests.size() : 0;
    }

    // Method to get the list of posts
    public List<Request> getRequests() {
        return requests;
    }
}

