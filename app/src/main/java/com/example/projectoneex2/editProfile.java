package com.example.projectoneex2;

import static com.example.projectoneex2.ImagePost.bitmapToString;
import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectoneex2.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
//  Activity for editing user profile
public class editProfile extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private UserViewModel viewModel=null;
    // Views
    private EditText editTextUsername;
    private EditText editTextNickname;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Bitmap pic;
    private ImageView imageViewProfile;
    private String token;
    public static MutableLiveData<String> indicatorE=new MutableLiveData<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.token = getIntent().getStringExtra("TOKEN_KEY");
        //dark mode setting
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_edit_profile);
        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNickname=findViewById(R.id.editTextNickname);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        ToggleButton darkModeToggle = findViewById(R.id.toggleButton);
        // Set the initial state of the dark mode toggle
        darkModeToggle.setChecked(isDarkTheme);
        // Set onClickListener for the signup button
        btnSignUp.setOnClickListener(view -> signupUser());
        // Set onClickListener for the profile image
        imageViewProfile.setOnClickListener(view -> openGallery());
        darkModeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkTheme = isChecked;
            saveThemePreference();
            recreate(); // Restart activity to apply theme changes
        });
    }

    // Method to handle user signup
    private void signupUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String nickname=editTextNickname.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();
        // Validate the input
        if (username.isEmpty() || password.isEmpty() || nickname.isEmpty() || repeatPassword.isEmpty()) {
            showToast("All fields are required");
            return;
        }
        if (pic == null) {
            showToast("Profile picture is required");
            return;
        }
        // Add the new user to the list
        User a=new User(username, password,this.pic,nickname);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.editUser(a,token,FeedActivity.userId);
        // Create a LiveData object to observe the result of the signup operation
        AtomicBoolean shownWaitMessage = new AtomicBoolean(false);
        indicatorE.postValue("wait");
        indicatorE.observe(this, s -> {
            if (s.equals("wait") && !shownWaitMessage.get()) {
                showToast("Please wait");
                shownWaitMessage.set(true);
            }
            if (s.equals("true")) {
                // Display a success message
                showToast("Edit successful");
                indicatorE.postValue("wait");
                Login.nickname=nickname;
                Login.profilePic=bitmapToString(pic);
                finish();
            }
            if (s.equals("false")) {
                showToast("Username already taken");
                indicatorE.postValue("wait");
            }
        });
    }

    // Method to display toast messages
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Method to open image gallery for selecting profile picture
    private void openGallery() {
        final CharSequence[] options = {"Choose from Gallery", "Take a Picture", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(editProfile.this);
        builder.setTitle("Choose Action");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            } else if (options[item].equals("Take a Picture")) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } else if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // Method to handle image selection from gallery or camera
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Handle the image selection from gallery or camera
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageViewProfile.setImageBitmap(bitmap);
                this.pic = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Handle the image capture from camera
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK && data != null) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = null;
            if (extras != null) {
                imageBitmap = (Bitmap) extras.get("data");
            }
            imageViewProfile.setImageBitmap(imageBitmap);
            this.pic = imageBitmap;
        }
    }

    // Method to load theme preference from SharedPreferences
    private void loadThemePreference() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(PREF_THEME_KEY, false); // Default is light theme
    }
    // Method to apply the selected theme
    private void applyTheme() {
        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }
    // Method to save theme preference to SharedPreferences
    private void saveThemePreference() {
        sharedPreferences.edit().putBoolean(PREF_THEME_KEY, isDarkTheme).apply();
    }
}