package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
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

        LinearLayout followersLayout = findViewById(R.id.followerslayout);
        LinearLayout followingLayout = findViewById(R.id.followinglayout);
        LinearLayout requestsLayout = findViewById(R.id.requestlayout);

        ArrayList<String> followersList = db.getFollowers(username);
        ArrayList<String> followingList = db.getFollowings(username);
        ArrayList<String> requestsList = db.getFriendRequests(username);

        ArrayList<CheckBox> requestChList = new ArrayList<CheckBox>();

        //build, inflate, and store checkboxes for friendsList and requestsList
        if(!followersList.isEmpty()) {
            for (String friend : followersList) {
                TextView tx = friendMaker(friend);
                followersLayout.addView(tx);
            }
        }

        if(!followingList.isEmpty()) {
            for (String friend : followingList) {
                TextView tx = friendMaker(friend);
                followingLayout.addView(tx);
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
                        Log.d("accept", "onClick: addfriend");
                        db.addFriend(username, friendname);
                        Log.d("accept", "onClick: deleteFriendReq");
                        db.deleteFriendRequest(friendname, username);
                        Log.d("accept", "onClick: -checkbox +textview");
                        followersList.add(friendname);
                        requestsList.remove(i);
                        followersLayout.addView(friendMaker(friendname));
                        requestsLayout.removeView(requestChList.get(i));
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
                ArrayList<String> friendfollowers = db.getFollowers(friendName);
                if(!userSet.contains(friendName)) {
                    Toast.makeText(FriendsActivity.this, "Username not found.", Toast.LENGTH_SHORT).show();
                    Log.d("addFriend", "onClick: user not found");
                } else if (friendfollowers.contains(username)) {
                    Toast.makeText(FriendsActivity.this, "Already following this user.", Toast.LENGTH_SHORT).show();
                    Log.d("addFriend", "onClick: user already friends");
                } else {
                    db.addFriendRequest(username, friendName);

                    //Send notification
                    Log.d("addFriend", "onClick: user found, senting notif");
                    String message = username + " has sent " + friendName + " a friend request.";
                    start_Sensing_new(message);

                    Log.d("addFriend", "onClick: notif done");
                    Toast.makeText(FriendsActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();

                }
                friendSearch.setText("");

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

        pendingIntent = PendingIntent.getBroadcast(this, 40, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        //Generating object of alarmManager using getSystemService method. Here ALARM_SERVICE is used to receive alarm manager with intent at a time.
        AlarmManager alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);

        //this method creates a repeating, exactly timed alarm
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, SystemClock.currentThreadTimeMillis() + 1000, pendingIntent);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, 1000, pendingIntent);
        Log.d("===Sensing alarm===", "Friend Request Notification.");
    }

    private TextView friendMaker(String friend) {
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
        return tx;
    }

    public void onBackPressed() {
        // Close app when back pressed
        Log.d("FriendsActivity", "backPressed, go to home");
        Intent intent = new Intent(getApplicationContext(), HomeActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);

    }
}