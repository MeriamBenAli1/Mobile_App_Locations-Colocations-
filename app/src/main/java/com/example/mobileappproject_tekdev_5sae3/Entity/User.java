package com.example.mobileappproject_tekdev_5sae3.Entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.List;
import androidx.room.Ignore;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String name;
    private String email;

    // Constructeur principal
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }

    // Getters et setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
