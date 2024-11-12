package com.example.mobileappproject_tekdev_5sae3.Entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int userId;

    private String name;
    private String email;
    private String password;  // Add password field
    @ColumnInfo(name = "is_logged_in", defaultValue = "0")
    private int isLoggedIn;
    private String otp; // OTP code for password reset
    @ColumnInfo(name = "otp_timestamp")
    private long otpTimestamp; // Timestamp for OTP generation

    public String getOtp() {
        return otp;
    }

    public long getOtpTimestamp() {
        return otpTimestamp;
    }

    public void setOtpTimestamp(long otpTimestamp) {
        this.otpTimestamp = otpTimestamp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    // Main constructor
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public int getIsLoggedIn() {
        return isLoggedIn;
    }

    public void setIsLoggedIn(int isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
}
