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
import android.widget.CompoundButton;
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

public class feedActivity extends AppCompatActivity implements PostsListAdapter.PostActionListener, CommentListAdapter.commentActionsListener {
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 3;
    private Button btnAdd;
    boolean share;
    private EditText editPost;
    private ImageView imageViewProfile;
    private ImageView imageViewPic;
    private Bitmap selectedBitmap;
    private ImageButton menuButton;
    private ImageButton editImage;
    private List<Post> posts;
    private PostsListAdapter adapter;
    private String username;
    private Drawable d;
    private ToggleButton darkModeToggle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_feed);
        imageViewProfile = findViewById(R.id.imageButtona);
        imageViewPic = findViewById(R.id.imageViewPic);
        menuButton = findViewById(R.id.menuButton);
        darkModeToggle = findViewById(R.id.toggleButton3);
        darkModeToggle.setChecked(isDarkTheme);
        imageViewPic.setImageBitmap(userList.get(0).getProfileImage());
        SwipeRefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        RecyclerView lstPosts = findViewById(R.id.lstPosts);
        final PostsListAdapter adapter = new PostsListAdapter(this);
        this.adapter = adapter;
        adapter.setPostActionListener(this);
        lstPosts.setAdapter(adapter);
        lstPosts.setLayoutManager(new LinearLayoutManager(this));
        username = getIntent().getStringExtra("USERNAME");
        initPosts();
        adapter.setPosts(posts);
        editPost = findViewById(R.id.edtWhatsOnYourMindMiddle);
        btnAdd = findViewById(R.id.button3);
        ImageButton btnSearch = findViewById(R.id.searchButton);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPost(adapter, posts);
            }
        });
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMenu();
            }
        });
        darkModeToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isDarkTheme = isChecked;
                saveThemePreference();
                recreate(); // Restart activity to apply theme changes
            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
            }
        });
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                share = true;
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

    private void openMenu() {
        Intent i = new Intent(this, Menu.class);
        startActivity(i);
    }
    private void loadThemePreference() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(PREF_THEME_KEY, false); // Default is light theme
    }
    private void applyTheme() {
        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }
    private void saveThemePreference() {
        sharedPreferences.edit().putBoolean(PREF_THEME_KEY, isDarkTheme).apply();
    }

    private void initPosts() {
        try {
            InputStream inputStream = getAssets().open("posts.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            bufferedReader.close();
            inputStream.close();

            String jsonData = stringBuilder.toString();
            JSONArray jsonArray = new JSONArray(jsonData);

            posts = new ArrayList<>();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                String author = jsonObject.getString("author");
                String content = jsonObject.getString("content");
                String time = jsonObject.getString("time");
                int imageResourceId = getResources().getIdentifier(jsonObject.getString("imageResourceId"), "drawable", getPackageName());
                int profilePic=getResources().getIdentifier(jsonObject.getString("profilePic"), "drawable", getPackageName());
                // Create a Post object with the extracted data
                Post new_post = new imagePost(author, content, imageResourceId,profilePic,time);

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

    private void search() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_post, null);
        builder.setView(dialogView);
        final EditText editText = dialogView.findViewById(R.id.editText);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().hide();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void onLikeButtonClick(int position) {
        // Handle like button click for the post at the given position
        // For example:
        Post post = posts.get(position);
        int currentLikes = post.getLikes();
        if (post.getLike() == false) {
            post.setLikes(currentLikes + 1);
            post.setLike(true);
            TextView likeCounterTextView = findViewById(R.id.likeCounter);
            likeCounterTextView.setText(String.valueOf(post.getLikes()) + " Likes");
            return;

        }
        if (post.getLike() == true) {
            post.setLikes(currentLikes - 1);
            post.setLike(false);
            TextView likeCounterTextView = findViewById(R.id.likeCounter);
            likeCounterTextView.setText(String.valueOf(post.getLikes()) + " Likes");
            return;
        }

    }

    private void addPost(PostsListAdapter adapter, List<Post> posts) {
        Calendar calendar = Calendar.getInstance();

        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        // Construct a string with hours and minutes
        String timeString = String.format("%02d:%02d", hour, minute);
        // Set the image view with the selected bitmap (if available)
        if (selectedBitmap != null) {
            imageViewProfile.setImageBitmap(selectedBitmap);
        }
        String post = editPost.getText().toString();
        if (TextUtils.isEmpty(post)) {
            // If the post text is empty, show a toast message and return
            Toast.makeText(this, "Please write something before posting", Toast.LENGTH_SHORT).show();
            return;
        }
        Drawable pic = imageViewProfile.getDrawable();
        // Check if it's a shared post or not
        if (!share) {
            // Add a new post with the username, post content, and a placeholder image
            // Assuming you have an ImageView instance named imageView
            imageViewProfile.setImageResource(R.drawable.ic_white_foreground);
            Drawable empty=imageViewProfile.getDrawable();
            imagePost p = new imagePost(username, post, empty,userList.get(0).getProfileImage(),timeString);
            p.setAuthorPic(userList.get(0).getProfileImage());
            posts.add(0, p);
        } else {
            imagePost p = new imagePost(username, post, pic,userList.get(0).getProfileImage(),timeString);
            posts.add(0, p);
        }

        // Notify the adapter that the data set has changed
        adapter.notifyDataSetChanged();

        // Reset the image view to the default placeholder
        imageViewProfile.setImageResource(R.drawable.ic_photo_foreground);
    }
    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }



    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onEditButtonClick(int position) {
        Post post = posts.get(position);
        if (!Objects.equals(post.getAuthor(), userList.get(0).getUsername())){
            showToast("you cant edit post that are not yours!");
            return;
        }
        showEditDialog(position);

    }
    public void onCommentButtonClick(int position) {
        showCommentDialog(position);

    }
    @Override
    public void onDeletsButtonClick(int position, PostsListAdapter adapter) {
        posts.remove(position);
        adapter.notifyDataSetChanged();
    }
    private void showCommentDialog(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.comment_dialog, null);
        builder.setView(dialogView);
        RecyclerView lstComments = dialogView.findViewById(R.id.dialogRecyclerView);
        SwipeRefreshLayout refreshLayout = dialogView.findViewById(R.id.dialogRefreshLayout);
        final CommentListAdapter adapter1 = new CommentListAdapter(this, position);
        lstComments.setAdapter(adapter1);
        lstComments.setLayoutManager(new LinearLayoutManager(this));
        // Get the list of comments for the selected post and set it to the adapter
        List<Comment> comments = adapter.getPosts().get(position).getComments();
        adapter1.setComments(comments);
        adapter1.setOnEditButtonClickListener(this);
        AlertDialog dialog = builder.create();
        dialog.show();
        FloatingActionButton exit = dialogView.findViewById(R.id.floatingActionButton4);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.notifyDataSetChanged();
                dialog.hide();
            }
        });
        FloatingActionButton add = dialogView.findViewById(R.id.floatingActionButton5);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAddDialog(position, adapter1);
            }
        });}
    private void openCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
        } else {
            Toast.makeText(this, "No camera app found", Toast.LENGTH_SHORT).show();
        }
    }


    private void showAddDialog(final int position, CommentListAdapter adapter1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_comment, null);
        builder.setView(dialogView);

        final EditText editText = dialogView.findViewById(R.id.editText);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String updatedContent = editText.getText().toString().trim();
                // Update the post
                Post post = posts.get(position);
                post.addComment(new Comment(username, updatedContent, userList.get(0).getProfileImage()));
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
        editImage = dialogView.findViewById(R.id.imageButton);
        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String updatedContent = editText.getText().toString().trim();
                Drawable pic = imageViewProfile.getDrawable();
                // Update the post
                Post post = posts.get(position);
                post.setContent(updatedContent);
                    post.setId(-1);
                    post.setUserpic(d);
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

    public void onCommentEditButtonClick(int position, int postPosition, CommentListAdapter adapter1) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_edit_comment, null);
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
        if (post.getComments().get(position).getLike() == false) {
            post.getComments().get(position).setLikes(likes + 1);
            likeCounter.setText(String.valueOf(post.getComments().get(position).getLikes()) + " Likes");
            adapter1.notifyDataSetChanged();
            post.getComments().get(position).setLike(true);
        } else {
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
                this.d = bitmapToDrawable(selectedBitmap);
                Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 400, 400, true);
                imageViewProfile.setImageBitmap(resizedBitmap);
                 resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, 600, 400, true);
                if (editImage!=null) {
                    editImage.setImageBitmap(resizedBitmap);
                }
                // Set the resized image to your ImageView (assuming imageViewProfile is your ImageView)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            // Handle camera capture
            Bundle extras = data.getExtras();
            Bitmap capturedBitmap = (Bitmap) extras.get("data");
            selectedBitmap = capturedBitmap;
            this.d = bitmapToDrawable(selectedBitmap);
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(capturedBitmap, 400, 400, true);
            imageViewProfile.setImageBitmap(resizedBitmap);
            resizedBitmap = Bitmap.createScaledBitmap(capturedBitmap, 600, 400, true);
            if (editImage!=null) {
                editImage.setImageBitmap(resizedBitmap);
            }
        }
    }
    private Drawable bitmapToDrawable(Bitmap bitmap) {
        return new BitmapDrawable(getResources(), bitmap);
    }

}