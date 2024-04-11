package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        db = new Database (this, "database");
        // Received intent and get sharedpreferences
        Intent received_intent = getIntent();
        String username = received_intent.getStringExtra("Username");

        String name = db.getName(username);

        TextView header = findViewById(R.id.header);
        header.setText("Welcome " + name + "!");

        // Create button object for all the buttons on the page
        Button diary = findViewById(R.id.diary_home);
        Button notes = findViewById(R.id.notes_home);
        Button mood = findViewById(R.id.mood_home);
        Button links = findViewById(R.id.helpfulLinks);
        Button logout = findViewById(R.id.logout);

//
//        // For diary
//        diary.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Home Activity", "diary: was pressed ");
//                Intent intent = new Intent(getApplicationContext(), DailyDiaryActivity.class);
//                intent.putExtra("Username", username);
//                startActivity(intent);
//            }
//        });
//
//        // For viewnotes
//        notes.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Home Activity", "notes: was pressed ");
//                Intent intent = new Intent(getApplicationContext(), ViewNotesActivity.class);
//                intent.putExtra("Username", username);
//                startActivity(intent);
//            }
//        });
//
//        // For tracker
//        mood.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Home Activity", "tracker: was pressed ");
//                Intent intent = new Intent(getApplicationContext(), TrackerActivity.class);
//                intent.putExtra("Username", username);
//                startActivity(intent);
//            }
//        });
//
//        // For helpful links
//        links.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Home Activity", "links: was pressed ");
//                Intent intent = new Intent(getApplicationContext(), HelpfulLinksActivity.class);
//                intent.putExtra("Username", username);
//                startActivity(intent);
//            }
//        });
//
//        // For logout
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("Home Activity", "logout: was pressed ");
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//            }
//        });
//
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        // Disable back pressed
//        Log.d("Home Activity", "onBackPressed: was pressed ");

    }

}