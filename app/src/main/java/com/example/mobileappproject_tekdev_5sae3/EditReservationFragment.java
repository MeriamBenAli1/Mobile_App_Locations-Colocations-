package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobileappproject_tekdev_5sae3.dao.ReservationDao;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;

public class EditReservationFragment extends Fragment {

    // Declare only the required fields
    private EditText editTextPhone, editTextDate, editTextGuests, editTextDuration, editTextDurationType;
    private Button btnSaveReservation;

    // Reservation to edit
    private Reservation reservationToEdit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_reservation, container, false);

        // Initialize only the required EditText fields
        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextGuests = view.findViewById(R.id.editTextGuests);
        editTextDuration = view.findViewById(R.id.editTextDuration);
        editTextDurationType = view.findViewById(R.id.editTextDurationType);
        btnSaveReservation = view.findViewById(R.id.btnSaveReservation);

        // If a reservation exists, load its information into the fields
        if (reservationToEdit != null) {
            editTextPhone.setText(reservationToEdit.getPhone());
            editTextDate.setText(reservationToEdit.getDate());
            editTextGuests.setText(String.valueOf(reservationToEdit.getGuests()));
            editTextDuration.setText(String.valueOf(reservationToEdit.getDuration()));
            editTextDurationType.setText(reservationToEdit.getDurationType());
        }

        // Action for the save button
        btnSaveReservation.setOnClickListener(v -> {
            // Get the values from the fields
            String phone = editTextPhone.getText().toString();
            String date = editTextDate.getText().toString();
            int guests = Integer.parseInt(editTextGuests.getText().toString());
            int duration = Integer.parseInt(editTextDuration.getText().toString());
            String durationType = editTextDurationType.getText().toString();

            // Create the updated reservation object
            Reservation updatedReservation = new Reservation(
                    reservationToEdit.getFirstName(), reservationToEdit.getLastName(),
                    reservationToEdit.getEmail(), phone, date, guests, duration, durationType,
                    reservationToEdit.getAmount(), reservationToEdit.getPaymentMethod(),
                    reservationToEdit.getCardNumber(), reservationToEdit.getExpiryDate(),
                    reservationToEdit.getCvv()
            );

            // Update the reservation in the database
            updateReservation(updatedReservation);

            // Show a success message
            Toast.makeText(getContext(), "Réservation modifiée", Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void updateReservation(Reservation updatedReservation) {
        // Get the DAO instance from the Room database
        ReservationDao reservationDao = AppDataBase.getInstance(getContext()).reservationDao();

        // Run the update in a background thread (database operations should not run on the main thread)
        new Thread(() -> {
            reservationDao.update(updatedReservation);
        }).start();
    }

    public void setReservationToEdit(Reservation reservation) {
        this.reservationToEdit = reservation;
    }
}
