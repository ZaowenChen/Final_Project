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
    private static final int DATABASE_VERSION = 7;

    private static final String TABLE_NAME = "Userinfo";
    private static final String USERINFO_ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String USERNAME_COL = "username";
    private static final String PASSWORD_COL = "password";
    private static final String EMIAL_COL = "Email";

    private static final String TABLE2_NAME = "PrivatePost";
    private static final String PRIVATEPOST_ID_COL = "Post_ID";
    private static final String USER_PRIVATEPOST_COL = "Username";
    private static final String PRIVATE_POST_COL = "Post";
    private static final String DATE_PRIVATEPOST_COL = "Date";

    private static final String TABLE3_NAME = "PublicPost";
    private static final String PUBLICPOST_ID_COL = "Post_ID";
    private static final String USER_PUBLICPOST_COL = "Username";
    private static final String PUBLIC_POST_COL = "Post";
    private static final String DATE_PUBLICPOST_COL = "Date";

    private static final String TABLE4_NAME = "Friendzone";
    private static final String USER_FRIENDZONE_COL = "Username";
    private static final String FRIEND_FRIENDZONE_COL = "FriendUsername";

    private static final String TABLE5_NAME = "FriendRequest";
    private static final String USER_COL = "Username";
    private static final String FRIEND_COL = "FriendUsername";


    public Database(Context context, String database) {super(context, DATABASE_NAME, null, DATABASE_VERSION);}

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSQL = "CREATE TABLE " + TABLE_NAME + " (" +
                USERINFO_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NAME_COL + " TEXT, " +
                USERNAME_COL + " TEXT, " +
                EMIAL_COL + " TEXT, " +
                PASSWORD_COL + " TEXT)";

        String createTable2SQL = "CREATE TABLE " + TABLE2_NAME + " (" +
                PRIVATEPOST_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERINFO_ID_COL + " INTEGER, " +
                PRIVATE_POST_COL + " TEXT, " +
                DATE_PRIVATEPOST_COL + " TEXT, " +
                "FOREIGN KEY (" + USERINFO_ID_COL + ") REFERENCES " + TABLE_NAME + "(" + USERINFO_ID_COL + "))";

        String createTable3SQL = "CREATE TABLE " + TABLE3_NAME + " (" +
                PUBLICPOST_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USERINFO_ID_COL + " INTEGER, " +
                PUBLIC_POST_COL + " TEXT, " +
                DATE_PUBLICPOST_COL + " TEXT, " +
                "FOREIGN KEY (" + USERINFO_ID_COL + ") REFERENCES " + TABLE_NAME + "(" + USERINFO_ID_COL + "))";

        String createTable4SQL = "CREATE TABLE " + TABLE4_NAME + " (" +
                USER_FRIENDZONE_COL + " INTEGER, " +
                FRIEND_FRIENDZONE_COL + " INTEGER, " +
                "PRIMARY KEY (" + USER_FRIENDZONE_COL + ", " + FRIEND_FRIENDZONE_COL + "), " +
                "FOREIGN KEY (" + USER_FRIENDZONE_COL + ") REFERENCES " + TABLE_NAME + "(" + USERINFO_ID_COL + "), " +
                "FOREIGN KEY (" + FRIEND_FRIENDZONE_COL + ") REFERENCES " + TABLE_NAME + "(" + USERINFO_ID_COL + "))";

        String createTable5SQL = "CREATE TABLE " + TABLE5_NAME + " (" +
                USER_COL + " INTEGER, " +
                FRIEND_COL + " INTEGER, " +
                "PRIMARY KEY (" + USER_COL + ", " + FRIEND_COL + "), " +
                "FOREIGN KEY (" + USER_COL + ") REFERENCES " + TABLE_NAME + "(" + USERINFO_ID_COL + "), " +
                "FOREIGN KEY (" + FRIEND_COL + ") REFERENCES " + TABLE_NAME + "(" + USERINFO_ID_COL + "))";

        db.execSQL(createTableSQL);
        db.execSQL(createTable2SQL);
        db.execSQL(createTable3SQL);
        db.execSQL(createTable4SQL);
        db.execSQL(createTable5SQL);
}

    // Method to add a new user
    public void addUser(String name,  String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, hashPassword(password));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Method to update user information, need some modification, update each field sololy
    public void updateUser(int id, String name, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, password);
        db.update(TABLE_NAME, values, USERINFO_ID_COL + " = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Method to add a private post
    public void addPrivatePost(int userId, String post, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PRIVATEPOST_COL, userId);
        values.put(PRIVATE_POST_COL, post);
        values.put(DATE_PRIVATEPOST_COL, date);
        db.insert(TABLE2_NAME, null, values);
        db.close();
    }

    // Method to add a public post
    public void addPublicPost(int userId, String post, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PUBLICPOST_COL, userId);
        values.put(PUBLIC_POST_COL, post);
        values.put(DATE_PUBLICPOST_COL, date);
        db.insert(TABLE3_NAME, null, values);
        db.close();
    }

    // Method to add a friend to the Friendzone table
    public void addFriend(int userId, int friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_FRIENDZONE_COL, userId);
        values.put(FRIEND_FRIENDZONE_COL, friendId);
        db.insert(TABLE4_NAME, null, values);
        db.close();
    }

    // Method to add a friend request
    public void addFriendRequest(int userId, int friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COL, userId);
        values.put(FRIEND_COL, friendId);
        db.insert(TABLE5_NAME, null, values);
        db.close();
    }

    // Method to delete a friend request
    public void deleteFriendRequest(int userId, int friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE5_NAME, USER_COL + " = ? AND " + FRIEND_COL + " = ?", new String[]{String.valueOf(userId), String.valueOf(friendId)});
        db.close();
    }



    //unused methods, were used in old versions
