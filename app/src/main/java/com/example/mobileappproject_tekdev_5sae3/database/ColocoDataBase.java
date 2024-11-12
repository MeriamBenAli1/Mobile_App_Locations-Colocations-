package com.example.mobileappproject_tekdev_5sae3.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.mobileappproject_tekdev_5sae3.Dao.UserDao;
import com.example.mobileappproject_tekdev_5sae3.Entity.User;

public class ColocoDataBase extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "ColokoDb.db";
    private static final int DATABASE_VERSION = 2;  // Increment version if needed

    // Table and columns for User
    private static final String TABLE_USER = "user";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_PASSWORD = "password";  // New password column
    private static final String COLUMN_IS_LOGGED_IN = "is_logged_in"; // New column to track login status
    private static final String COLUMN_OTP = "otp"; // New column for OTP
    private static final String COLUMN_OTP_TIMESTAMP = "otp_timestamp"; // New
    // Create table query for User
    private static final String CREATE_TABLE_USER = "CREATE TABLE " + TABLE_USER + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_NAME + " TEXT, " +
            COLUMN_EMAIL + " TEXT, " +
            COLUMN_PASSWORD + " TEXT, " +
            COLUMN_IS_LOGGED_IN + " INTEGER DEFAULT 0, " + // 0 = not logged in, 1 = logged in
            COLUMN_OTP + " TEXT, " + // Column for OTP
            COLUMN_OTP_TIMESTAMP + " INTEGER" + // Column for OTP timestamp
            ");";
    public ColocoDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_USER);  // Create the users table with the new structure
        Log.d("Database", "Table users created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        onCreate(db);
    }
    // Method to check if a user is logged in
    public boolean isUserLoggedIn() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_IS_LOGGED_IN + "=1";
        Cursor cursor = db.rawQuery(query, null);

        boolean isLoggedIn = cursor.getCount() > 0;  // If any user has `is_logged_in=1`, they are logged in.
        cursor.close();
        db.close();
        return isLoggedIn;
    }
    // Method to log in the user (update the `is_logged_in` flag)
    public void logInUser(String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_USER + " SET " + COLUMN_IS_LOGGED_IN + "=1 WHERE " + COLUMN_EMAIL + "=?";
        db.execSQL(query, new String[]{email});
        db.close();
    }
    // Method to insert a user
    public void insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertSQL = "INSERT INTO " + TABLE_USER + " (" + COLUMN_NAME + ", " + COLUMN_EMAIL + ", " + COLUMN_PASSWORD + ", " + COLUMN_IS_LOGGED_IN + ") " +
                "VALUES ('" + user.getName() + "', '" + user.getEmail() + "', '" + user.getPassword() + "', 0)";
        db.execSQL(insertSQL);
        Log.d("Database", "User inserted: " + user.getName());
        db.close();
    }

    // Method to retrieve all users
    public Cursor getAllUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectSQL = "SELECT * FROM " + TABLE_USER;
        return db.rawQuery(selectSQL, null);
    }
    // Method to log out all users (reset `is_logged_in`)
    public void logOutAllUsers() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_USER + " SET " + COLUMN_IS_LOGGED_IN + "=0";
        db.execSQL(query);
        db.close();
    }

    // Method to check if the user with given email and password exists
    public boolean isValidUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USER + " WHERE " + COLUMN_EMAIL + "=? AND " + COLUMN_PASSWORD + "=?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            cursor.close();
            return true; // User found
        }
        cursor.close();
        return false; // User not found
    }
}
