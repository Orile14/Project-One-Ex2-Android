package com.example.projectoneex2.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.ImageButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.projectoneex2.Comment;
import com.example.projectoneex2.Post;
import com.example.projectoneex2.R;

import java.util.List;

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.PostViewHolder> {
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
        private final ImageView ivPic;
        public ImageButton likeButton;
        public ImageButton editButton;
        private EditText editTextContent;
        private ImageButton commentButton;



        private PostViewHolder(View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tvAuthor);
            tvContent = itemView.findViewById(R.id.tvContent);
            ivPic = itemView.findViewById(R.id.ivPic);
            likeButton = itemView.findViewById(R.id.likeButton);
            editButton=itemView.findViewById(R.id.editButton);
            editTextContent=itemView.findViewById(R.id.editTextContent);
            commentButton=itemView.findViewById(R.id.commentButton);
        }
    }


    private final LayoutInflater mInflater;
    private List<Post> posts;

    public PostsListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
    }
    public void setOnLikeButtonClickListener(OnLikeButtonClickListener listener) {
        this.likeButtonClickListener = listener;
    }
    public void setOnEditButtonClickListener(OnEditButtonClickListener listener) {
        this.editButtonClickListener = listener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_image_post, parent, false);
        return new PostViewHolder(itemView);
    }

    // Inside PostsListAdapter class
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editButtonClickListener != null) {
                    editButtonClickListener.onEditButtonClick(position);
                }
            }
        });
        holder.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                likeButtonClickListener.onCommentButtonClick(position);

            }
        });




        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (likeButtonClickListener != null) {
                    likeButtonClickListener.onLikeButtonClick(position);
                }
            }
        });

        if (posts != null) {
            final Post current = posts.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());


            if (current.getPic() !=0) {
                // If it's an integer, assume it's a drawable resource ID
                holder.ivPic.setImageResource((Integer) current.getPic());
            } else if (current.getuserpick()!=null) {
                // If it's a Drawable object
                holder.ivPic.setImageDrawable((Drawable) current.getuserpick());
                holder.ivPic.setVisibility(View.VISIBLE);
            }
            else {


            }
        }
    }


    public void setPosts (List<Post> s){
        posts = s;

        notifyDataSetChanged ();
    }

    @Override
    public int getItemCount() {
        if (posts != null)
            return posts.size();
        else return 0;
    }
    public List<Post> getPosts (){ return posts;
    }
}
