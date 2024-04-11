package com.example.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 7; // database version
    private static final String TABLE_NAME = "users"; // define the user table column
    private static final String ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String AGE_COL = "age";
    private static final String GENDER_COL = "gender";
    private static final String USERNAME_COL = "username";
    private static final String PASSWORD_COL = "password";

    private static final String TABLE2_NAME = "tracker"; // define the tracker table
    private static final String DATE_COl = "date";  // define the date colum
    private static final String NOTES_COL = "notes";
    private static final String EXERCISE_COL = "exercise";
    private static final String SOCIAL_COL = "social";
    private static final String HOMEWORK_COL = "homework";
    private static final String SLEEP_COL = "sleep";
    private static final String EAT_COL = "eat";

    private static final String TABLE3_NAME = "MoodTable"; // define the moodtable column
    private static final String MOOD_USER = "MoodUsername";
    private static final String DATE_COL_MOOD = "date";  // define the date colum
    private static final String MOOD_COL = "Mood";
    private static final String ANXIETY_COL = "Anxiety";
    private static final String ON_MEDICINE_COL = "OnMedicine";
    private static final String TAKE_MEDICINE_COL = "TakeMedicine";
    private static final String APPLICABLE_COL = "Applicable";

    public Database(Context context, String database) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) { // creat three tables
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                AGE_COL + " INTEGER, " +
                GENDER_COL + " TEXT, " +
                USERNAME_COL + " TEXT, " +
                PASSWORD_COL + " TEXT)";

        String createTable2SQL = "CREATE TABLE " + TABLE2_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATE_COl + " TEXT, " +   // add the time into the table
                USERNAME_COL + " TEXT, " +
                NOTES_COL + " TEXT, " +
                EXERCISE_COL + " BOOLEAN, " +
                SOCIAL_COL + " BOOLEAN, " +
                HOMEWORK_COL + " BOOLEAN, " +
                SLEEP_COL + " BOOLEAN, " +
                EAT_COL + " BOOLEAN)";


        String createTable3SQL = "CREATE TABLE " + TABLE3_NAME + " (" +
                ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MOOD_USER + " TEXT, " +
                DATE_COL_MOOD + " TEXT, " +   // add the time into the table
                MOOD_COL + " INTEGER, " +
                ANXIETY_COL + " INTEGER, " +
                ON_MEDICINE_COL + " INTEGER, " +
                TAKE_MEDICINE_COL + " INTEGER)";

        db.execSQL(createTableSQL);
        db.execSQL(createTable2SQL);
        db.execSQL(createTable3SQL);
    }
    public void addUser(String name, int age, String gender, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(AGE_COL, age);
        values.put(GENDER_COL, gender);
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, hashPassword(password));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void addNotes(String date, String user, String notes, boolean exercise, boolean social, boolean homework, boolean sleep, boolean eat) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_COl, date);
        values.put(USERNAME_COL, user);
        values.put(NOTES_COL, notes);
        values.put(EXERCISE_COL, exercise);
        values.put(SOCIAL_COL, social);
        values.put(HOMEWORK_COL, homework);
        values.put(SLEEP_COL, sleep);
        values.put(EAT_COL, eat);
        db.insert(TABLE2_NAME, null, values);
        db.close();
    }

    public void addMood(String username, String date, int mood, int anxiety, int onMedicine, int takeMedicine){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DATE_COl, date);
        values.put(MOOD_USER, username);
        values.put(MOOD_COL, mood);
        values.put(ANXIETY_COL, anxiety);
        values.put(ON_MEDICINE_COL, onMedicine);
        values.put(TAKE_MEDICINE_COL, takeMedicine);
        db.insert(TABLE3_NAME, null, values);
        db.close();
    }

    public int getLastIdMood(String username) {
        int result = -1;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE3_NAME, new String[]{ID_COL, USERNAME_COL},
                USERNAME_COL + "=?", new String[]{username}, null, null, null);
        if(cursor!=null && cursor.moveToLast()) {
            int idIndex = cursor.getColumnIndex(ID_COL);
            if (idIndex != -1) {
                result = cursor.getInt(idIndex);
                cursor.close();
            }
        }
        return result;
    }


    //unused methods, were used in old versions
    public ArrayList<Integer> getMoodValues(int id) {
        ArrayList<Integer> result = new ArrayList<Integer>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE3_NAME, null,
                ID_COL + "=?", new String[]{Integer.toString(id)}, null, null, null);
        if(cursor != null && cursor.moveToFirst()) {
            int moodIndex = cursor.getColumnIndex(MOOD_COL);
            int anxietyIndex = cursor.getColumnIndex(ANXIETY_COL);
            int onMedIndex = cursor.getColumnIndex(ON_MEDICINE_COL);
            int takenMedIndex = cursor.getColumnIndex(TAKE_MEDICINE_COL);
            if (moodIndex != -1 && anxietyIndex != -1 && onMedIndex != -1 && takenMedIndex != -1) {
                result.add(cursor.getInt(moodIndex));
                result.add(cursor.getInt(anxietyIndex));
                result.add(cursor.getInt(onMedIndex));
                result.add(cursor.getInt(takenMedIndex));
            }
            cursor.close();
        }
        return result;
    }

    public void setMoodRating(int id, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE3_NAME + " SET " + MOOD_COL + "=" + value + " WHERE " +
                ID_COL + "=" + id;
        db.execSQL(query);
        db.close();
    }

    public void setMoodAnxiety(int id, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE3_NAME + " SET " + ANXIETY_COL + "=" + value + " WHERE " +
                ID_COL + "=" + id;
        db.execSQL(query);
        db.close();
    }

    public void setMoodOnMed(int id, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE3_NAME + " SET " + ON_MEDICINE_COL + "=" + value + " WHERE " +
                ID_COL + "=" + id;
        db.execSQL(query);
        db.close();
    }

    public void setMoodTakeMed(int id, int value) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE3_NAME + " SET " + TAKE_MEDICINE_COL + "=" + value + " WHERE " +
                ID_COL + "=" + id;
        db.execSQL(query);
        db.close();
    }


    public String getName(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{USERNAME_COL, NAME_COL}, USERNAME_COL + "=?", new String[]{username}, null, null, null);
        String result = "";
        if (cursor != null && cursor.moveToFirst()) {
            int nameIndex = cursor.getColumnIndex(NAME_COL);
            if (nameIndex != -1) {
                result = cursor.getString(nameIndex);
            }
        }
        cursor.close();
        return result;

    }
