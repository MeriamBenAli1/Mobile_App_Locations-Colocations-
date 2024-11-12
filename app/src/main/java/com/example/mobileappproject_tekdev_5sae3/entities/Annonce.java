package com.example.mobileappproject_tekdev_5sae3.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "annonces")
public class Annonce{
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "price")
    private int price;

    @ColumnInfo(name = "imageUri")
    private String imageUri;

    @ColumnInfo(name = "date")
    private long date; // Change Date to long for storage

    // Constructor
    public Annonce(String title, String description, int price, String imageUri, long date) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
        this.date = date;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    // To get Date object from long
    public Date getDateAsObject() {
        return new Date(date);
    }

    // To get formatted date as string
    public String getFormattedDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        return dateFormat.format(getDateAsObject());
    }
}
