package com.example.mobileappproject_tekdev_5sae3.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;
import java.util.List;

@Dao
public interface AnnonceDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insertAnnonce(Annonce annonce);

    @Delete
    void deleteAnnonce(Annonce annonce);

    @Query("SELECT * FROM annonces")
    List<Annonce> getAllAnnonces();


    @Update
    int updateAnnonce(Annonce annonce);

    @Query("SELECT * FROM annonces WHERE id = :id")
    Annonce getAnnonceById(int id);  // Added method
}
