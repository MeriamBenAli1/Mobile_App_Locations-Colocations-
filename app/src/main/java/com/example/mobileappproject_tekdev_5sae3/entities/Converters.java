package com.example.mobileappproject_tekdev_5sae3.entities;

import androidx.room.TypeConverter;
import java.util.Date;

public class Converters {

    @TypeConverter
    public static Long fromDate(Date date) {
        return date != null ? date.getTime() : null;
    }

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp != null ? new Date(timestamp) : null;
    }
}
