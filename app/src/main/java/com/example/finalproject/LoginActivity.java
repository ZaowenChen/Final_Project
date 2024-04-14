package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context c = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new Database(this, "UserDatabase.db");

        Button submit = findViewById(R.id.login);
        Button signUp = findViewById(R.id.newuser);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login ===", "onClick: submit");
                EditText usernameInp = findViewById(R.id.username);
                EditText passwordInp = findViewById(R.id.password);
                String currUsername = usernameInp.getText().toString();
                String currPassword = passwordInp.getText().toString();

                boolean isValid = db.validateUser(currUsername, currPassword); // This method needs to be implemented in your DBClass

                if(isValid) {
                    Log.d("Login ===", "username valid");
                    Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
                    intent.putExtra("Username", currUsername);
                    startActivity(intent);
                } else {
                    Log.d("Login ===", "username invalid");
                    TextView err = findViewById(R.id.errPrint);
                    err.setText("Username or Password is invalid.");
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login ===", "onClick: signup");
                Intent returnSignup = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(returnSignup);
            }
        });
    }
//    @Override
//    public void onBackPressed() {
//        // Disable back pressed
//        Log.d("Login Activity", "onBackPressed: was pressed ");
//
//    }

}
