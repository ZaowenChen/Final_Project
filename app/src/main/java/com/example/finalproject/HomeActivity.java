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

    private TextView global;
    private TextView friends_Post;
    private Button post, profile, friends, settings;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent received_intent = getIntent();
        username = received_intent.getStringExtra("Username");
        global = findViewById(R.id.globalposts);
        friends_Post = findViewById(R.id.friendposts);
        post = findViewById(R.id.post);
        profile = findViewById(R.id.profile);
        friends = findViewById(R.id.friends);
        settings = findViewById(R.id.settings);


        global.setBackgroundColor(Color.parseColor("#BDE0FE"));
        friends_Post.setBackgroundColor(Color.parseColor("#FFFFFF"));

        Log.d("HomeActivity", "onCreate: adding fragment for global post");

        setFragmentListeners();
        navigateToFragment(PublicFragment.newInstance());  // Load public posts by default

        global.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                global.setBackgroundColor(Color.parseColor("#BDE0FE"));
                friends_Post.setBackgroundColor(Color.parseColor("#FFFFFF"));
                navigateToFragment(PublicFragment.newInstance());
            }
        });

        friends_Post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                friends_Post.setBackgroundColor(Color.parseColor("#BDE0FE"));
                global.setBackgroundColor(Color.parseColor("#FFFFFF"));

                navigateToFragment(PrivateFragment.newInstance());
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postIntent = new Intent(HomeActivity.this, PostActivity.class);
                postIntent.putExtra("Username", username);
                startActivity(postIntent);

            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Settingintent = new Intent(HomeActivity.this, AccountSettingActivity.class);
                Settingintent.putExtra("Username", username);
                startActivity(Settingintent);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, UserProfileActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

        friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, FriendsActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });


    }



    private void setFragmentListeners() {
        global.setOnClickListener(v -> switchFragment(true));
        friends_Post.setOnClickListener(v -> switchFragment(false));
    }

    private void switchFragment(boolean isPublic) {
        if (isPublic) {
            global.setBackgroundColor(Color.parseColor("#BDE0FE"));
            friends_Post.setBackgroundColor(Color.parseColor("#FFFFFF"));
            navigateToFragment(PublicFragment.newInstance());
        } else {
            friends_Post.setBackgroundColor(Color.parseColor("#BDE0FE"));
            global.setBackgroundColor(Color.parseColor("#FFFFFF"));
            navigateToFragment(PrivateFragment.newInstance());
        }
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
        Bundle bundle = new Bundle();
        bundle.putString("Username", username);
        fragment.setArguments(bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.postfrag, fragment); // Make sure this container ID matches your layout
        transaction.addToBackStack(null);
        transaction.commit();
    }

}