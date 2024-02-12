package com.example.projectoneex2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectoneex2.Post;
import com.example.projectoneex2.R;
import java.util.List;

// Adapter class for managing the display of posts in a RecyclerView
public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {
    private PostActionListener postActionsListener; // Listener for post actions

    private final PostsListAdapter adapter;

    // Interface for defining post action listener methods
    public interface PostActionListener {
        void onLikeButtonClick(int position);
        void onCommentButtonClick(int position);
        void onDeletsButtonClick(int position, PostsListAdapter adapter);
        void onEditButtonClick(int position);
        void onShareButtonClick();
    }

    // View holder class for managing individual post views
    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        public Button likeButton;
        public ImageButton editButton;
        public ImageButton deleteButton;
        public ImageView AuthorPic;
        public Button share;
        private final Button commentButton;
        private final TextView commentCounter;
        private final TextView time;

        // Constructor to initialize view elements
        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            likeButton = itemView.findViewById(R.id.likeButton);
            editButton = itemView.findViewById(R.id.editButton);
            commentButton = itemView.findViewById(R.id.commentButton);
            AuthorPic = itemView.findViewById(R.id.imageViewPic);
            deleteButton = itemView.findViewById(R.id.postDeleteButton);
            commentCounter = itemView.findViewById(R.id.commentCounter);
            time = itemView.findViewById(R.id.time);
            share = itemView.findViewById(R.id.shareButton);
        }
    }

    private final LayoutInflater mInflater;
    private List<Post> posts; // List of posts to display

    // Constructor to initialize the adapter
    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.adapter = this;
    }

    // Method to set the post action listener
    public void setPostActionListener(PostActionListener listener) {
        this.postActionsListener = listener;
    }

    // Method to create a view holder for a post item
    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_image_post, parent, false);
        return new PostViewHolder(itemView);
    }

    // Method to bind data to a post item view
    @Override
    public void onBindViewHolder(PostViewHolder holder, int position) {
        // Set click listeners for various action buttons
        holder.editButton.setOnClickListener(v -> {
            if (postActionsListener != null) {
                postActionsListener.onEditButtonClick(position);
            }
        });
        holder.share.setOnClickListener(v -> {
            if (postActionsListener != null) {
                postActionsListener.onShareButtonClick();
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (postActionsListener != null) {
                postActionsListener.onDeletsButtonClick(position, adapter);
            }
        });
        holder.commentButton.setOnClickListener(v -> postActionsListener.onCommentButtonClick(position));
        holder.likeButton.setOnClickListener(v -> {
            if (postActionsListener != null) {
                postActionsListener.onLikeButtonClick(position);
            }
        });

        // Bind post data to the view holder elements
        if (posts != null) {
            final Post current = posts.get(position);
            holder.time.setText(current.getTime());
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            holder.AuthorPic.setImageBitmap(current.getAuthorPic());
            int num = (current.getComments() == null) ? 0 : current.getComments().size();
            holder.commentCounter.setText(num + " comments");
            //-1 is a flag that means we take pic from user(drawable)and not id
            if (current.getId() != -1) {
                holder.ivPic.setImageResource(current.getId());
                holder.AuthorPic.setImageResource(current.getAuthorPicId());
                holder.ivPic.setVisibility(View.VISIBLE);
            } else if (current.getuserpic() != null) {
                holder.ivPic.setImageDrawable(current.getuserpic());
                holder.ivPic.setVisibility(View.VISIBLE);
            }
        }
    }

    // Method to set the list of posts
    public void setPosts(List<Post> s) {
        posts = s;
        notifyDataSetChanged();
    }

    // Method to get the total number of posts
    @Override
    public int getItemCount() {
        return (posts != null) ? posts.size() : 0;
    }

    // Method to get the list of posts
    public List<Post> getPosts() {
        return posts;
    }
}
