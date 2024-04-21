package com.example.projectoneex2;

import static com.example.projectoneex2.ImagePost.stringToDrawable;
import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.CursorWindow;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectoneex2.adapters.CommentListAdapter;
import com.example.projectoneex2.adapters.PostsListAdapter;
import com.example.projectoneex2.api.PostAPI;
import com.example.projectoneex2.viewmodel.PostsViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
//activity for the feed

public class FeedActivity extends AppCompatActivity implements PostsListAdapter.PostActionListener, CommentListAdapter.CommentActionsListener {
    private static final int REQUEST_IMAGE_PICK = 2;
    private PostsViewModel viewModel;
    boolean share;
    private EditText editPost;
    private ImageView imageViewProfile;
    private Bitmap selectedBitmap;
    private ImageButton editImage;
    private  List<ImagePost> posts;
    private PostsListAdapter adapter;
    int position;
    public static String currentId;
    public static String currentProfilePic;
    public static String currentNickname;
    private String nickname;
    private String username;
    private AlertDialog dialog;
    private Drawable d;
    public static String userId;
    public static AppDB db;
    private String token;
    private Boolean update=false;
    private ImagePostDao postDao    ;
    private List<User> Friends;
    PostAPI postAPI;
    public static MutableLiveData<String> banned=new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.token = getIntent().getStringExtra("TOKEN_KEY");
        this.username = getIntent().getStringExtra("USERNAME_KEY");
        //making sure the cursor window is big enough to handle the data(images)
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 100 * 1024 * 1024);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //room database setup for the posts
        db= Room.databaseBuilder(getApplicationContext(), AppDB.class, "posts").allowMainThreadQueries().fallbackToDestructiveMigration().build();
        postDao=db.imagePostDao();
        //theme operation(darkmode if needed)
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_feed);
        viewModel=new ViewModelProvider(this).get(PostsViewModel.class);
        viewModel.setToken(token);
        //getting the user id
        viewModel.getUserId(token) ;
        ImageView imageViewPic = findViewById(R.id.imageViewPic);
        //observing the post list so our post will be updated auto
        viewModel.getPosts(token).observe(this, imagePosts -> {
            //need to update the adapter
            if (update==true){
                viewModel.reload(token);
                update=false;
            }
            //setting the posts
            adapter.setPosts(imagePosts);
            adapter.notifyDataSetChanged();
            //deleting the old posts
            postDao.deleteAllRecords(username);
            for (ImagePost post:imagePosts){
               post.setDaoID(username);
            }
            //inserting the new posts with username
            postDao.insertImagePost(imagePosts);
            imageViewPic.setImageDrawable(stringToDrawable(Login.profilePic));
            //refresh the dialog if its open
            if (dialog!=null){
                dialog.hide();
            }
            if (dialog!=null){
                showCommentDialog(position);
            }
        });
        //observing the banned status
        banned.observe(this, s -> {
            if (s!=null&&s.equals("banned")) {
               Toast.makeText(this,"this  link is banned",Toast.LENGTH_SHORT).show();
                banned.postValue("not banned");
            }
        });

        //profile pic of the user
        imageViewProfile = findViewById(R.id.imageButtona);
        ImageButton menuButton = findViewById(R.id.menuButton);
        ToggleButton darkModeToggle = findViewById(R.id.toggleButton3);
        //marking darkbutton state
        darkModeToggle.setChecked(isDarkTheme);
        //only 1 user therefore the first one
        //layout for the posts
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        //adapter for the posts
        final PostsListAdapter adapter = new PostsListAdapter(this);
        this.adapter = adapter;
        //so we can preform on click actions from the post
        adapter.setPostActionListener(this);
        //setting the posts by the username
        adapter.setPosts(postDao.indexById(username));
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        //initializing view
        editPost = findViewById(R.id.edtWhatsOnYourMindMiddle);
        Button btnAdd = findViewById(R.id.button3);
        ImageButton btnSearch = findViewById(R.id.searchButton);
        //adding post
        btnAdd.setOnClickListener(view -> addPost(adapter, posts));
        menuButton.setOnClickListener(view -> openMenu());
        darkModeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            //changing dark mod situation
            isDarkTheme = isChecked;
            saveThemePreference();
            recreate(); // Restart activity to apply theme changes
        });

        btnSearch.setOnClickListener(view -> search());
        imageViewProfile.setOnClickListener(view -> {
            //user uploaded a photo
            openGallery();
        });

        refreshLayout.setOnRefreshListener(() -> {
           //refreshing the posts
            viewModel.reload(token);
            //stop refreshing
            refreshLayout.setRefreshing(false);
        });

    }

    // Define a method to open the menu activity
    private void openMenu() {
        Intent i = new Intent(this, Menu.class);
        i.putExtra("NICK",Login.nickname);
        i.putExtra("TOKEN_KEY", token);
        startActivity(i);
    }

    // Define a method to load the theme preference from SharedPreferences
    private void loadThemePreference() {
        // Get SharedPreferences instance
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // Load the theme preference (default is light theme)
        isDarkTheme = sharedPreferences.getBoolean(PREF_THEME_KEY, false);
    }
    // Define a method to apply the selected theme
    private void applyTheme() {
        // Apply the selected theme based on the theme preference
        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }

    // Define a method to save the theme preference to SharedPreferences
    private void saveThemePreference() {
        // Save the current theme preference
        sharedPreferences.edit().putBoolean(PREF_THEME_KEY, isDarkTheme).apply();
    }
    // Define a method to initiate a search
    private void search() {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the LayoutInflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the dialog_edit_comment layout which is like the search
        View dialogView = inflater.inflate(R.layout.dialog_edit_comment, null);
        EditText e=dialogView.findViewById(R.id.editText);
        e.setHint("search");
        // Set the custom layout to the dialog builder
        builder.setView(dialogView);
        // Set negative button (Cancel) click listener to hide the dialog
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> builder.create().hide());
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    // Method called when the profile picture is clicked to open the profile
    @Override
    public void onPictureClick(int position) {
        currentProfilePic=adapter.getPosts().get(position).getAuthorPic();
        currentNickname=adapter.getPosts().get(position).getAuthor();
        currentId=adapter.getPosts().get(position).getPostOwnerID();
        Intent i = new Intent(this, Profile.class);
        i.putExtra("TOKEN_KEY", token);
        i.putExtra("USERNAME_KEY", adapter.getPosts().get(position).getAuthor() );
        i.putExtra("ID",adapter.getPosts().get(position).getPostOwnerID());
        startActivity(i);

    }

    // Method called when the like button is clicked for a post
    public void onLikeButtonClick(int position) {
        // Get the post at the specified position
         ImagePost post=adapter.getPosts().get(position);
        viewModel.like(post,token);
        viewModel.reload(token);
    }

    // Define a method to add a new post
    private void addPost(PostsListAdapter adapter, List<ImagePost> posts) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Construct a string with hours and minutes
        String timeString = String.format("%02d:%02d", hour, minute);
        // Set the image view with the selected bitmap (if available)
        if (selectedBitmap != null) {
            imageViewProfile.setImageBitmap(selectedBitmap);
        }
        // Get the post text from the edit text field
        String postText = editPost.getText().toString();
        // Check if the post text is empty
        if (TextUtils.isEmpty(postText)) {
            // If the post text is empty, show a toast message and return
            Toast.makeText(this, "Please write something before posting", Toast.LENGTH_SHORT).show();
            return;
        }
        // Get the drawable from the image view
        Drawable postImage = imageViewProfile.getDrawable();
        // Check if it's a shared post or not
        if (!share) {
            // If not a shared post(no pic), add a new post with the username, post content, and a placeholder image
            // Set a default empty image
            imageViewProfile.setImageResource(R.drawable.ic_white_foreground);
            Drawable empty = imageViewProfile.getDrawable();
            // Set bounds to 0 to hide the image
            empty.setBounds(0, 0, 0, 0);
            String emtypic=drawableToString(empty);
            // Create a new post object with the username, post content, and empty image
            ImagePost newPost = new ImagePost(nickname, postText, emtypic, emtypic, timeString);
            newPost.setDaoID(username);
            // Add the new post to the list of posts
            viewModel.add(newPost,token);
            // Insert the new post to the database
            postDao.insertImagePost(Collections.singletonList(newPost));
        } else {
            Drawable empty = imageViewProfile.getDrawable();
            // Set bounds to 0 to hide the image
            String emtypic=drawableToString(empty);
            // If it's a shared post, add a new post with the username, post content, and selected image
            ImagePost newPost = new ImagePost(nickname, postText, drawableToString(postImage), emtypic, timeString);
            newPost.setDaoID(username);
            // Add the new post to the list of posts
            viewModel.add(newPost,token);
            // Insert the new post to the database
            postDao.insertImagePost(Collections.singletonList(newPost));
           // Reset the share flag
            share = false;
        }
        update=true;
        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
        // Reset the image view to the default placeholder
        imageViewProfile.setImageResource(R.drawable.ic_photo_foreground);
        editPost.setHint("what's on your mind?");
        editPost.setText("");
    }
    // Method to convert a drawable to a string
    public static String drawableToString(Drawable drawable) {
        Bitmap bitmap;
        if (drawable instanceof BitmapDrawable) {
            bitmap = ((BitmapDrawable) drawable).getBitmap();
        } else if (drawable instanceof VectorDrawable) {
            bitmap = Bitmap.createBitmap(
                    drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(),
                    Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
        } else {
            // Handle other types of drawables or return null if not supported
            return null;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] bytes = outputStream.toByteArray();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
    // Define a method to open the gallery for image selection
    private void openGallery() {
        // Create an intent to pick an image from the gallery
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // Start the activity to select an image and expect a result
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    // Define a method to show a toast indicating the user can't edit posts that are not theirs
    private void showToast() {
        Toast.makeText(this, "You can't edit posts that are not yours!", Toast.LENGTH_SHORT).show();
    }
    private void showEditToast() {
        Toast.makeText(this, "You can't edit comments that are not yours!", Toast.LENGTH_SHORT).show();
    }
    // Method called when the edit button is clicked for a post
    @Override
    public void onEditButtonClick(int position) {
        // Get the post at the specified position
        ImagePost post = adapter.getPosts().get(position);
        if (post.getPostOwnerID().equals(userId)){
            showEditDialog(position);
        }
        else {
           showToast();
        }
    }

    // Method called when the share button is clicked for a post
    @Override
    public void onShareButtonClick() {
        openMenu();
    }

    // Method called when the comment button is clicked for a post
    public void onCommentButtonClick(int position) {
        this.position=position;
        showCommentDialog(position);
    }

    // Method called when the delete button is clicked for a post
    @Override
    public void onDeletsButtonClick(int position, PostsListAdapter adapter) {
        if (!FeedActivity.userId.equals(adapter.getPosts().get(position).getPostOwnerID())){
            showToastDelete();
            return;
        }
        // delete the post
        viewModel.delete(adapter.getPosts().get(position), token);
        viewModel.reload(token);
        // Notify the adapter that the data set has changed
        postDao.deleteImagePost(adapter.getPosts().get(position));
    }
    // Method to show a toast indicating the user can't delete posts that are not theirs
    private void showToastDelete() {
        Toast.makeText(this, "you cant delete posts that are not yours!", Toast.LENGTH_SHORT).show();
    }
    // Define a method to show the comment dialog for a post at a given position
    private void showCommentDialog(int position) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the LayoutInflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the comment_dialog layout
        View dialogView = inflater.inflate(R.layout.comment_dialog, null);
        // Set the custom layout to the dialog builder
        builder.setView(dialogView);
        // Find the RecyclerView and SwipeRefreshLayout in the custom layout
        RecyclerView lstComments = dialogView.findViewById(R.id.dialogRecyclerView);
        SwipeRefreshLayout refreshLayout = dialogView.findViewById(R.id.dialogRefreshLayout);
        // Create a new CommentListAdapter for the dialog RecyclerView
        final CommentListAdapter adapter1 = new CommentListAdapter(this, position);
        lstComments.setAdapter(adapter1);
        lstComments.setLayoutManager(new LinearLayoutManager(this));
        // Get the list of comments for the selected post and set it to the adapter
        List<Comment> comments = adapter.getPosts().get(position).getCommentsList();
        adapter1.setComments(comments);
        // Set an edit button click listener for the adapter
        adapter1.setOnEditButtonClickListener(this);
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        this.dialog=dialog;
        dialog.show();
        // Find the FloatingActionButton for exiting the dialog
        FloatingActionButton exit = dialogView.findViewById(R.id.floatingActionButton4);
        // Set click listener for exiting the dialog
        exit.setOnClickListener(view -> {
            // Refresh the main adapter
            adapter.notifyDataSetChanged();
            // Hide the dialog
            this.dialog=null;
            dialog.hide();
        });
        // Find the FloatingActionButton for adding a comment
        FloatingActionButton add = dialogView.findViewById(R.id.floatingActionButton5);
        // Set click listener for adding a comment
        add.setOnClickListener(view -> showAddCommentDialog(position, adapter1));
        // Set refresh listener for the SwipeRefreshLayout
        refreshLayout.setOnRefreshListener(() -> {
            // Stop the refreshing indicator
            refreshLayout.setRefreshing(false);
        });
    }
    // Define a method to show the add comment dialog for a post at a given position
    private void showAddCommentDialog(final int position, CommentListAdapter adapter1) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the LayoutInflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the dialog_edit_comment layout
        View dialogView = inflater.inflate(R.layout.dialog_edit_comment, null);
        // Set the custom layout to the dialog builder
        builder.setView(dialogView);
        // Find the EditText in the custom layout
        final EditText editText = dialogView.findViewById(R.id.editText);
        editText.setHint("type your comment");
        // Set positive button (Save) click listener
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get the updated content from the EditText
                String updatedContent = editText.getText().toString().trim();
                // Get the post object at the specified position
                ImagePost post = adapter.getPosts().get(position);
                // Add a new comment to the post with provided content, nickname, and profile image
                Comment c=new Comment(nickname, updatedContent,"aa");
                // add the comment to the post
                viewModel.addComment(post.get_id(),c,token);
            }
        });

        // Set negative button (Cancel) click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Hide the dialog
                builder.create().hide();
            }
        });
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Define a method to show the edit dialog for a post at a given position
    private void showEditDialog(final int position) {
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the LayoutInflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the dialog_edit_post layout
        View dialogView = inflater.inflate(R.layout.dialog_edit_post, null);
        // Set the custom layout to the dialog builder
        builder.setView(dialogView);
        // Find the EditText in the custom layout
        final EditText editText = dialogView.findViewById(R.id.editText);
        // Set initial text in the EditText
        editText.setText(adapter.getPosts().get(position).getContent());
        // Find the ImageButton for editing image
        editImage = dialogView.findViewById(R.id.imageButton);
        // Set click listener for editing image button
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open gallery for selecting an image
                openGallery();
            }
        });
        // Set positive button (Save) click listener
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get the updated content from the EditText
                String updatedContent = editText.getText().toString().trim();
                // Get the post object at the specified position
                ImagePost post = adapter.getPosts().get(position);
                // Update the content of the post
                post.setContent(updatedContent);
                // Set a temporary ID (-1 indicates an user image post)
                post.setPicID(-1);
                // Set the user profile picture to the selected image
                if(share==true) {
                    if (d != null) {
                        post.setUserpicDraw(d);
                    }
                }
                imageViewProfile.setImageResource(R.drawable.ic_photo_foreground);
                //edit the post
                viewModel.editPost(post,token);
                // Update the post in the database
                postDao.updateImagePost(post);
                update=true;
                // Refresh the UI by notifying the adapter of the data change
                adapter.notifyDataSetChanged();
            }
        });

        // Set negative button (Cancel) click listener
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Cancel editing
            }
        });

        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //method to edit a comment
    @Override
    public void onCommentEditButtonClick(int position, int postPosition, CommentListAdapter adapter1) {
        if (!FeedActivity.userId.equals(adapter.getPosts().get(postPosition).getCommentsList().get(position).getCommentOwnerID())){
            showEditToast();
            return;
        }
        // Create an AlertDialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Get the LayoutInflater
        LayoutInflater inflater = getLayoutInflater();
        // Inflate the dialog_edit_comment layout
        View dialogView = inflater.inflate(R.layout.dialog_edit_comment, null);
        // Set the custom layout to the dialog builder
        builder.setView(dialogView);
        // Find the EditText in the custom layout
        final EditText editText = dialogView.findViewById(R.id.editText);
        // Set initial text in the EditText
        editText.setText(adapter.getPosts().get(postPosition).getCommentsList().get(position).getContent());
        // Set positive button (Save) click listener
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get the updated content from the EditText
                String updatedContent = editText.getText().toString().trim();
                // Get the post object at the specified position
                ImagePost post = adapter.getPosts().get(postPosition);
                //edit the comment
                viewModel.editComment(post.get_id(),post.getCommentsList().get
                        (position).getId(),updatedContent,token);
                // Update the content of the comment
                postDao.updateImagePost(post);
                // Notify the adapter of the data change
                adapter1.notifyDataSetChanged();
            }
        });
        // Set negative button (Cancel) click listener
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            // Cancel editing
        });
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    //method to delete a comment
    @Override
    public void onCommentDeleteButtonClick(int position, int postPosition, CommentListAdapter adapter1) {
        if (!FeedActivity.userId.equals(adapter.getPosts().get(postPosition).getCommentsList().get(position).getCommentOwnerID())){
            showToastDelete();
            return;
        }
        // Get the post object at the specified position
        String postID = adapter.getPosts().get(postPosition).get_id();
        ImagePost post = adapter.getPosts().get(postPosition);
        // Remove the comment at the specified position from the post's comments list
        String commentID=adapter.getPosts().get(postPosition).getCommentsList().get(position).getId();
        //delete the comment
        viewModel.deleteComment(postID,commentID,token);
        // Notify the adapter of the data change
        adapter1.notifyDataSetChanged();
    }
    //method to like a comment
    public void onCommentLikeButtonClick(int position, int postPosition, CommentListAdapter adapter1, TextView likeCounter) {
        // Get the post object at the specified position
        ImagePost post = adapter.getPosts().get(postPosition);
        // Get the number of likes for the comment at the specified position
        int likes = post.getCommentsList().get(position).getLikes();
        // Check if the comment is currently not liked
        if (!post.getCommentsList().get(position).getLike()) {
            // Increment the number of likes for the comment
            post.getCommentsList().get(position).setLikes(likes + 1);
            // Update the like counter TextView with the new number of likes
            likeCounter.setText(post.getCommentsList().get(position).getLikes() + " Likes");
            // Notify the adapter that the data has changed
            adapter1.notifyDataSetChanged();
            // Set the comment as liked
            post.getCommentsList().get(position).setLike(true);
        } else { // If the comment is currently liked
            // Decrement the number of likes for the comment
            post.getCommentsList().get(position).setLikes(likes - 1);
            // Update the like counter TextView with the new number of likes
            likeCounter.setText(post.getCommentsList().get(position).getLikes() + " Likes");
            adapter1.notifyDataSetChanged();
            post.getCommentsList().get(position).setLike(false);
        }
        adapter.notifyDataSetChanged();
        // Perform your API call here to update the server with the like status change
        viewModel.commentLike(post.get_id(), post.getCommentsList().get(position).getId(), token);

    }
    //method to like a post
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check if the request is for picking an image and if the result is OK
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // Get the URI of the selected image
            Uri selectedImageUri = data.getData();

            try {
                // Convert the URI to a Bitmap
                Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                // Set the selectedBitmap to the originalBitmap
                selectedBitmap = originalBitmap;
                // Convert the Bitmap to a Drawable
                this.d = bitmapToDrawable(selectedBitmap);
                // Resize the originalBitmap and set it to imageViewProfile
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 400, 400, true);
                imageViewProfile.setImageBitmap(resizedBitmap);
                // Resize the originalBitmap again and set it to editImage (if it exists)
                resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 600, 400, true);
                if (editImage != null) {
                    editImage.setImageBitmap(resizedBitmap);
                }
                //mark that the user upload a photo
                share = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Method to convert Bitmap to Drawable
    private Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(getResources(), bitmap);
    }


}