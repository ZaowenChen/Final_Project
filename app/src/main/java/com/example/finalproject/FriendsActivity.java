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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;

public class FriendsActivity extends AppCompatActivity {

    private Database db;
    private String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        db = new Database(this, "database");
        Intent received_intent = getIntent();
        username = received_intent.getStringExtra("Username");

        LinearLayout friendsLayout = findViewById(R.id.friendslayout);
        LinearLayout requestsLayout = findViewById(R.id.requestlayout);

        ArrayList<String> friendsList = db.getFriends(username);
        ArrayList<String> requestsList = db.getFriendRequests(username);

        ArrayList<CheckBox> requestChList = new ArrayList<CheckBox>();

        //build, inflate, and store checkboxes for friendsList and requestsList
        if(!friendsList.isEmpty()) {
            for (String friend : friendsList) {
                TextView tx = new TextView(this);
                tx.setText(friend);
                friendsLayout.addView(tx);
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

        Button accept = findViewById(R.id.acceptFriend);
        Button deny = findViewById(R.id.denyFriend);
        Button add = findViewById(R.id.addFriend);

        EditText friendSearch = findViewById(R.id.friendSearch2);

        HashSet<String> userSet = db.getUserSet();

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
                String friendName = friendSearch.getText().toString();
                if(!userSet.contains(friendName)) {
                    Toast.makeText(FriendsActivity.this, "Username not found.", Toast.LENGTH_SHORT).show();
                    return;
                }
                db.addFriendRequest(username, friendName);
                Toast.makeText(FriendsActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onBackPressed() {
        // Close app when back pressed
        Log.d("FriendsActivity", "backPressed, go to home");
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);

    }
}