package com.example.mobileappproject_tekdev_5sae3.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import androidx.room.Delete;


import com.example.mobileappproject_tekdev_5sae3.Entity.Publication;

import java.util.List;

@Dao
public interface PublicationDao {

    @Insert
    void insertPublication(Publication publication);

    @Update
    void updatePublication(Publication publication);

    @Delete
    void deletePublication(Publication publication);
    @Query("SELECT * FROM publications")
    List<Publication> getAllPublications();

    @Query("UPDATE publications SET likeCount = :likeCount WHERE id = :publicationId")
    void updateLikeCount(int publicationId, int likeCount);

}