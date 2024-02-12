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
// Class responsible for user login functionality
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
        // Load and apply theme preference(dark mode)
        loadThemePreference();
        applyTheme();
        // Set layout
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
        String password = editTextPassword.getText().toString();
        // Check if the user exists in the list
        boolean userExists = false;
        for (User user : userList) {
            if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
                userExists = true;
                break;
            }
        }
        // Display a message based on login success or failure
        if (userExists) {
            // Redirect to feedActivity if login successful
            Intent i = new Intent(this, feedActivity.class);
            i.putExtra("USERNAME", username);
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
