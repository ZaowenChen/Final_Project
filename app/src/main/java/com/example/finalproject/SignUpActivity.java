package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

public class SignUpActivity extends AppCompatActivity {
    private EditText editTextName, editTextEmail, editTextUsername, editTextPassword;
    private Button signUpButton;
    private TextInputLayout UsernameLayout, PasswordLayout;
    private Database db;
    Context c = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize DBClass
        db = new Database(this, "UserDatabase.db"); // Use your actual database name

        // Initialize views
        editTextName = findViewById(R.id.Name);
        editTextEmail = findViewById(R.id.Email);
        editTextUsername = findViewById(R.id.Username);
        editTextPassword = findViewById(R.id.passwords);
        signUpButton = findViewById(R.id.buttonSignUp);
        UsernameLayout = findViewById(R.id.UsernameLayout);
        PasswordLayout = findViewById(R.id.PasswordLayout);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("SignUp", "onClick: signup");
                String name = editTextName.getText().toString();
                String address = editTextEmail.getText().toString();
                String username = editTextUsername.getText().toString();
                String password = editTextPassword.getText().toString();

                if(validateUsername(username) && validatePassword(password)) {
                    saveUserInfo(name, address, username, password);
                    Toast.makeText(SignUpActivity.this, "Sign Up Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean validateUsername(String username) {
        // Here we check the database to see if the username already exists
        if (username.length() == 5 && username.equals(username.toLowerCase()) && !username.contains(" ")) {
            if (!db.isUsernameExists(username)) {
                return true;
            } else {
                UsernameLayout.setError("The username exists!"); // required alert in the text input layout
                return false;
            }
        } else {
            if (username.isEmpty()) {
                UsernameLayout.setError("Name is required"); // required alert in the text input layout
            } else {
                UsernameLayout.setError("Username must be 5 characters, lowercase, no spaces.");
            }
            return false;
        }
    }

    // Method remains unchanged, just for context
    private boolean validatePassword(String password) {
        boolean hasUpperCase = !password.equals(password.toLowerCase());
        boolean hasNumber = password.matches(".*\\d.*");
        if(password.length() == 8 && hasUpperCase && hasNumber && !password.contains(" ")) {
            return true;
        } else {
            if (password.isEmpty()){
                PasswordLayout.setError("Password is required");
            }
            PasswordLayout.setError("Password must be 8 characters, start with uppercase, include a number, no spaces.");
            return false;
        }
    }

    private void saveUserInfo(String name, String address, String username, String password) {
        db.addUser(name, username, password);
    }

}
