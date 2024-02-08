package com.example.projectoneex2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {
    private EditText editTextContent;
    private static final int EDIT_POST_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_activty);

        // Initialize UI components
        editTextContent = findViewById(R.id.editTextContent);

        // Get the initial content and position passed from the MainActivity
        String initialContent = getIntent().getStringExtra("postContent");
        editTextContent.setText(initialContent);
        final int position = getIntent().getIntExtra("position", -1);

        // Set up the Save button
        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the edited content
                String updatedContent = editTextContent.getText().toString().trim();

                // Check if content is empty
                if (updatedContent.isEmpty()) {
                    // Show an error message if content is empty
                    Toast.makeText(EditActivity.this, "Content cannot be empty", Toast.LENGTH_SHORT).show();
                } else {
                    // Pass the edited content back to the MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("updatedContent", updatedContent);
                    resultIntent.putExtra("position", position);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Finish the EditActivity
                }
            }
        });
    }

}
