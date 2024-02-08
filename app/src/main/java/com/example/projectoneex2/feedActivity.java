package com.example.projectoneex2;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectoneex2.adapters.CommentListAdapter;
import com.example.projectoneex2.adapters.PostsListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class feedActivity extends AppCompatActivity implements PostsListAdapter.OnLikeButtonClickListener,PostsListAdapter.OnEditButtonClickListener,CommentListAdapter.commentActionsListener {
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int EDIT_POST_REQUEST = 3;
    private Button btnAdd;
    boolean share;
    private EditText editPost;
    private ImageView imageViewProfile;
    private Bitmap selectedBitmap;

    private List<Post> posts;
    int position;
    private PostsListAdapter adapter;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        imageViewProfile = findViewById(R.id.imageButtona);
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts=findViewById(R.id.lstPosts);
        final PostsListAdapter adapter= new PostsListAdapter(this);
        this.adapter=adapter;
        adapter.setOnLikeButtonClickListener(this);
        adapter.setOnEditButtonClickListener(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        username = getIntent().getStringExtra("USERNAME");
        posts=new ArrayList<>();
        imagePost post=new imagePost("Rona","nice sun",R.drawable.ic_action_name);
        post.addComment(new Comment("ori","not that good"));
        post.addComment(new Comment("ALICE","NO!,i love it!"));
        posts.add(post);
        posts.add(new imagePost("Alice","wonderul day!",R.drawable.ic_avatat));
        posts.add(new imagePost("Danni","go Lakers!!!!",R.drawable.ic_action_name));
        posts.add(new imagePost("Ori","miss home...",R.drawable.ic_action_name));
        posts.add(new imagePost("Shimon","so funny lol",R.drawable.ic_action_name));
        adapter.setPosts(posts);
        editPost=findViewById(R.id.edtWhatsOnYourMindMiddle);
        btnAdd = findViewById(R.id.button3);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost(adapter,posts);
            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share=true;
                openGallery();
            }
        });

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do nothing here, as you want it to do nothing when refreshed
                refreshLayout.setPressed(false);
                return;// Stop the refreshing indicator
            }
        });

    }
    public void onLikeButtonClick(int position) {
        // Handle like button click for the post at the given position
        // For example:
        Post post = posts.get(position);
        int currentLikes = post.getLikes();
        if (post.getLike()==false) {
            post.setLikes(currentLikes + 1);
            post.setLike(true);
            TextView likeCounterTextView = findViewById(R.id.likeCounter);
            likeCounterTextView.setText(String.valueOf(post.getLikes()) + " Likes");
            return;

        }
        if (post.getLike()==true) {
            post.setLikes(currentLikes - 1);
            post.setLike(false);
            TextView likeCounterTextView = findViewById(R.id.likeCounter);
            likeCounterTextView.setText(String.valueOf(post.getLikes()) + " Likes");
            return;
        }

    }

    private void addPost(PostsListAdapter adapter, List<Post> posts) {
        if (selectedBitmap!=null) {
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(selectedBitmap, 2000, 1200, true);
            imageViewProfile.setImageBitmap(resizedBitmap);
        }
        String post = editPost.getText().toString();
        Drawable pic=imageViewProfile.getDrawable();
        if (!share){
            posts.add(0,new imagePost(username,post,R.drawable.ic_white_foreground));
            adapter.notifyDataSetChanged();
            imageViewProfile.setImageResource(R.drawable.ic_photo_foreground);
            return;
        }
        posts.add(0,new imagePost(username,post,pic));
        adapter.notifyDataSetChanged();
        imageViewProfile.setImageResource(R.drawable.ic_photo_foreground);

    }



    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }






    @Override
    public void onEditButtonClick(int position) {
        showEditDialog(position);

    }
    public void onCommentButtonClick(int position) {
        showCommentDialog(position);

    }
    private void showCommentDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.comment_dialog, null);
        builder.setView(dialogView);
        RecyclerView lstComments = dialogView.findViewById(R.id.dialogRecyclerView);
        SwipeRefreshLayout refreshLayout = dialogView.findViewById(R.id.dialogRefreshLayout);
        final CommentListAdapter adapter1 = new CommentListAdapter(this,position);
        lstComments.setAdapter(adapter1);
        lstComments.setLayoutManager(new LinearLayoutManager(this));
        // Get the list of comments for the selected post and set it to the adapter
        List<Comment> comments = adapter.getPosts().get(position).getComments();
        adapter1.setComments(comments);
        adapter1.setOnEditButtonClickListener(this);
        AlertDialog dialog = builder.create();
        dialog.show();
        FloatingActionButton exit=dialogView.findViewById(R.id.floatingActionButton4);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
        FloatingActionButton add=dialogView.findViewById(R.id.floatingActionButton5);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(position,adapter1);
            }
        });




    }
    private void showAddDialog(final int position, CommentListAdapter adapter1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_post, null);
        builder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String updatedContent = editText.getText().toString().trim();
                // Update the post
                Post post = posts.get(position);
                post.addComment(new Comment(username,updatedContent));
                adapter1.notifyDataSetChanged();

            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().hide();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Define a method to show the edit dialog
    private void showEditDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_post, null);
        builder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);
        // Set initial text if needed
        editText.setText(posts.get(position).getContent());

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String updatedContent = editText.getText().toString().trim();
                // Update the post
                Post post = posts.get(position);
                post.setContent(updatedContent);
                // Refresh the UI
                adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel editing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
    public void onCommentEditButtonClick( int position, int postPosition, CommentListAdapter adapter1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_post, null);
        builder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);
        // Set initial text if needed
        editText.setText(posts.get(postPosition).getComments().get(position).getContent());
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String updatedContent = editText.getText().toString().trim();
                // Update the post
                Post post = posts.get(postPosition);
                post.getComments().get(position).setContent(updatedContent);
                adapter1.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel editing
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public void onCommentDeleteButtonClick(int position, int postPosition, CommentListAdapter adapter1) {
        Post post = posts.get(postPosition);
        post.getComments().remove(position);
        adapter1.notifyDataSetChanged();
    }

    @Override
    public void onCommentLikeButtonClick(int position, int postPosition, CommentListAdapter adapter1, TextView likeCounter) {
        Post post = posts.get(postPosition);
        int likes = post.getComments().get(position).getLikes();
        if (post.getComments().get(position).getLike()==false) {
            post.getComments().get(position).setLikes(likes + 1);
            likeCounter.setText(String.valueOf(post.getComments().get(position).getLikes()) + " Likes");
            adapter1.notifyDataSetChanged();
            post.getComments().get(position).setLike(true);
        }
        else {
            post.getComments().get(position).setLikes(likes - 1);
            likeCounter.setText(String.valueOf(post.getComments().get(position).getLikes()) + " Likes");
            adapter1.notifyDataSetChanged();
            post.getComments().get(position).setLike(false);
        }

    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            Uri selectedImageUri = data.getData();

            try {
                // Convert the URI to a Bitmap
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                selectedBitmap = originalBitmap;
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 400, 400, true);
                imageViewProfile.setImageBitmap(resizedBitmap);


                // Set the resized image to your ImageView (assuming imageViewProfile is your ImageView)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}