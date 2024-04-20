package com.example.finalproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Context c = this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new Database(this, "UserDatabase.db");

        Button login = findViewById(R.id.login);
        Button signUp = findViewById(R.id.newuser);
        requestAppPermissions();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Login ===", "onClick: submit");
                EditText usernameInp = findViewById(R.id.username);
                EditText passwordInp = findViewById(R.id.password);
                String currUsername = usernameInp.getText().toString();
                String currPassword = passwordInp.getText().toString();

                boolean isValid = db.validateUser(currUsername, currPassword);

                if(isValid) {
                    Log.d("Login ===", "username valid");
                    try {
                        saveLoginTime();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
    private void requestAppPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE

            }, 0);
        }
    }


    private void saveLoginTime() throws JSONException, IOException {
        String filename = "login_time.json";
        // Use the proper method to get the Downloads directory
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        if (!downloadsDir.exists()) {
            if (!downloadsDir.mkdirs()) {
                Log.e("LoginActivity", "Failed to create directory");
                return;
            }
        }
        File file = new File(downloadsDir, filename);
        if (!file.exists()) {
            file.createNewFile();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String logoutTime = sdf.format(new Date());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("logout_time", logoutTime);

        FileOutputStream fOut = new FileOutputStream(file, true);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(jsonObject.toString() + "\n");
        myOutWriter.close();
        fOut.close();
    }
//    @Override
//    public void onBackPressed() {
//        // Disable back pressed
//        Log.d("Login Activity", "onBackPressed: was pressed ");
//
//    }

}