//    public ArrayList<Integer> getMoodValues(int id) {
//        ArrayList<Integer> result = new ArrayList<Integer>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor cursor = db.query(TABLE3_NAME, null,
//                ID_COL + "=?", new String[]{Integer.toString(id)}, null, null, null);
//        if(cursor != null && cursor.moveToFirst()) {
//            int moodIndex = cursor.getColumnIndex(MOOD_COL);
//            int anxietyIndex = cursor.getColumnIndex(ANXIETY_COL);
//            int onMedIndex = cursor.getColumnIndex(ON_MEDICINE_COL);
//            int takenMedIndex = cursor.getColumnIndex(TAKE_MEDICINE_COL);
//            if (moodIndex != -1 && anxietyIndex != -1 && onMedIndex != -1 && takenMedIndex != -1) {
//                result.add(cursor.getInt(moodIndex));
//                result.add(cursor.getInt(anxietyIndex));
//                result.add(cursor.getInt(onMedIndex));
//                result.add(cursor.getInt(takenMedIndex));
//            }
//            cursor.close();
//        }
//        return result;
//    }
//
//    public void setMoodRating(int id, int value) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE3_NAME + " SET " + MOOD_COL + "=" + value + " WHERE " +
//                ID_COL + "=" + id;
//        db.execSQL(query);
//        db.close();
//    }
//
//    public void setMoodAnxiety(int id, int value) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE3_NAME + " SET " + ANXIETY_COL + "=" + value + " WHERE " +
//                ID_COL + "=" + id;
//        db.execSQL(query);
//        db.close();
//    }
//
//    public void setMoodOnMed(int id, int value) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE3_NAME + " SET " + ON_MEDICINE_COL + "=" + value + " WHERE " +
//                ID_COL + "=" + id;
//        db.execSQL(query);
//        db.close();
//    }
//
//    public void setMoodTakeMed(int id, int value) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        String query = "UPDATE " + TABLE3_NAME + " SET " + TAKE_MEDICINE_COL + "=" + value + " WHERE " +
//                ID_COL + "=" + id;
//        db.execSQL(query);
//        db.close();
//    }


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

//    public void deleteNotes(int id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        db.delete(TABLE2_NAME, ID_COL + " = " + Integer.toString(id), null);
//        db.close();
//        return;
//    }

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
