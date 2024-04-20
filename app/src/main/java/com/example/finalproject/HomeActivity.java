package com.example.finalproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.ContactsContract;
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
        Intent received_intent = getIntent();
        String username = received_intent.getStringExtra("Username");

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
                navigateToFragment(PublicFragment.newInstance());
            }
        });

        friends_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friends_post.setBackgroundColor(Color.parseColor("#BDE0FE"));
                global.setBackgroundColor(Color.parseColor("#FFFFFF"));
                navigateToFragment(PrivateFragment.newInstance());
            }
        });

        Button post = findViewById(R.id.post);
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(HomeActivity.this, PostActivity.class);
                postIntent.putExtra("Username", username);
                startActivity(postIntent);

            }
        });


        Button settings = findViewById(R.id.settings);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Settingintent = new Intent(HomeActivity.this, AccountSettingActivity.class);
                Settingintent.putExtra("Username", username);
                startActivity(Settingintent);
            }
        });


        Button profile = findViewById(R.id.profile);
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });


    }

    @Override
    public void onBackPressed() {
        // Close app when back pressed
        Log.d("Home Activity", "onBackPressed: was pressed ");
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.postfrag, fragment); // Make sure this container ID matches your layout
        transaction.addToBackStack(null);
        transaction.commit();
    }

}