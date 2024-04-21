package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
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
import java.util.Calendar;
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
                tx.setTextSize(16);
                tx.setPadding(20,5,0,5);

                tx.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(FriendsActivity.this, FriendProfileActivity.class);
                        intent.putExtra("Friend", friend);
                        intent.putExtra("Username", username);
                        startActivity(intent);
                    }
                });

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
                } else {
                    db.addFriendRequest(username, friendName);

                    //Send notification
                    String message = username + " has sent " + friendName + " a friend request.";
                    start_Sensing_new(message);

                    Toast.makeText(FriendsActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();

                }

            }
        });
    }

    public void start_Sensing_new(String message) {  //testing for upload frequency.

        Log.d("MyActivity", "Notification On");

        //Creating a pending intent for sendNotification class.
        Intent intent = new Intent(this, sendNotification.class);
        intent.putExtra("Message", message);
        PendingIntent pendingIntent = null;

        Log.d("Main sensing: ", "Pending intent starting");
        //    pendingIntent = PendingIntent.getBroadcast(this, 40, intent, PendingIntent.FLAG_IMMUTABLE |PendingIntent.FLAG_UPDATE_CURRENT);
        pendingIntent = PendingIntent.getBroadcast(this, 40, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        //this method creates a repeating, exactly timed alarm
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), pendingIntent);
        Log.d("===Sensing alarm===", "Friend Request Notification.");
    }

    public void onBackPressed() {
        // Close app when back pressed
        Log.d("FriendsActivity", "backPressed, go to home");
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);

    }
}