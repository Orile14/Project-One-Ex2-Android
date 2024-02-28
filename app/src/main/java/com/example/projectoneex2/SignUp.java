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
import androidx.lifecycle.ViewModelProvider;

import com.example.projectoneex2.viewmodel.UserViewModel;

import java.io.IOException;
import java.util.ArrayList;

// Activity class for user signup
public class SignUp extends AppCompatActivity {
    public static ArrayList<User> userList = new ArrayList<>();
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //dark mode setting
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_signup);
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
        // Add the new user to the list
        User a=new User(username, password,this.pic,nickname);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.SignUp(a);
        Intent i=new Intent(SignUp.this, Login.class);
        startActivity(i);
        // Display a success message
        showToast("Signup successful");
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
