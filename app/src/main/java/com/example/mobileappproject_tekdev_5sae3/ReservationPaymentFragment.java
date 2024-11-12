package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.room.Room;

import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;

public class ReservationPaymentFragment extends Fragment {

    private EditText cardNumberEditText, expiryDateEditText, cvvEditText;
    private RadioGroup paymentMethodGroup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View view = inflater.inflate(R.layout.fragment_reservation_payement, container, false);

        // Initialize views
        cardNumberEditText = view.findViewById(R.id.card_number);
        expiryDateEditText = view.findViewById(R.id.expiry_date);
        cvvEditText = view.findViewById(R.id.cvv);
        paymentMethodGroup = view.findViewById(R.id.payment_method_group);

        // Retrieve data from arguments (replace with your actual data)
        String firstName = getArguments().getString("firstName");
        String lastName = getArguments().getString("lastName");
        String email = getArguments().getString("email");
        String phone = getArguments().getString("phone");
        String date = getArguments().getString("date");
        int guests = getArguments().getInt("guests", 1);
        int duration = getArguments().getInt("duration", 1);
        String durationType = getArguments().getString("durationType");
        double totalPrice = getArguments().getDouble("totalPrice", 0.0);

        // Save reservation button click listener
        Button btnSaveReservation = view.findViewById(R.id.nextStepButton);
        btnSaveReservation.setOnClickListener(v -> {
            String cardNumber = cardNumberEditText.getText().toString().trim();
            String expiryDate = expiryDateEditText.getText().toString().trim();
            String cvv = cvvEditText.getText().toString().trim();

            // Validate input
            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(getActivity(), "Please fill all payment details", Toast.LENGTH_SHORT).show();
                return;
            }

            int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
            RadioButton selectedPaymentMethodButton = view.findViewById(selectedPaymentMethodId);
            String paymentMethod = selectedPaymentMethodButton != null ? selectedPaymentMethodButton.getText().toString() : "";

            if (paymentMethod.isEmpty()) {
                Toast.makeText(getActivity(), "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create the reservation object
            Reservation reservation = new Reservation(
                    firstName, lastName, email, phone, date, guests, duration,
                    durationType, totalPrice, paymentMethod, cardNumber, expiryDate, cvv
            );

            // Access the database and insert the reservation
            AppDataBase db = Room.databaseBuilder(getActivity().getApplicationContext(),
                            AppDataBase.class, "reservation-database")
                    .fallbackToDestructiveMigration()
                    .build();
            new Thread(() -> {
                db.reservationDao().insert(reservation);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Reservation saved successfully", Toast.LENGTH_SHORT).show();
                    // Optionally, navigate to another fragment or activity here
                });
            }).start();
        });

        return view;
    }
}
