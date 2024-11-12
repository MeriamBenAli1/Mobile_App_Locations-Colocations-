package com.example.mobileappproject_tekdev_5sae3.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;
import java.util.List;

@Dao
public interface ReservationDao {

    // Insérer une nouvelle réservation
    @Insert
    void insert(Reservation reservation);

    // Mettre à jour une réservation existante
    @Update
    void update(Reservation reservation);

    // Supprimer une réservation
    @Delete
    void delete(Reservation reservation);

    // Récupérer toutes les réservations
    @Query("SELECT * FROM reservation_table")
    List<Reservation> getAllReservations();

    // Récupérer une réservation par son ID
    @Query("SELECT * FROM reservation_table WHERE id_reservation = :reservationId")
    Reservation getReservationById(int reservationId);
}