package com.example.projectoneex2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class Login extends AppCompatActivity {

    public static ArrayList<User> userList = new ArrayList<>();
    private EditText editTextUsername;
    private EditText editTextPassword;
    public static boolean isDarkTheme = false;
    public static final String PREF_THEME_KEY = "theme_preference";
    public static SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadThemePreference();
        applyTheme();
        setContentView(R.layout.activity_login);
        // Initialize views
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        Button buttonLogin = findViewById(R.id.buttonLogin);
        Button buttonSignUp = findViewById(R.id.btnSignup);
        ToggleButton darkModeToggle = findViewById(R.id.darkModeToggle);
        // Set toggle button state
        darkModeToggle.setChecked(isDarkTheme);

        // Set onClickListener for the login button
        buttonLogin.setOnClickListener(view -> loginUser());
        buttonSignUp.setOnClickListener(view -> {
            Intent i;
            i = new Intent(Login.this, SignUp.class);
            startActivity(i);
        });
        darkModeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkTheme = isChecked;
            saveThemePreference();
            recreate(); // Restart activity to apply theme changes
        });
    }


    private void loginUser() {
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();

        // Check if the user exists in the list
        boolean userExists = false;
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                userExists = true;
                break;
            }
        }

        // Display a message based on login success
        if (userExists) {
            Intent i=new Intent(this,feedActivity.class);
            i.putExtra("USERNAME", username);

            startActivity(i);

        } else {
            showToast();
        }
    }

    private void showToast() {
        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }
    private void loadThemePreference() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(PREF_THEME_KEY, false); // Default is light theme
    }

    private void saveThemePreference() {
        sharedPreferences.edit().putBoolean(PREF_THEME_KEY, isDarkTheme).apply();
    }

    private void applyTheme() {
        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }

}
