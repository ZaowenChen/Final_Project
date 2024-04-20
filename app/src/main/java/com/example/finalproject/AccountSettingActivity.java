package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;

public class AccountSettingActivity extends AppCompatActivity {

    private TextView userOld, emailOld;
    private EditText emailNew;
    private Button btnConfirm, btnResetPassword, btnLogout;
    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_setting);
        db = new Database(this, "UserDatabase.db");
        userOld = findViewById(R.id.userold);
        emailOld = findViewById(R.id.emailold);
        emailNew = findViewById(R.id.email2);

        btnConfirm = findViewById(R.id.conf_changes);
        btnResetPassword = findViewById(R.id.password_reset);
        btnLogout = findViewById(R.id.logout_settings);

        // Assume the username is passed from the previous activity
        String username = getIntent().getStringExtra("Username");
        userOld.setText(username);
        emailOld.setText(db.getEmailByUsername(username));

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (db.updateEmail(username, emailNew.getText().toString())) {
                    emailOld.setText(emailNew.getText().toString());
                    emailNew.setText("");
                }
            }
        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AccountSettingActivity.this, PasswordResetActivity.class));
                Intent Passwordreset = new Intent(AccountSettingActivity.this, PasswordResetActivity.class);
                Passwordreset.putExtra("Username", username);
                startActivity(Passwordreset);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    saveLogoutTime();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                startActivity(new Intent(AccountSettingActivity.this, LoginActivity.class));
            }
        });

    }

    private void saveLogoutTime() throws JSONException, IOException {
            String filename = "logout_time.json";
            // Use the proper method to get the Downloads directory
            File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
            if (!downloadsDir.exists()) {
                if (!downloadsDir.mkdirs()) {
                    Log.e("LogoutActivity", "Failed to create directory");
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

}
