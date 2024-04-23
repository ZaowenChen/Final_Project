package com.example.finalproject;

import static com.example.finalproject.Database.PRIVATE_POST_COL;
import static com.example.finalproject.Database.USER_PRIVATEPOST_COL;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class PublicFragment extends Fragment {
    private Database db;
    private TextView usernameView, contentView;
    private Button prevButton, nextButton;
    private int currentPostId = -1; // Start with an invalid ID


    private static int postCounter = 0;

    public PublicFragment() {
        // Required empty public constructor
    }
    public static PublicFragment newInstance() {
        return new PublicFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new Database(getContext(), "UserDatabase.db");
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_public, container, false);
        //Set content
        usernameView = view.findViewById(R.id.public_username);
        contentView = view.findViewById(R.id.public_postcontent);
        prevButton = view.findViewById(R.id.public_prev);
        nextButton = view.findViewById(R.id.public_next);
        ArrayList<String> posts = db.loadPublicPost(postCounter);
        // Load the last post initially
        loadPost(posts);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCounter += 1;
                ArrayList<String> posts = db.loadPublicPost(postCounter);
                if(posts.isEmpty()) {
                    postCounter -= 1;
                }
                loadPost(posts);
            }
        });
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                postCounter -= 1;

                // to ensure counter does not go to negative
                postCounter = Math.max(0, postCounter);

                ArrayList<String> posts = db.loadPublicPost(postCounter);
                if(posts.isEmpty()) {
                    postCounter += 1;
                }
                loadPost(posts);
            }
        });

        return view;
    }

    private void loadPost(ArrayList<String> posts) {
        if (posts.isEmpty()) {
            Toast.makeText(getContext(), "No posts available", Toast.LENGTH_SHORT).show();
        } else {
            usernameView.setText(posts.get(0));
            contentView.setText(posts.get(1));
        }
    }
}