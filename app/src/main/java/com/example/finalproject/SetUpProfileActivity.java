package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;

public class SetUpProfileActivity extends AppCompatActivity {



    // implement the share preference in the setup activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();








    }


    private void saveUserInfo(String UserId, String gender, String age, String address, String username, String password) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE); // access the shared preference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Prefix each key with the username to ensure uniqueness
        editor.putString(username + "_Name", name); // it would be stored as "zaowe_name : Zaowen"
        editor.putString(username + "_Gender", gender);
        editor.putString(username + "_Age", age);
        editor.putString(username + "_Address", address);
        editor.putString(username + "_Username", username);
        editor.putString(username + "_Password", password);
        editor.apply(); // apply to the shared preference
    }
}