//
//    public ArrayList<Tuple> getNotes(String username) {
//
//        ArrayList<Tuple> result = new ArrayList<Tuple>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE2_NAME, null, USERNAME_COL + "=?", new String[]{username}, null, null, null);
//
//        if(cursor != null && cursor.moveToFirst()) {
//            int idIndex = cursor.getColumnIndex(ID_COL);
//            int noteIndex = cursor.getColumnIndex(NOTES_COL);
//            if (noteIndex != -1 && idIndex != -1) {
//                do {
//                    result.add(new Tuple(
//                            cursor.getInt(idIndex), cursor.getString(noteIndex)
//                    ));
//                }
//                while(cursor.moveToNext());
//            }
//        }
//
//        cursor.close();
//        return result;
//    }

    public void deleteNotes(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE2_NAME, ID_COL + " = " + Integer.toString(id), null);
        db.close();
        return;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to hash password", e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("Database Upgrade", "Upgrading the database from version " + oldVersion + " to " + newVersion + ". All old data will be lost.");

        // Drop the old tables if they exist
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE2_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE3_NAME);



        // Call onCreate to recreate the database with the new schema
        onCreate(db);
    }


    public boolean isUsernameExists(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + USERNAME_COL + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{username});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public boolean validateUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);

        Cursor cursor = db.query(TABLE_NAME, new String[]{USERNAME_COL, PASSWORD_COL}, USERNAME_COL + "=?", new String[]{username}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int passwordIndex = cursor.getColumnIndex(PASSWORD_COL);
            if (passwordIndex != -1) {
                String storedPassword = cursor.getString(passwordIndex);
                cursor.close();
                return storedPassword.equals(hashedPassword);
            }
            cursor.close();
        }
        return false;
    }

}
