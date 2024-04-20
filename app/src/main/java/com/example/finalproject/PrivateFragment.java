package com.example.finalproject;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PrivateFragment extends Fragment {
    private Database db;
    private TextView usernameView, contentView;
    private Button prevButton, nextButton;

    public PrivateFragment() {
        // Required empty public constructor
    }
    public static PrivateFragment newInstance() {
        return new PrivateFragment();
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
        View view =  inflater.inflate(R.layout.fragment_private, container, false);
        //Set content
        TextView content = view.findViewById(R.id.private_postcontent);
        content.setText("Private Content");
        usernameView = view.findViewById(R.id.private_username);
        contentView = view.findViewById(R.id.private_postcontent);
        prevButton = view.findViewById(R.id.private_prev);
        nextButton = view.findViewById(R.id.private_next);
        // Load the last post initially
        if (!loadPost()) {
            Toast.makeText(getContext(), "No posts available", Toast.LENGTH_SHORT).show();
        }
        prevButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "No previous posts", Toast.LENGTH_SHORT).show();
        });
        nextButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Last post", Toast.LENGTH_SHORT).show();
        });
        return view;
    }

    // Method to load the last post from the database
    private boolean loadPost() {
        Cursor cursor = db.getLastPrivatePost();
        if (cursor != null && cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndex("user_privatepost_col");  // Adjust column name as per your schema
            int contentIndex = cursor.getColumnIndex("private_post_col");        // Adjust column name as per your schema
            if (usernameIndex == -1 || contentIndex == -1) {
                Toast.makeText(getContext(), "Database column not found", Toast.LENGTH_SHORT).show();
                cursor.close();
                return false;
            }
            String username = cursor.getString(usernameIndex);
            String content = cursor.getString(contentIndex);
            usernameView.setText(username);
            contentView.setText(content);
            cursor.close();
            return true;
        } else {
            return false;
        }
    }


}