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

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {
    private PostActionListener postActionsListener;

    private PostsListAdapter adapter;

    public interface PostActionListener {
        void onLikeButtonClick(int position);

        void onCommentButtonClick(int position);

        void onDeletsButtonClick(int position, PostsListAdapter adapter);

        void onEditButtonClick(int position);

        void onShareButtonClick();
    }

    class PostViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvAuthor;
        private final TextView tvContent;
        private final ImageView ivPic;
        public Button likeButton;
        public ImageButton editButton;
        public ImageButton deleteButton;
        public ImageView AuthorPic;
        public Button share;
        private Button commentButton;
        private TextView commentCounter;
        private TextView time;


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
    private List<Post> posts;

    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        this.adapter = this;
    }

    public void setPostActionListener(PostActionListener listener) {
        this.postActionsListener = listener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_image_post, parent, false);
        return new PostViewHolder(itemView);
    }
    // Inside PostsListAdapter class
    public void onBindViewHolder(PostViewHolder holder, int position) {
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
        if (posts != null) {
            final Post current = posts.get(position);
            holder.time.setText(current.getTime());
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            holder.AuthorPic.setImageBitmap(current.getAuthorPic());
            int num;
            if (current.getComments() == null) {
                num = 0;
            } else {
                num = current.getComments().size();
            }
            holder.commentCounter.setText(num + " comments");
            if (current.getId() != -1) {
                // If it's an integer, assume it's a drawable resource ID
                holder.ivPic.setImageResource(current.getId());
                holder.AuthorPic.setImageResource(current.getAuthorPicId());
                holder.ivPic.setVisibility(View.VISIBLE);
            } else if (current.getuserpick() != null) {
                // If it's a Drawable object
                holder.ivPic.setImageDrawable(current.getuserpick());
                holder.ivPic.setVisibility(View.VISIBLE);
            }
        }
    }
    public void setPosts(List<Post> s) {
        posts = s;
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        else return 0;
    }

    public List<Post> getPosts() {
        return posts;
    }
}