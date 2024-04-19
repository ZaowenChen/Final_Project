package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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

        ArrayList<CheckBox> friendChList = new ArrayList<CheckBox>();
        ArrayList<CheckBox> requestChList = new ArrayList<CheckBox>();

        //build, inflate, and store checkboxes for friendsList and requestsList
        if(!friendsList.isEmpty()) {
            for (String friend : friendsList) {
                CheckBox ch = new CheckBox(this);
                ch.setText(friend);
                friendsLayout.addView(ch);
                friendChList.add(ch);
            }
        }
        if(!requestsList.isEmpty()) {
            for (String req : requestsList) {
                CheckBox ch = new CheckBox(this);
                ch.setText(req);
                requestsLayout.addView(ch);
                requestChList.add(ch);
            }
        }

        Button remove = findViewById(R.id.removeFriend);
        Button accept = findViewById(R.id.acceptFriend);
        Button deny = findViewById(R.id.denyFriend);
        Button add = findViewById(R.id.addFriend);

        EditText friendSearch = findViewById(R.id.friendSearch2);

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = friendChList.size()-1; i >= 0; i--) {
                    if(friendChList.get(i).isChecked()) {
                        db.removeFriend(username, friendsList.get(i));
                        friendsList.remove(i);
                        friendsLayout.removeView(friendChList.get(i));
                        friendChList.remove(i);
                    }
                }
            }
        });

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = requestChList.size()-1; i >= 0; i--) {
                    if(requestChList.get(i).isChecked()) {
                        String friendname = requestsList.get(i);
                        db.addFriend(username, friendname);
                        db.deleteFriendRequest(friendname, username);
                        friendsList.add(friendname);
                        requestsList.remove(i);
                        CheckBox ch = requestChList.get(i);
                        friendsLayout.addView(ch);
                        requestsLayout.removeView(ch);
                        friendChList.add(ch);
                        requestChList.remove(i);
                    }
                }
            }
        });

        deny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = requestChList.size()-1; i >= 0; i--) {
                    if(requestChList.get(i).isChecked()) {
                        String friendname = requestsList.get(i);
                        db.deleteFriendRequest(friendname, username);
                        requestsList.remove(i);
                        CheckBox ch = requestChList.get(i);
                        requestChList.remove(i);
                        requestsLayout.removeView(ch);
                    }
                }
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}