package com.example.projectoneex2.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ImageButton;
import static com.example.projectoneex2.MainActivity.userList;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projectoneex2.Comment;
import com.example.projectoneex2.R;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.PostViewHolder> {
    private commentActionsListener commentListener;
    int postPosition;
    CommentListAdapter adapter=this;

    public interface commentActionsListener {
        void onCommentEditButtonClick(int position, int postPosition, CommentListAdapter adapter);
        void onCommentDeleteButtonClick(int position, int postPosition, CommentListAdapter adapter);
        void onCommentLikeButtonClick(int position, int postPosition, CommentListAdapter adapter, TextView likeCounter);
    }
    class PostViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvAuthor;
        private final TextView tvContent;
        public  TextView likeCounter;
        public ImageView pic;
        public ImageButton likeButton;
        public ImageButton editButton;
        public ImageButton deleteButton;



        private PostViewHolder(View itemView) {
            super(itemView);
            likeCounter = itemView.findViewById(R.id.likeCounter1);
            tvAuthor = itemView.findViewById(R.id.authorName);
            tvContent = itemView.findViewById(R.id.commentText);
            pic=itemView.findViewById(R.id.imageViewPic);
            editButton=itemView.findViewById(R.id.imageButton6);
            deleteButton=itemView.findViewById(R.id.imageButton12);
            likeButton=itemView.findViewById(R.id.likeButton);
        }
    }


    private final LayoutInflater mInflater;
    private List<Comment> comments;

    public CommentListAdapter(Context context,int postPosition) {
        mInflater = LayoutInflater.from(context);
        this.postPosition=postPosition;
    }
    public void setOnEditButtonClickListener(commentActionsListener listener) {
        this.commentListener = listener;
    }

    @Override
    public PostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_comment, parent, false);
        return new PostViewHolder(itemView);
    }

    // Inside PostsListAdapter class
    public void onBindViewHolder(PostViewHolder holder, int position) {
        holder.likeCounter.setText(String.valueOf(comments.get(position).getLikes())+" Likes");
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentListener != null) {
                    commentListener.onCommentEditButtonClick(position,postPosition,adapter);
                }
            }
        });
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentListener != null) {
                    commentListener.onCommentDeleteButtonClick(position,postPosition,adapter);
                }
            }
        });
        holder.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentListener != null) {
                    commentListener.onCommentLikeButtonClick(position,postPosition,adapter,holder.likeCounter);
                }
            }
        });

        if (comments != null) {
            final Comment current = comments.get(position);
            holder.tvAuthor.setText(current.getAuthor());
            holder.tvContent.setText(current.getContent());
            holder.pic.setImageBitmap(current.getAuthorPic());

        }
    }


    public void setComments (List<Comment> s){
        comments = s;

        notifyDataSetChanged ();
    }

    @Override
    public int getItemCount() {
        if (comments != null)
            return comments.size();
        else return 0;
    }
    public List<Comment> getPosts (){ return comments;
    }
}
