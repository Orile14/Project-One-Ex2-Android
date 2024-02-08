package com.example.projectoneex2.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectoneex2.Post;
import com.example.projectoneex2.R;
import com.example.projectoneex2.textPost;

import java.util.List;

public class TextPostAdapter extends RecyclerView.Adapter<TextPostAdapter.PostViewHolder> {
    private OnLikeButtonClickListener likeButtonClickListener;
    private OnEditButtonClickListener editButtonClickListener;

    public interface OnLikeButtonClickListener {
        void onLikeButtonClick(int position);
        void onCommentButtonClick(int position);
    }
    public interface OnEditButtonClickListener {
        void onEditButtonClick(int position);
    }
    class PostViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvAuthor;
        private final TextView tvContent;
        public  TextView likeCounter;
        public ImageButton likeButton;
        public ImageButton editButton;
        public ImageButton deleteButton;



        private PostViewHolder(View itemView) {
            super(itemView);

            tvAuthor = itemView.findViewById(R.id.authorName);
            tvContent = itemView.findViewById(R.id.commentText);

        }
    }


    private final LayoutInflater mInflater;
    private List<textPost> posts;;

    public TextPostAdapter(Context context) {
        mInflater = LayoutInflater.from(context);

    }


    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_text_post, parent, false);
        return new TextPostAdapter.PostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        if (posts != null) {
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
        }
    }


    public void setPosts (List<textPost> s){
        posts = s;

        notifyDataSetChanged ();
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        else return 0;
    }
    public List<textPost> getPosts (){ return posts;
    }
}