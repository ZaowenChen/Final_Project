package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PostActivity extends AppCompatActivity {
    private Database db;
    private EditText noteInput;
    private TextView usernameView;
    private CheckBox globalCheckbox, friendsCheckbox;
    private Button postButton;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        db = new Database(this, "UserDatabase.db");
        username = getIntent().getStringExtra("Username");
        noteInput = findViewById(R.id.note_input);
        globalCheckbox = findViewById(R.id.globalcheckbox);
        friendsCheckbox = findViewById(R.id.friendsCheckbox);
        postButton = findViewById(R.id.buttonChange);
        usernameView = findViewById(R.id.username); // TextView for displaying the username
        usernameView.setText(username);

        postButton.setOnClickListener(view -> postComment());
    }

    private void postComment() {
        String postContent = noteInput.getText().toString();
        String currentDate = getCurrentDate();
        if (postContent.isEmpty()) {
            Toast.makeText(this, "Post content cannot be empty", Toast.LENGTH_LONG).show();
            return;
        }
        if (globalCheckbox.isChecked()) {
            db.addPublicPost(username, postContent, currentDate);
            Toast.makeText(this, "Posted publicly", Toast.LENGTH_SHORT).show();
        } else if (friendsCheckbox.isChecked()) {
            db.addPrivatePost(username, postContent, currentDate);
            Toast.makeText(this, "Posted to friends", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Please select post visibility", Toast.LENGTH_LONG).show();
        }
        noteInput.setText("");        // Clear the input field after posting
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);
    }
    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void onBackPressed() {
        // Close app when back pressed
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);
    }
}
