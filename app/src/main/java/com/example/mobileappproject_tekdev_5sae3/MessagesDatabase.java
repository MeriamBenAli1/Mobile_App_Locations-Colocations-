package com.example.mobileappproject_tekdev_5sae3;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessagesDatabase extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "messages.db";
    public static final int DATABASE_VERSION = 1;

    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_MESSAGE_TEXT = "message_text";
    public static final String COLUMN_MESSAGE_SENDER = "sender";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_MESSAGES + " (" +
                    "_id INTEGER PRIMARY KEY," +
                    COLUMN_MESSAGE_TEXT + " TEXT," +
                    COLUMN_MESSAGE_SENDER + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TABLE_MESSAGES;

    public MessagesDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}

