package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Environment;
import java.io.IOException;



public class PasswordResetActivity extends AppCompatActivity {

    private EditText oldPassword, newPassword, conPassword;
    private Button confirm;
    private Database db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        db = new Database(this, "UserDatabase.db");


        oldPassword = findViewById(R.id.old_pass2);
        newPassword = findViewById(R.id.new_pass2);
        conPassword = findViewById(R.id.conf_pass2);
        confirm = findViewById(R.id.submit_password_reset);
        String username = getIntent().getStringExtra("username");

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = oldPassword.getText().toString();
                String newPass = newPassword.getText().toString();
                String confPass = conPassword.getText().toString();

                if (!db.isPasswordCorrect(username, oldPass)) {
                    oldPassword.setError("Password is incorrect");
                } else if (!newPass.equals(confPass)) {
                    conPassword.setError("Password inconsistent");
                } else {
                    if (db.updatePassword(username, newPass)) {
                        try {
                            savePasswordChangeLog(username, newPass);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

    }
    private void savePasswordChangeLog(String username, String newPassword) throws JSONException, IOException {
        String filename = "password_change_log.json";
        // Use the standard Documents directory path as per the logout time save method.
        String myDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Documents/" + filename;
        File file = new File(myDir);
        if (!file.exists()) {
            file.createNewFile();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String timestamp = sdf.format(new Date());

        JSONObject logEntry = new JSONObject();
        logEntry.put("username", username);
        logEntry.put("new_password", newPassword); // Caution: Storing plain passwords even in logs might be a security risk.
        logEntry.put("timestamp", timestamp);

        FileOutputStream fOut = new FileOutputStream(file, true);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(logEntry.toString() + "\n");
        myOutWriter.close();
        fOut.close();
    }

}