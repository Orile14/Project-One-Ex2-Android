package com.example.projectoneex2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import androidx.recyclerview.widget.RecyclerView;
import com.example.projectoneex2.Comment;
import com.example.projectoneex2.R;

import java.util.List;

// Adapter class for managing the display of comments in a RecyclerView
public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.PostViewHolder> {
    private CommentActionsListener commentListener; // Listener for comment actions
    int postPosition; // Position of the post associated with the comments
    CommentListAdapter adapter = this;

    // Interface for defining comment action listener methods
    public interface CommentActionsListener {
        void onCommentEditButtonClick(int position, int postPosition, CommentListAdapter adapter);
        void onCommentDeleteButtonClick(int position, int postPosition, CommentListAdapter adapter);
        void onCommentLikeButtonClick(int position, int postPosition, CommentListAdapter adapter, TextView likeCounter);
    }

    // View holder class for managing individual comment views
    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        public TextView likeCounter;
        public ImageView pic;
        public ImageButton likeButton;
        public ImageButton editButton;
        public ImageButton deleteButton;

        // Constructor to initialize view elements
        private PostViewHolder(View itemView) {
            super(itemView);
            likeCounter = itemView.findViewById(R.id.likeCounter1);
            tvAuthor = itemView.findViewById(R.id.authorName);
            tvContent = itemView.findViewById(R.id.commentText);
            pic = itemView.findViewById(R.id.imageViewPic);
            editButton = itemView.findViewById(R.id.imageButton6);
            deleteButton = itemView.findViewById(R.id.imageButton12);
            likeButton = itemView.findViewById(R.id.likeButton);
        }
    }

    private final LayoutInflater mInflater;
    private List<Comment> comments; // List of comments to display

    // Constructor to initialize the adapter
    public CommentListAdapter(Context context, int postPosition) {
        mInflater = LayoutInflater.from(context);
        this.postPosition = postPosition;
    }

    // Method to set the comment action listener
    public void setOnEditButtonClickListener(CommentActionsListener listener) {
        this.commentListener = listener;
    }

    // Method to create a view holder for a comment item

    @Override
    public PostViewHolder onCreateViewHolder( ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_comment, parent, false);
        return new PostViewHolder(itemView);
    }

    // Method to bind data to a comment item view
    public void onBindViewHolder(PostViewHolder holder, int position) {
        // Set click listeners for various action buttons
        holder.likeCounter.setText(comments.get(position).getLikes() + " Likes");
        holder.editButton.setOnClickListener(v -> {
            if (commentListener != null) {
                commentListener.onCommentEditButtonClick(position, postPosition, adapter);
            }
        });
        holder.deleteButton.setOnClickListener(v -> {
            if (commentListener != null) {
                commentListener.onCommentDeleteButtonClick(position, postPosition, adapter);
            }
        });
        holder.likeButton.setOnClickListener(v -> {
            if (commentListener != null) {
                commentListener.onCommentLikeButtonClick(position, postPosition, adapter, holder.likeCounter);
            }
        });

        // Bind comment data to the view holder elements
        if (comments != null) {
            final Comment current = comments.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            holder.pic.setImageBitmap(current.getAuthorPic());
        }
    }

    // Method to set the list of comments
    public void setComments(List<Comment> s) {
        comments = s;
        notifyDataSetChanged();
    }

    // Method to get the total number of comments
    @Override
    public int getItemCount() {
        return (comments != null) ? comments.size() : 0;
    }
}
