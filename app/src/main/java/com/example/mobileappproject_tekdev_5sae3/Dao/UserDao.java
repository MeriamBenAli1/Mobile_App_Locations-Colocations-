package com.example.mobileappproject_tekdev_5sae3.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.mobileappproject_tekdev_5sae3.Entity.User;


@Dao
public interface UserDao {

    @Insert
    void insertUser(User user);

}
