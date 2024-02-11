package com.example.projectoneex2;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class SignUp extends AppCompatActivity {

    public static ArrayList<User> userList = new ArrayList<>();
    private static final int REQUEST_IMAGE_PICK = 2;

    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextRepeatPassword;
    private Button btnSignUp;
    private Bitmap pic;
    private ImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextRepeatPassword = findViewById(R.id.editTextRepeatPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        imageViewProfile = findViewById(R.id.imageViewProfile);

        // Set onClickListener for the signup button
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signupUser();
            }
        });

        // Set onClickListener for the profile image
        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openGallery();
            }
        });
    }

    private void signupUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String repeatPassword = editTextRepeatPassword.getText().toString();


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
        Login.userList.add(new User(username, password,this.pic));
        Intent i=new Intent(SignUp.this, Login.class);
        startActivity(i);
        // Display a success message
        showToast("Signup successful");
    }
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                // Set the selected image to your ImageView
                imageViewProfile.setImageBitmap(bitmap);
                this.pic=bitmap;

                // Set the image to the current user object
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }
    



}
