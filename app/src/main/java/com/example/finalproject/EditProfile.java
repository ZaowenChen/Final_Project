package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditProfile extends AppCompatActivity {

    private String username;
    private String name;
    private String curr_profile;
    private String curr_bio;
    private String curr_gender;
    private String curr_birthday;
    private String curr_online;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // Retrieved Username
        Intent retrieved = getIntent();
        username = retrieved.getStringExtra("Username");

        // Retrieved SharedPreference UserInfo to display initial values
        SharedPreferences pref = getSharedPreferences("UserInfo", MODE_PRIVATE);
        name = pref.getString(username + "_Name", null);
        curr_profile = pref.getString(username  + "_ProfilePic", null);
        curr_bio = pref.getString(username + "_Bio", null);
        curr_gender = pref.getString(username + "_Gender", null);
        curr_birthday = pref.getString(username + "_Birthday", null);
        curr_online = pref.getString(username + "_Online", null);

        // Get all display items
        TextView header = findViewById(R.id.header);
        ImageView image_display = findViewById(R.id.profilePic);
        TextView bio_display = findViewById(R.id.info1_change);
        TextView gender_display = findViewById(R.id.gender_change);
        TextView birthday_display = findViewById(R.id.info2_change);
        TextView online_display = findViewById(R.id.info3_change);
        Calendar calendar = Calendar.getInstance();



        // Show current items
        String output_profile = name + "'s Profile";
        header.setText(output_profile);
        setUpProfilePic(curr_profile, image_display);
        bio_display.setText(curr_bio);
        gender_display.setText(curr_gender);
        birthday_display.setText(curr_birthday);
        online_display.setText(curr_online);

        Log.d("EditProfile: ", "onCreate: Display All Successful ==================");

        // Get all the changing features

        // Change Spinner
        Spinner profilePicSelection = findViewById(R.id.profilePicSpinner);
        String[] profile_description = {"Yoshi", "Lion", "Space", "Spaceman"};

        // Get index to show old profile
        // Iterate through the array to find the index of the target item
        int index = 0;
        for (int i = 0; i < profile_description.length; i++) {
            if (profile_description[i].equals(curr_profile)) {
                index = i; // Set the index if the item is found
                break;     // Exit the loop once the item is found
            }
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, profile_description);
        profilePicSelection.setAdapter(adapter); // create a new adapter to feed the songs list into the spinner


        profilePicSelection.setSelection(index);
        profilePicSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                curr_profile = profile_description[position];
                setUpProfilePic(curr_profile, image_display);
                Toast.makeText(EditProfile.this, "Updated Profile Picture", Toast.LENGTH_SHORT).show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // No action needed here for nothing is selected
            }
        });

        // Bio Change
        Button bio_change = findViewById(R.id.bio_change_btn);
        bio_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText new_bio = findViewById(R.id.bio_change);
                if (!new_bio.getText().toString().equals("")) {
                    curr_bio = new_bio.getText().toString();
                    bio_display.setText(curr_bio);
                } else {
                    bio_display.setText("NA");
                    curr_bio = "NA";
                }

                new_bio.setText("");
                Toast.makeText(EditProfile.this, "Updated Bio", Toast.LENGTH_SHORT).show();
            }
        });

        // Gender Change
        Button gender_change = findViewById(R.id.genderChangeBtn);
        gender_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RadioGroup radiogp = findViewById(R.id.radioGroupGender);
                int sel_num = radiogp.getCheckedRadioButtonId();
                if (sel_num != -1) {
                    RadioButton selectedbtn = findViewById(sel_num);
                    curr_gender = selectedbtn.getText().toString();
                    gender_display.setText(curr_gender);
                } else {
                    gender_display.setText("NA");
                    curr_gender = "NA";
                }

                radiogp.clearCheck();
                Toast.makeText(EditProfile.this, "Updated Gender", Toast.LENGTH_SHORT).show();
            }
        });

//        //Birthday change
//        Button birthday_change = findViewById(R.id.birthday_change_btn);
//        birthday_change.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                CalendarView inputCal = findViewById(R.id.calender);
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTimeInMillis(inputCal.getDate());
//                Log.d("DebugBirthday", "CalendarView Date in Millis: " + inputCal.getDate()); // Log the raw data from CalendarView
//                curr_birthday = getDate(calendar);
//                Log.d("DebugBirthday", "Formatted Birthday: " + curr_birthday); // Log the formatted date
//                birthday_display.setText(curr_birthday);
//                Toast.makeText(EditProfile.this, "Updated Birthday", Toast.LENGTH_SHORT).show();
//            }
//        });

        // Initialize the CalendarView
        final CalendarView inputCal = findViewById(R.id.calender);
        inputCal.setDate(calendar.getTimeInMillis(), true, true);
// Listener for date changes
        inputCal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            }
        });

        Button birthday_change = findViewById(R.id.birthday_change_btn);
        birthday_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curr_birthday = getDate(calendar); // Format the date from the calendar instance
                birthday_display.setText(curr_birthday); // Update the display
                Toast.makeText(EditProfile.this, "Updated Birthday", Toast.LENGTH_SHORT).show();
                Log.d("EditProfile", "Birthday updated to: " + curr_birthday);
            }
        });



        // Online change
        Button online_change = findViewById(R.id.time_change_btn);
        online_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePicker timePicker = findViewById(R.id.time);
                curr_online = getTime(timePicker.getHour(), timePicker.getMinute());
                online_display.setText(curr_online);
                Toast.makeText(EditProfile.this, "Updated Online Around", Toast.LENGTH_SHORT).show();
            }
        });

        // Return to Profile
        Button returnProfile = findViewById(R.id.returnProfilebtn);
        returnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo(curr_profile, curr_bio, curr_gender, curr_birthday, curr_online, username);
                Toast.makeText(EditProfile.this, "Edit Profile Complete", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(EditProfile.this, UserProfileActivity.class);
                intent.putExtra("Username", username);
                startActivity(intent);
            }
        });

    }

    private void setUpProfilePic(String profilePic, ImageView imageDisplay) {
        if (profilePic.equals("Yoshi")) {
            imageDisplay.setImageResource(R.drawable.character_header_yoshi);
        } else if (profilePic.equals("Lion")) {
            imageDisplay.setImageResource(R.drawable.lion);
        } else if (profilePic.equals("Space")) {
            imageDisplay.setImageResource(R.drawable.space);
        } else if (profilePic.equals("Spaceman")) {
            imageDisplay.setImageResource(R.drawable.spaceman);
        }
    }

    // Convert date to string
    private String getDate(Calendar calendar) {
        // Format the selected date into a string
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        String selectedDateStr = sdf.format(calendar.getTime());
        return selectedDateStr;
    }

    // Convert time to string
    private String getTime(int hourOfDay, int minute) {
        String selectedTimeStr = String.format("%02d:%02d", hourOfDay, minute);
        return selectedTimeStr;
    }

    private void updateUserInfo(String profilePic, String bio, String gender, String birthday, String online, String username) {
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

    public void onBackPressed() {
        // Does not update
        Toast.makeText(EditProfile.this, "Edit Profile Canceled", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(EditProfile.this, UserProfileActivity.class);
        intent.putExtra("Username", username);
        startActivity(intent);
    }

}