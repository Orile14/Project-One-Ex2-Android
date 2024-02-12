package com.example.projectoneex2;

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

import java.io.IOException;
import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    public static ArrayList<User> userList = new ArrayList<>();
    private static final int REQUEST_IMAGE_PICK = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private EditText editTextUsername;
    private EditText editTextNickname;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Bitmap pic;
    private ImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        // Set toggle button state
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

    private void signupUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String nickname=editTextNickname.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty() || nickname.isEmpty() || repeatPassword.isEmpty()) {
            showToast("All fields are required");
            return;
        }

        // Check if passwords match
        if (!password.equals(repeatPassword)) {
            showToast("Passwords do not match");
            return;
        }

        // Check if the username is already taken
        for (User user : userList) {
            if (user.getUsername().equals(username)) {
                showToast("Username already taken");
                return;
            }
        }
        if (pic == null) {
            showToast("Profile picture is required");
            return;
        }

        // Add the new user to the list
        Login.userList.add(new User(username, password,this.pic,nickname));
        Intent i=new Intent(SignUp.this, Login.class);
        startActivity(i);
        // Display a success message
        showToast("Signup successful");
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void openGallery() {
        final CharSequence[] options = {"Choose from Gallery", "Take a Picture", "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
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

    // Modify onActivityResult() method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                imageViewProfile.setImageBitmap(bitmap);
                this.pic = bitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
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



}