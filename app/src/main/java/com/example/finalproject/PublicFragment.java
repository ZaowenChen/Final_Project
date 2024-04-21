package com.example.finalproject;

import static com.example.finalproject.Database.PRIVATE_POST_COL;
import static com.example.finalproject.Database.PUBLIC_POST_COL;
import static com.example.finalproject.Database.USER_PRIVATEPOST_COL;
import static com.example.finalproject.Database.USER_PUBLICPOST_COL;

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

public class PublicFragment extends Fragment {
    private Database db;
    private TextView usernameView, contentView;
    private Button prevButton, nextButton;

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
        Cursor cursor = db.getLastPublicPost();
        if (cursor != null && cursor.moveToFirst()) {
            int usernameIndex = cursor.getColumnIndex(USER_PUBLICPOST_COL);
            int contentIndex = cursor.getColumnIndex(PUBLIC_POST_COL);
            if (usernameIndex == -1 || contentIndex == -1) {
                Toast.makeText(getContext(), "Database column not found", Toast.LENGTH_SHORT).show();
                cursor.close();
                return false;
            }
            Log.d("LoadPost", "Username Index: " + usernameIndex + ", Content Index: " + contentIndex);
            if (cursor.getString(usernameIndex) != null) Log.d("LoadPost", "Username: " + cursor.getString(usernameIndex));
            if (cursor.getString(contentIndex) != null) Log.d("LoadPost", "Content: " + cursor.getString(contentIndex));
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