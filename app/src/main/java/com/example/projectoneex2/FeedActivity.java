package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;
import static com.example.projectoneex2.Login.userList;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.projectoneex2.adapters.CommentListAdapter;
import com.example.projectoneex2.adapters.PostsListAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class FeedActivity extends AppCompatActivity implements PostsListAdapter.PostActionListener, CommentListAdapter.CommentActionsListener {
    private static final int REQUEST_IMAGE_PICK = 2;
    boolean share;
    private EditText editPost;
    private ImageView imageViewProfile;
    private Bitmap selectedBitmap;
    private ImageButton editImage;
    private static List<Post> posts;
    private PostsListAdapter adapter;
    private String nickname;
    private Drawable d;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //theme operation(darkmode if needed)
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_feed);
        //profile pic of the user
        imageViewProfile = findViewById(R.id.imageButtona);
        ImageView imageViewPic = findViewById(R.id.imageViewPic);
        ImageButton menuButton = findViewById(R.id.menuButton);
        ToggleButton darkModeToggle = findViewById(R.id.toggleButton3);
        //marking darkbutton state
        darkModeToggle.setChecked(isDarkTheme);
        //only 1 user therefore the first one
        imageViewPic.setImageBitmap(userList.get(0).getProfileImage());
        //layout for the posts
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        //adapter for the posts
        final PostsListAdapter adapter = new PostsListAdapter(this);
        this.adapter = adapter;
        //so we can preform on click actions from the post
        adapter.setPostActionListener(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        //only one user at lsit
        nickname = userList.get(0).getNickname();
        //create the 10 defualt posts
        if (posts==null){
        initPosts();}
        adapter.setPosts(posts);
        //new post upload
        editPost = findViewById(R.id.edtWhatsOnYourMindMiddle);
        Button btnAdd = findViewById(R.id.button3);
        ImageButton btnSearch = findViewById(R.id.searchButton);
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
            // Stop the refreshing indicator
            refreshLayout.setRefreshing(false);
        });

    }

    // Define a method to open the menu activity
    private void openMenu() {
        Intent i = new Intent(this, Menu.class);
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

    // Define a method to initialize posts from a JSON file
    private void initPosts() {
        try {
            // Open the JSON file containing posts
            InputStream inputStream = getAssets().open("posts.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            // Read each line of the JSON file
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStream.close();
            // Convert JSON data to string
            String jsonData = stringBuilder.toString();
            // Create a JSONArray from the JSON string
            JSONArray jsonArray = new JSONArray(jsonData);
            // Initialize the list of posts
            posts = new ArrayList<>();
            // Iterate through the JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                // Get each JSON object from the array
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                // Extract data from JSON object
                String author = jsonObject.getString("author");
                String content = jsonObject.getString("content");
                String time = jsonObject.getString("time");
                // Get the resource IDs for image and profile picture
                int imageResourceId = getResources().getIdentifier(jsonObject.getString("imageResourceId"), "drawable", getPackageName());
                int profilePic = getResources().getIdentifier(jsonObject.getString("profilePic"), "drawable", getPackageName());
                // Create a Post object with the extracted data
                Post new_post = new ImagePost(author, content, imageResourceId, profilePic, time);
                // Add the Post object to the list
                posts.add(new_post);
            }
            // Now you have a list of Post objects parsed from the JSON data.
            // You can use this list in your app as needed.
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    // Method called when the like button is clicked for a post
    public void onLikeButtonClick(int position) {
        // Get the post at the specified position
        Post post = posts.get(position);
        // Get the current number of likes for the post
        int currentLikes = post.getLikes();
        // If the post is not liked
        if (!post.getLike()) {
            // Increment the number of likes
            post.setLikes(currentLikes + 1);
            // Set the post as liked
            post.setLike(true);
        } else { // If the post is already liked
            // Decrement the number of likes
            post.setLikes(currentLikes - 1);
            // Set the post as not liked
            post.setLike(false);
        }
        // Update the like counter TextView with the new number of likes
        TextView likeCounterTextView = findViewById(R.id.likeCounter);
        likeCounterTextView.setText(post.getLikes() + " Likes");
    }

    // Define a method to add a new post
    private void addPost(PostsListAdapter adapter, List<Post> posts) {
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
            // Create a new post object with the username, post content, and empty image
            ImagePost newPost = new ImagePost(nickname, postText, empty, userList.get(0).getProfileImage(), timeString);
            // Set the author's profile picture
            newPost.setAuthorPic(userList.get(0).getProfileImage());
            // Add the new post at the beginning of the list
            posts.add(0, newPost);
        } else {
            // If it's a shared post, add a new post with the username, post content, and selected image
            ImagePost newPost = new ImagePost(nickname, postText, postImage, userList.get(0).getProfileImage(), timeString);
            // Add the new post at the beginning of the list
            posts.add(0, newPost);
            // Reset the share flag
            share = false;
        }
        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();
        // Reset the image view to the default placeholder
        imageViewProfile.setImageResource(R.drawable.ic_photo_foreground);
        editPost.setHint("what's on your mind?");
        editPost.setText("");
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

    // Method called when the edit button is clicked for a post
    @Override
    public void onEditButtonClick(int position) {
        // Get the post at the specified position
        Post post = posts.get(position);
        // Check if the post author is the current user
        if (!Objects.equals(post.getAuthor(), userList.get(0).getNickname())) {
            // If not, show a toast indicating the user can't edit the post
            showToast();
            return;
        }
        // If the current user is the author, show the edit dialog
        showEditDialog(position);
    }

    // Method called when the share button is clicked for a post
    @Override
    public void onShareButtonClick() {
        openMenu();
    }

    // Method called when the comment button is clicked for a post
    public void onCommentButtonClick(int position) {
        showCommentDialog(position);
    }

    // Method called when the delete button is clicked for a post
    @Override
    public void onDeletsButtonClick(int position, PostsListAdapter adapter) {
        // Remove the post at the specified position
        posts.remove(position);
        // Notify the adapter of the data change
        adapter.notifyDataSetChanged();
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
        List<Comment> comments = adapter.getPosts().get(position).getComments();
        adapter1.setComments(comments);
        // Set an edit button click listener for the adapter
        adapter1.setOnEditButtonClickListener(this);
        // Create and show the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
        // Find the FloatingActionButton for exiting the dialog
        FloatingActionButton exit = dialogView.findViewById(R.id.floatingActionButton4);
        // Set click listener for exiting the dialog
        exit.setOnClickListener(view -> {
            // Refresh the main adapter
            adapter.notifyDataSetChanged();
            // Hide the dialog
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
                Post post = posts.get(position);
                // Add a new comment to the post with provided content, nickname, and profile image
                post.addComment(new Comment(nickname, updatedContent, userList.get(0).getProfileImage()));
                // Refresh the UI by notifying the adapter of the data change
                adapter1.notifyDataSetChanged();
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
        editText.setText(posts.get(position).getContent());
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
                Post post = posts.get(position);
                // Update the content of the post
                post.setContent(updatedContent);
                // Set a temporary ID (-1 indicates an user image post)
                post.setId(-1);
                // Set the user profile picture to the selected image
                post.setUserPic(d);
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
    @Override
    public void onCommentEditButtonClick(int position, int postPosition, CommentListAdapter adapter1) {
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
        editText.setText(posts.get(postPosition).getComments().get(position).getContent());
        // Set positive button (Save) click listener
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Get the updated content from the EditText
                String updatedContent = editText.getText().toString().trim();
                // Get the post object at the specified position
                Post post = posts.get(postPosition);
                // Update the content of the comment at the specified position
                post.getComments().get(position).setContent(updatedContent);
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
    @Override
    public void onCommentDeleteButtonClick(int position, int postPosition, CommentListAdapter adapter1) {
        // Get the post object at the specified position
        Post post = posts.get(postPosition);
        // Remove the comment at the specified position from the post's comments list
        post.getComments().remove(position);
        // Notify the adapter of the data change
        adapter1.notifyDataSetChanged();
    }

    public void onCommentLikeButtonClick(int position, int postPosition, CommentListAdapter adapter1, TextView likeCounter) {
        // Get the post object at the specified position
        Post post = posts.get(postPosition);
        // Get the number of likes for the comment at the specified position
        int likes = post.getComments().get(position).getLikes();
        // Check if the comment is currently not liked
        if (!post.getComments().get(position).getLike()) {
            // Increment the number of likes for the comment
            post.getComments().get(position).setLikes(likes + 1);
            // Update the like counter TextView with the new number of likes
            likeCounter.setText(post.getComments().get(position).getLikes() + " Likes");
            // Notify the adapter that the data has changed
            adapter1.notifyDataSetChanged();
            // Set the comment as liked
            post.getComments().get(position).setLike(true);
        } else { // If the comment is currently liked
            // Decrement the number of likes for the comment
            post.getComments().get(position).setLikes(likes - 1);
            // Update the like counter TextView with the new number of likes
            likeCounter.setText(post.getComments().get(position).getLikes() + " Likes");
            adapter1.notifyDataSetChanged();
            post.getComments().get(position).setLike(false);
        }
    }

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