package com.example.projectoneex2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.projectoneex2.viewmodel.UserViewModel;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

// Class responsible for user login functionality
public class Login extends AppCompatActivity {

    public static ArrayList<User> userList = new ArrayList<>();
    private UserViewModel viewModel=null;
    private EditText editTextUsername;
    private EditText editTextPassword;
    public static boolean isDarkTheme = false;
    public static final String PREF_THEME_KEY = "theme_preference";
    public static SharedPreferences sharedPreferences;
    private String token;
    private String username;
    public static String nickname;
    public static String profilePic;
    public static String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load and apply theme preference(dark mode)
        loadThemePreference();
        applyTheme();
        // Set layout
        setContentView(R.layout.activity_login);
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
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
        // Set onClickListener for the sign up button
        buttonSignUp.setOnClickListener(view -> {
            Intent i = new Intent(Login.this, SignUp.class);
            startActivity(i);
        });
        // Set OnCheckedChangeListener for the dark mode toggle button
        darkModeToggle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            isDarkTheme = isChecked;
            saveThemePreference();
            recreate(); // Restart activity to apply theme changes
        });
    }
    // Method to handle user login
    private void loginUser() {
        String username = editTextUsername.getText().toString();
        this.username=username;
        String password = editTextPassword.getText().toString();
        if (viewModel==null) {
            this.viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        }
        // Set the token to 0
        token ="0";
        // Set the status to wait
        status="wait";
        viewModel.getLogin(username, password);
        // Observe the token
        AtomicBoolean shownWaitMessage = new AtomicBoolean(false);
        viewModel.getToken(username, password).observe(this, token1 -> {
            token= token1;
            // If the status is succes, redirect to feedActivity
            if (status.equals("success")){
                loginMove(token);
                showToastSuccess();
                status="wait";
            }
            // If the status if failed, show a toast message
            if (status.equals("failed")){
                showToast();
                token ="0"  ;
                status="wait";
            }
            // If the status is wait, show a toast message
            if (status.equals("wait")&&!shownWaitMessage.get()){
                Toast.makeText(this, "wait", Toast.LENGTH_SHORT).show();
                // Set the shownWaitMessage to true(showing wait only once)
                shownWaitMessage.set(true);
            }
        });

    }
    // Method to redirect to feedActivity
        private void loginMove(String s) {
        if (!Objects.equals(s, "")) {
            // Redirect to feedActivity if login successful
            Intent i = new Intent(this, FeedActivity.class);
            i.putExtra("TOKEN_KEY", token);
            i.putExtra("USERNAME_KEY", username);
            startActivity(i);
        } else {
            // Show a toast message for invalid username or password
            showToast();
        }
    }
    // Method to display a toast message for invalid login attempt
    private void showToast() {
        Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
    }
    // Method to display a toast message for successful login
    private void showToastSuccess() {
        Toast.makeText(this, "Welcome", Toast.LENGTH_SHORT).show();
    }
    // Method to load theme preference from SharedPreferences
    private void loadThemePreference() {
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        isDarkTheme = sharedPreferences.getBoolean(PREF_THEME_KEY, false); // Default is light theme
    }

    // Method to save theme preference to SharedPreferences
    private void saveThemePreference() {
        sharedPreferences.edit().putBoolean(PREF_THEME_KEY, isDarkTheme).apply();
    }

    // Method to apply the selected theme
    private void applyTheme() {
        setTheme(isDarkTheme ? R.style.AppTheme_Dark : R.style.AppTheme_Light);
    }
}
