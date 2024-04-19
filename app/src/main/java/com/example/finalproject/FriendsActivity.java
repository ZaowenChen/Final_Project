package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class FriendsActivity extends AppCompatActivity {

    private Database db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        db = new Database(this, "database");
        Intent received_intent = getIntent();
        String username = received_intent.getStringExtra("Username");

        LinearLayout friendsLayout = findViewById(R.id.friendslayout);
        LinearLayout requestsLayout = findViewById(R.id.requestlayout);

        ArrayList<String> friendsList = db.getFriends(username);
        ArrayList<String> requestsList = db.getFriendRequests(username);

    }
}