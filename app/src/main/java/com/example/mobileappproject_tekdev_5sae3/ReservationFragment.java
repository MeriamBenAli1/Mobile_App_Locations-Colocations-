package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappproject_tekdev_5sae3.R;

public class ReservationFragment extends Fragment {

    private EditText firstName, lastName, email, phone;

    public ReservationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_reservation, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
        firstName = view.findViewById(R.id.editText_firstName_event_booking);
        lastName = view.findViewById(R.id.editText_lastName_event_booking);
        email = view.findViewById(R.id.editText_email_event_booking);
        phone = view.findViewById(R.id.editText_phoneNumber_event_booking);

        // Retrieve data passed via the arguments
        if (getArguments() != null) {
            firstName.setText(getArguments().getString("firstName"));
            lastName.setText(getArguments().getString("lastName"));
            email.setText(getArguments().getString("email"));
            phone.setText(getArguments().getString("phone"));
        }

        Button btnNextStep = view.findViewById(R.id.nextStepButton);

        btnNextStep.setOnClickListener(v -> {
            // Collect input data
            String firstNameInput = firstName.getText().toString();
            String lastNameInput = lastName.getText().toString();
            String emailInput = email.getText().toString();
            String phoneInput = phone.getText().toString();

            // Create a Bundle to pass data to ReservationPaymentFragment
            Bundle bundle = new Bundle();
            bundle.putString("firstName", firstNameInput);
            bundle.putString("lastName", lastNameInput);
            bundle.putString("email", emailInput);
            bundle.putString("phone", phoneInput);

            // Pass any other required data from arguments to the bundle
            if (getArguments() != null) {
                bundle.putString("date", getArguments().getString("date"));
                bundle.putInt("guests", getArguments().getInt("guests", 1));
                bundle.putInt("duration", getArguments().getInt("duration", 1));
                bundle.putString("durationType", getArguments().getString("durationType"));
                bundle.putDouble("totalPrice", getArguments().getDouble("totalPrice"));
                bundle.putString("paymentMethod", getArguments().getString("paymentMethod"));
            }

            // Initialize ReservationPaymentFragment and set the bundle as arguments
            ReservationPaymentFragment reservationPaymentFragment = new ReservationPaymentFragment();
            reservationPaymentFragment.setArguments(bundle);

            // Replace the current fragment with ReservationPaymentFragment
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, reservationPaymentFragment); // Use the container ID for your fragment
            transaction.addToBackStack(null); // Adds to the back stack so the user can navigate back
            transaction.commit();
        });

    }
        // Method to set arguments when initializing the fragment
    public static ReservationFragment newInstance(String firstName, String lastName, String email, String phone, String date, int guests, int duration, String durationType, double totalPrice, String paymentMethod) {
        ReservationFragment fragment = new ReservationFragment();
        Bundle args = new Bundle();
        args.putString("firstName", firstName);
        args.putString("lastName", lastName);
        args.putString("email", email);
        args.putString("phone", phone);
        args.putString("date", date);
        args.putInt("guests", guests);
        args.putInt("duration", duration);
        args.putString("durationType", durationType);
        args.putDouble("totalPrice", totalPrice);
        args.putString("paymentMethod", paymentMethod);
        fragment.setArguments(args);
        return fragment;
    }
}
