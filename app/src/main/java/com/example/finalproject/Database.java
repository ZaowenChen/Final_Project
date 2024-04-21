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
import java.util.HashSet;
import java.util.Set;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "UserDatabase.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "Userinfo";
    private static final String USERINFO_ID_COL = "id";
    private static final String NAME_COL = "name";
    private static final String USERNAME_COL = "username";
    private static final String PASSWORD_COL = "password";
    private static final String EMAIL_COL = "Email";

    private static final String TABLE2_NAME = "PrivatePost";
    private static final String PRIVATEPOST_ID_COL = "Post_ID";
    public static final String USER_PRIVATEPOST_COL = "Username";
    public static final String PRIVATE_POST_COL = "Post";
    private static final String DATE_PRIVATEPOST_COL = "Date";

    private static final String TABLE3_NAME = "PublicPost";
    private static final String PUBLICPOST_ID_COL = "Post_ID";
    public static final String USER_PUBLICPOST_COL = "Username";
    public  static final String PUBLIC_POST_COL = "Post";
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
                USERNAME_COL + " TEXT UNIQUE, " + // Make the username column unique
                EMAIL_COL + " TEXT, " +
                PASSWORD_COL + " TEXT)";

        String createTable2SQL = "CREATE TABLE " + TABLE2_NAME + " (" +
                PRIVATEPOST_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_PRIVATEPOST_COL + " TEXT, " +
                PRIVATE_POST_COL + " TEXT, " +
                DATE_PRIVATEPOST_COL + " TEXT, " +
                "FOREIGN KEY (" + USER_PRIVATEPOST_COL + ") REFERENCES " + TABLE_NAME + "(" + USERNAME_COL + "))";

        String createTable3SQL = "CREATE TABLE " + TABLE3_NAME + " (" +
                PUBLICPOST_ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                USER_PUBLICPOST_COL + " TEXT, " +
                PUBLIC_POST_COL + " TEXT, " +
                DATE_PUBLICPOST_COL + " TEXT, " +
                "FOREIGN KEY (" + USER_PUBLICPOST_COL + ") REFERENCES " + TABLE_NAME + "(" + USERNAME_COL + "))";


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
    public void addUser(String name, String address, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NAME_COL, name);
        values.put(EMAIL_COL, address);
        values.put(USERNAME_COL, username);
        values.put(PASSWORD_COL, hashPassword(password));
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String getEmailByUsername(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{EMAIL_COL}, USERNAME_COL + "=?", new String[]{username}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            String email = cursor.getString(0);
            cursor.close();
            db.close();
            return email;
        }
        return null;
    }

    public boolean updateEmail(String username, String newEmail) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(EMAIL_COL, newEmail);
        int rows = db.update(TABLE_NAME, contentValues, USERNAME_COL + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }

    public boolean isPasswordCorrect(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        String hashedPassword = hashPassword(password);
        Cursor cursor = db.query(TABLE_NAME, new String[]{PASSWORD_COL}, USERNAME_COL + "=? AND " + PASSWORD_COL + "=?", new String[]{username, hashedPassword}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        db.close();
        return exists;
    }
    public boolean updatePassword(String username, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String hashedPassword = hashPassword(newPassword);
        contentValues.put(PASSWORD_COL, hashedPassword);
        int rows = db.update(TABLE_NAME, contentValues, USERNAME_COL + "=?", new String[]{username});
        db.close();
        return rows > 0;
    }
    public void addPrivatePost(String Username, String post, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PRIVATEPOST_COL, Username);
        values.put(PRIVATE_POST_COL, post);
        values.put(DATE_PRIVATEPOST_COL, date);
        db.insert(TABLE2_NAME, null, values);
        db.close();
    }

    // Method to add a public post
    public void addPublicPost(String Username, String post, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_PUBLICPOST_COL, Username);
        values.put(PUBLIC_POST_COL, post);
        values.put(DATE_PUBLICPOST_COL, date);
        db.insert(TABLE3_NAME, null, values);
        db.close();
    }
    public Cursor getLastPrivatePost() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE2_NAME + " ORDER BY " + PRIVATEPOST_ID_COL + " DESC LIMIT 1";
        Cursor cursorprivate = db.rawQuery(sql, null);
        return cursorprivate;
    }

    public Cursor getLastPublicPost() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE3_NAME + " ORDER BY " + PUBLICPOST_ID_COL + " DESC LIMIT 1";
        Cursor cursorpublic = db.rawQuery(sql, null);
        return cursorpublic;
    }


    // Method to add a friend to the Friendzone table
    public void addFriend(String userId, String friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_FRIENDZONE_COL, userId);
        values.put(FRIEND_FRIENDZONE_COL, friendId);
        db.insert(TABLE4_NAME, null, values);
        db.close();
    }

    // Method to remove a friend from the Friendzone table
    public void removeFriend(String username, String friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE4_NAME, USER_COL + " = ? AND " + FRIEND_COL + " = ?", new String[]{username, friend});
        db.delete(TABLE4_NAME, FRIEND_COL + " = ? AND " + USER_COL + " = ?", new String[]{username, friend});
        db.close();
    }


    // Method to add a friend request
    public void addFriendRequest(String userId, String friendId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USER_COL, userId);
        values.put(FRIEND_COL, friendId);
        db.insert(TABLE5_NAME, null, values);
        db.close();
    }

    // Method to delete a friend request
    public void deleteFriendRequest(String username, String friend) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE5_NAME, USER_COL + " = ? AND " + FRIEND_COL + " = ?", new String[]{username, friend});
        db.close();
    }



    public ArrayList<String> getFriends(String username) {
        ArrayList<String> result = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE4_NAME, null, USER_COL + "=?",
                new String[]{username}, null, null, null);
        if(cursor != null) {
            int friendIndex = cursor.getColumnIndex(FRIEND_COL);
            if(friendIndex!= -1) {
                while(cursor.moveToNext()) {
                    result.add(cursor.getString(friendIndex));
                }
            }
            cursor.close();
        }
        Cursor cursor1 = db.query(TABLE4_NAME, null, FRIEND_COL + "=?",
                new String[]{username}, null, null, null);
        if(cursor1 != null) {
            int friendIndex = cursor1.getColumnIndex(USER_COL);
            if(friendIndex!= -1) {
                while(cursor1.moveToNext()) {
                    result.add(cursor1.getString(friendIndex));
                }
            }
            cursor1.close();
        }
        db.close();
        return result;
    }


    public ArrayList<String> getFriendRequests(String username) {
        ArrayList<String> result = new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE5_NAME, null, FRIEND_COL + "=?",
                new String[]{username}, null, null, null);
        if(cursor != null) {
            int senderIndex = cursor.getColumnIndex(USER_COL);
            if(senderIndex!= -1) {
                while(cursor.moveToNext()) {
                    result.add(cursor.getString(senderIndex));
                }
            }
            cursor.close();
        }
        db.close();
        return result;
    }

    public HashSet<String> getUserSet() {
        HashSet<String> userSet = new HashSet<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{USER_COL},
                null, null, null, null, null);
        if(cursor != null) {
            while(cursor.moveToNext()) {
                userSet.add(cursor.getString(0));
            }
            cursor.close();
        }
        db.close();
        return userSet;
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
