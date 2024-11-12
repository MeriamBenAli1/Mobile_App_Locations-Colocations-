package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.mobileappproject_tekdev_5sae3.ReservationAdapter;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;

import java.util.ArrayList;
import java.util.List;

public class ReservationHistoryFragment extends Fragment {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ReservationAdapter reservationAdapter;
    private List<Reservation> reservationsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reservation_history, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewReservations);
        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);

        // Initialize the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        reservationAdapter = new ReservationAdapter(reservationsList);
        recyclerView.setAdapter(reservationAdapter);

        // Load reservations from database
        loadReservations();

        // Swipe to refresh listener
        swipeRefreshLayout.setOnRefreshListener(this::loadReservations);

        return view;
    }

    // Method to load reservations from the database
    private void loadReservations() {
        swipeRefreshLayout.setRefreshing(true);
        AppDataBase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                        AppDataBase.class, "reservation-database")
                .fallbackToDestructiveMigration()
                .build();

        new Thread(() -> {
            List<Reservation> reservations = db.reservationDao().getAllReservations();
            getActivity().runOnUiThread(() -> {
                reservationsList.clear();
                reservationsList.addAll(reservations);
                reservationAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            });
        }).start();
    }
}
