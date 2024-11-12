package com.example.mobileappproject_tekdev_5sae3.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mobileappproject_tekdev_5sae3.dao.ReservationDao;
import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;

@Database(entities = {Reservation.class}, version = 1, exportSchema = false)
public abstract class AppDataBase extends RoomDatabase {
    private static AppDataBase instance;

    public abstract ReservationDao reservationDao();

    public static AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, "DBColoko")
                    .allowMainThreadQueries()
                    .build();
        }
        return instance;
    }
}
