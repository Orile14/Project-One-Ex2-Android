package com.example.projectoneex2;

import static com.example.projectoneex2.Login.PREF_THEME_KEY;
import static com.example.projectoneex2.Login.isDarkTheme;
import static com.example.projectoneex2.Login.sharedPreferences;
import android.Manifest;


import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectoneex2.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;

// Activity class for user signup
public class SignUp extends AppCompatActivity {
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 8;
    private UserViewModel viewModel=null;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001; // You can choose any integer value

    // Views
    private EditText editTextUsername;
    private EditText editTextNickname;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Bitmap pic;
    private ImageView imageViewProfile;
    public static MutableLiveData<String> indicator=new MutableLiveData<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dark mode setting
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_signup);
        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNickname=findViewById(R.id.editTextNickname);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        Button btnSignUp = findViewById(R.id.btnSignUp);
        imageViewProfile = findViewById(R.id.imageViewProfile);
        ToggleButton darkModeToggle = findViewById(R.id.toggleButton);
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
        // Check if any field is empty
        if (username.isEmpty() || password.isEmpty() || nickname.isEmpty() || repeatPassword.isEmpty()) {
            showToast("All fields are required");
            return;
        }

        // Check if passwords match
        if (!password.equals(repeatPassword)) {
            showToast("Passwords do not match");
            return;
        }
        if (pic == null) {
            showToast("Profile picture is required");
            return;
        }
        indicator.postValue("wait");
        // Add the new user to the list
        User a=new User(username, password,this.pic,nickname);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.SignUp(a);
        AtomicBoolean shownWaitMessage = new AtomicBoolean(false);
        // Observe the indicator
        indicator.observe(this, s -> {
            // If the status is wait, display a wait message
            if (s.equals("wait") && !shownWaitMessage.get()) {
                showToast("Please wait");
                // Set the shownWaitMessage to true to show it only once
                shownWaitMessage.set(true);
            }
            // If the status is true, redirect to the login screen
            if (s.equals("true")) {
                Intent i = new Intent(SignUp.this, Login.class);
                startActivity(i);
                // Display a success message
                showToast("Signup successful");
            }
            // If the status is false, display a message
            if (s.equals("false")) {
                showToast("Username already taken");
                indicator.postValue("wait");
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
        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
        builder.setTitle("Choose Action");
        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals("Choose from Gallery")) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_IMAGE_PICK);
            } else if (options[item].equals("Take a Picture")) {
                // Check if the CAMERA permission is not granted
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request the CAMERA permission
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                } else {
                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
        // Set the profile picture
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageViewProfile.setImageBitmap(bitmap);
                this.pic = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Set the profile picture
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
