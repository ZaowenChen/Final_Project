package com.example.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import org.w3c.dom.Text;


public class HomeActivity extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //db = new Database(this, "database");
        // Received intent and get sharedpreferences
        //Intent received_intent = getIntent();
        //String username = received_intent.getStringExtra("Username");

        //String name = db.getName(username);

        TextView global = findViewById(R.id.globalposts);
        TextView friends_post = findViewById(R.id.friendposts);

        global.setBackgroundColor(Color.parseColor("#BDE0FE"));
        friends_post.setBackgroundColor(Color.parseColor("#FFFFFF"));

        Log.d("HomeActivity", "onCreate: adding fragment for global post");
        navigateToFragment(PublicFragment.newInstance());


        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setBackgroundColor(Color.parseColor("#BDE0FE"));
                friends_post.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });

        friends_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friends_post.setBackgroundColor(Color.parseColor("#BDE0FE"));
                global.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Disable back pressed
        Log.d("Home Activity", "onBackPressed: was pressed ");

    }
    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.postfrag, fragment); // Make sure this container ID matches your layout
        transaction.addToBackStack(null);
        transaction.commit();
    }

}