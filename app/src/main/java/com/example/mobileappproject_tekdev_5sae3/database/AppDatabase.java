package com.example.mobileappproject_tekdev_5sae3.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import android.content.Context;

import com.example.mobileappproject_tekdev_5sae3.DAO.AnnonceDao;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;
import com.example.mobileappproject_tekdev_5sae3.entities.Converters;

@Database(entities = {Annonce.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase instance;

    public abstract AnnonceDao annonceDao();

    public static  synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "Annonces_database")

                    .build();
        }
        return instance;
    }
}
