package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SetUpProfileActivity extends AppCompatActivity {

    private String selectedpic = "Yoshi";

    // implement the share preference in the setup activity
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up_profile);

        // Retrieved Username from signUp page
        Intent retrieved = getIntent();
        String username = retrieved.getStringExtra("Username");

        ImageView profilePic = findViewById(R.id.profilePic);
        Spinner profilePicSelection = findViewById(R.id.profilePicSpinner);
        EditText bioInput = findViewById(R.id.bio_change);
        RadioGroup genderInput = findViewById(R.id.radioGroupGender);
        CalendarView birthdayInput = findViewById(R.id.calender);
        TimePicker onlineTime = findViewById(R.id.time);
        Button setup = findViewById(R.id.setUpProfilebtn);

        String[] profile_description = {"Yoshi", "Lion", "Space", "Spaceman"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, profile_description);
        profilePicSelection.setAdapter(adapter); // create a new adapter to feed the songs list into the spinner
        profilePicSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedpic = profile_description[position];
                if (selectedpic.equals("Yoshi")) {
                    profilePic.setImageResource(R.drawable.character_header_yoshi);
                } else if (selectedpic.equals("Lion")) {
                    profilePic.setImageResource(R.drawable.lion);
                } else if (selectedpic.equals("Space")) {
                    profilePic.setImageResource(R.drawable.space);
                } else if (selectedpic.equals("Spaceman")) {
                    profilePic.setImageResource(R.drawable.spaceman);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed here for nothing is selected
            }
        });

        // Set the date for Calendar View
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2000);
        calendar.set(Calendar.MONTH, Calendar.JANUARY);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long desiredDateInMillis = calendar.getTimeInMillis();
        birthdayInput.setDate(desiredDateInMillis);

        // Set the time for Time Picker
        onlineTime.setHour(9);
        onlineTime.setMinute(0);

        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String bio = "NA";
                String gender = "NA";

                if (!bioInput.getText().toString().equals("")){
                    bio = bioInput.getText().toString();
                }
                if(genderInput.getCheckedRadioButtonId() != -1) {
                    RadioButton selected = findViewById(genderInput.getCheckedRadioButtonId());
                    gender = selected.getText().toString();
                }

                Calendar birth_calendar = Calendar.getInstance();
                birth_calendar.setTimeInMillis(birthdayInput.getDate());

                String birthday = getDate(birth_calendar);
                String online = getTime(onlineTime.getHour(), onlineTime.getMinute());

                saveUserInfo(selectedpic, bio, gender, birthday, online, username);

                Toast.makeText(SetUpProfileActivity.this, "SetUp Complete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(SetUpProfileActivity.this, LoginActivity.class);
                startActivity(intent);

            }
        });

    }

    private String getDate(Calendar calendar) {
        // Format the selected date into a string
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String selectedDateStr = sdf.format(calendar.getTime());
        return selectedDateStr;
    }

    private String getTime(int hourOfDay, int minute) {
        String selectedTimeStr = String.format("%02d:%02d", hourOfDay, minute);
        return selectedTimeStr;
    }

    private void saveUserInfo(String profilePic, String bio, String gender, String birthday, String online, String username) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserInfo", MODE_PRIVATE); // access the shared preference
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Prefix each key with the username to ensure uniqueness
        editor.putString(username + "_ProfilePic", profilePic);
        editor.putString(username + "_Bio", bio);
        editor.putString(username + "_Gender", gender);
        editor.putString(username + "_Birthday", birthday);
        editor.putString(username + "_Online", online);
        editor.apply(); // apply to the shared preference
    }
}

