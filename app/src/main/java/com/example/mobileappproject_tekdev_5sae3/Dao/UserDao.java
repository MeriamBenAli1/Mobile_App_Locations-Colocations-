package com.example.mobileappproject_tekdev_5sae3.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.mobileappproject_tekdev_5sae3.Entity.User;
import java.util.List;

@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);
    @Update
    void updateUser(User user);
    @Query("SELECT * FROM user WHERE email = :email LIMIT 1")
    User getUserByEmail(String email);
    @Query("SELECT name FROM user WHERE email = :email LIMIT 1")
    String getUserNameByEmail(String email);
    @Query("SELECT COUNT(*) FROM user WHERE email = :email AND password = :password")
    int isValidUser(String email, String password);

    @Query("UPDATE user SET is_logged_in = 1 WHERE email = :email")
   void logInUser(String email);

    @Query("SELECT * FROM user")
    List<User> getAllUsers();
}
