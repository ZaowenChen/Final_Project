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

public class PrivateFragment extends Fragment {
    private Database db;
    private TextView usernameView, contentView;
    private Button prevButton, nextButton;
    private static int postCounter = 0;
    private String username;

    public PrivateFragment() {
        // Required empty public constructor
    }

    public static PrivateFragment newInstance() {
        return new PrivateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        db = new Database(getActivity().getApplicationContext(), "UserDatabase.db");
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_private, container, false);
        username = getArguments().getString("Username");
        usernameView = view.findViewById(R.id.private_username);
        contentView = view.findViewById(R.id.private_postcontent);
        prevButton = view.findViewById(R.id.private_prev);
        nextButton = view.findViewById(R.id.private_next);
        loadNextPost(); // Load initial post

        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadPreviousPost();
                Log.d("button clicked", "prev button clicked");

            }
        });


        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadNextPost();
                Log.d("button clicked", "next button clicked");

            }
        });
        return view;
    }

    private void loadNextPost() {
        postCounter += 1;
        ArrayList<String> posts = db.loadPrivatePost(postCounter, username);
        if (posts == null || posts.isEmpty()) {
            postCounter -= 1; // Rollback if no new post
            Toast.makeText(getContext(), "No more posts", Toast.LENGTH_SHORT).show();
        } else {
            loadPost(posts);
        }
    }

    private void loadPreviousPost() {
        if (postCounter > 0) {
            postCounter -= 1;
            ArrayList<String> posts = db.loadPrivatePost(postCounter, username);
            if (posts == null || posts.isEmpty()) {
                postCounter += 1; // Rollback if no previous post
            } else {
                loadPost(posts);
            }
        }
    }

    private void loadPost(ArrayList<String> posts) {
        if (posts == null || posts.isEmpty()) {
            Toast.makeText(getContext(), "No posts available", Toast.LENGTH_SHORT).show();
            usernameView.setText("");
            contentView.setText("");
        } else {
            usernameView.setText(posts.get(0));
            contentView.setText(posts.get(1));
        }
    }
}
