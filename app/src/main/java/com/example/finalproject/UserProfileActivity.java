package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfileActivity extends AppCompatActivity {

    private String username;
    private String name;
    private String curr_profile;
    private String curr_bio;
    private String curr_gender;
    private String curr_birthday;
    private String curr_online;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // Received intent and get sharedpreferences
        Intent received_intent = getIntent();
        username = received_intent.getStringExtra("Username");

        // Retrieved SharedPreference UserInfo to display initial values
        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        name = pref.getString(username + "_Name", null);
        curr_profile = pref.getString(username  + "_ProfilePic", null);
        curr_bio = pref.getString(username + "_Bio", null);
        curr_gender = pref.getString(username + "_Gender", null);
        curr_birthday = pref.getString(username + "_Birthday", null);
        curr_online = pref.getString(username + "_Online", null);

        // Get all display items
        TextView header = findViewById(R.id.header);
        ImageView image_display = findViewById(R.id.profilePic);
        TextView bio_display = findViewById(R.id.info1_change);
        TextView gender_display = findViewById(R.id.gender_change);
        TextView birthday_display = findViewById(R.id.info2_change);
        TextView online_display = findViewById(R.id.info3_change);

        // Show current items
        String output_profile = name + "'s Profile";
        header.setText(output_profile);
        setUpProfilePic(curr_profile, image_display);
        bio_display.setText(curr_bio);
        gender_display.setText(curr_gender);
        birthday_display.setText(curr_birthday);
        online_display.setText(curr_online);

        Log.d("UserProfileActivity", "onCreate: Successfully display UserProfile =============");

        // Navigate to EditProfile
        Button editProfile = findViewById(R.id.buttonChange);
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfileActivity.this, EditProfile.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

    }

    private void setUpProfilePic(String profilePic, ImageView imageDisplay) {
        if (profilePic.equals("Yoshi")) {
            imageDisplay.setImageResource(R.drawable.character_header_yoshi);
        } else if (profilePic.equals("Lion")) {
            imageDisplay.setImageResource(R.drawable.lion);
        } else if (profilePic.equals("Space")) {
            imageDisplay.setImageResource(R.drawable.space);
        } else if (profilePic.equals("Spaceman")) {
            imageDisplay.setImageResource(R.drawable.spaceman);
        }
    }

    public void onBackPressed() {
        // Go back to Post
        Intent intent = new Intent(UserProfileActivity.this, HomeActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);
    }
}