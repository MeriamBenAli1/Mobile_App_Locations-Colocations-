package com.example.mobileappproject_tekdev_5sae3.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ColocoDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Amine.db";
    private static final int DATABASE_VERSION = 2;

    // Table et colonnes
    private static final String TABLE_PUBLICATION = "publications";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";

    // Créez votre table ici
    private static final String CREATE_TABLE_PUBLICATION = "CREATE TABLE " + TABLE_PUBLICATION + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_TITLE + " TEXT)";

    public ColocoDataBase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_PUBLICATION);
        Log.d("Database", "Table publications created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PUBLICATION);
        onCreate(db);
    }

    // Méthode pour insérer une publication
    public void insertPublication(String title) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertSQL = "INSERT INTO " + TABLE_PUBLICATION + " (" + COLUMN_TITLE + ") VALUES ('" + title + "')";
        db.execSQL(insertSQL);
        Log.d("Database", "Publication inserted: " + title);
        db.close();
    }
}

