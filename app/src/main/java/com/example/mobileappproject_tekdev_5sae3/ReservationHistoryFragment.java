package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationHistoryFragment extends Fragment implements ReservationAdapter.OnReservationDeleteListener {

    private RecyclerView recyclerView;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_reservation_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Initialize the adapter with the delete listener and set it to the RecyclerView
        reservationAdapter = new ReservationAdapter(reservationsList, this);
        recyclerView.setAdapter(reservationAdapter);

        // Load reservations from the database
        loadReservations();
    }

    // Method to load reservations from the database
    private void loadReservations() {
        AppDataBase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                        AppDataBase.class, "reservation-database")
                .fallbackToDestructiveMigration()
                .build();

        new Thread(() -> {
            List<Reservation> reservations = db.reservationDao().getAllReservations();

            // Log the size of the list
            Log.d("ReservationHistory", "Loaded " + reservations.size() + " reservations from the database.");

            getActivity().runOnUiThread(() -> {
                // Clear the current list and add the new ones
                reservationsList.clear();
                if (reservations != null && !reservations.isEmpty()) {
                    reservationsList.addAll(reservations);
                    reservationAdapter.notifyDataSetChanged();
                } else {
                    Log.d("ReservationHistory", "No reservations found.");
                }
            });
        }).start();
    }

    @Override
    public void onReservationDelete(@NonNull Reservation reservation) {
        // Perform the deletion of reservation
        deleteReservation(reservation);
    }

    // Method to delete reservation from the database
    private void deleteReservation(@NonNull Reservation reservation) {
        new Thread(() -> {
            AppDataBase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                            AppDataBase.class, "reservation-database")
                    .fallbackToDestructiveMigration()
                    .build();
            db.reservationDao().delete(reservation);  // Delete the reservation from the database

            getActivity().runOnUiThread(() -> {
                // Show a toast message and reload the reservations
                Toast.makeText(getActivity(), "Reservation deleted", Toast.LENGTH_SHORT).show();
                loadReservations();  // Refresh the list after deletion
            });
        }).start();
    }
}
