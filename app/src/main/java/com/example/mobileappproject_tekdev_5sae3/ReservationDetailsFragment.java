package com.example.mobileappproject_tekdev_5sae3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappproject_tekdev_5sae3.R;

import java.util.Calendar;

public class ReservationDetailsFragment extends Fragment {
    private TextView tvSummaryDate, tvSummaryGuests, tvSummaryDuration, tvSummaryPrice;
    private EditText etReservationDate, etGuests, eDuration;
    private CheckBox cbMonths, cbNights;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_reservation_details, container, false);

        // Initialize the views
        etReservationDate = rootView.findViewById(R.id.etReservationDate);
        etGuests = rootView.findViewById(R.id.etGuests);
        eDuration = rootView.findViewById(R.id.eDuration);
        cbMonths = rootView.findViewById(R.id.cbMonths);
        cbNights = rootView.findViewById(R.id.cbNights);
        tvSummaryDate = rootView.findViewById(R.id.tvSummaryDate);
        tvSummaryGuests = rootView.findViewById(R.id.tvSummaryGuests);
        tvSummaryDuration = rootView.findViewById(R.id.tvSummaryDuration);
        tvSummaryPrice = rootView.findViewById(R.id.tvSummaryPrice);

        // Set up listeners
        setupListeners(rootView);

        Button btnNextStep = rootView.findViewById(R.id.btnNextStep);
        Button btnIncreaseGuests = rootView.findViewById(R.id.btnIncreaseGuests);
        Button btnDecreaseGuests = rootView.findViewById(R.id.btnDecreaseGuests);
        Button btnIncreaseDuration = rootView.findViewById(R.id.btnIncreaseDuration);
        Button btnDecreaseDuration = rootView.findViewById(R.id.btnDecreaseDuration);

        // Increment/Decrement the number of guests
        btnIncreaseGuests.setOnClickListener(v -> updateGuestsCount(true));
        btnDecreaseGuests.setOnClickListener(v -> updateGuestsCount(false));

        // Increment/Decrement the duration
        btnIncreaseDuration.setOnClickListener(v -> updateDuration(true));
        btnDecreaseDuration.setOnClickListener(v -> updateDuration(false));

        // Navigate to the next step
        btnNextStep.setOnClickListener(v -> {
            try {
                // Example of price calculation based on guests and duration
                int guests = Integer.parseInt(etGuests.getText().toString());
                int duration = Integer.parseInt(eDuration.getText().toString());
                double pricePerGuest = 20.0;
                double totalPrice = guests * duration * pricePerGuest;

                // Create a Bundle to pass data to ReservationFragment
                Bundle bundle = new Bundle();
                bundle.putString("date", etReservationDate.getText().toString());
                bundle.putInt("guests", guests);
                bundle.putInt("duration", duration);
                bundle.putString("durationType", cbMonths.isChecked() ? "months" : "nights");
                bundle.putDouble("totalPrice", totalPrice);

                // Initialize ReservationFragment and set arguments
                ReservationFragment reservationFragment = new ReservationFragment();
                reservationFragment.setArguments(bundle);

                // Replace current fragment with ReservationFragment
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, reservationFragment); // Replace with the new fragment
                transaction.addToBackStack(null); // Add to back stack for navigation
                transaction.commit();

            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "Invalid input for price calculation", Toast.LENGTH_SHORT).show();
            }
        });

        // Date picker
        etReservationDate.setOnClickListener(v -> showDatePicker());

        return rootView;
    }

    private void setupListeners(View rootView) {
        // Update the number of guests in the summary
        etGuests.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String guests = editable.toString();
                tvSummaryGuests.setText("Guests: " + guests);
            }
        });

        // Update the duration and duration type in the summary
        eDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void afterTextChanged(Editable editable) {
                String duration = editable.toString();
                String durationType = cbMonths.isChecked() ? "months" : "nights";
                tvSummaryDuration.setText("Duration: " + duration + " " + durationType);
            }
        });

        // Checkbox listener for duration type
        cbMonths.setOnCheckedChangeListener((buttonView, isChecked) -> updateDurationType(isChecked, "months"));
        cbNights.setOnCheckedChangeListener((buttonView, isChecked) -> updateDurationType(isChecked, "nights"));
    }

    private void updateGuestsCount(boolean increment) {
        int currentGuests = etGuests.getText().toString().isEmpty() ? 1 : Integer.parseInt(etGuests.getText().toString());
        etGuests.setText(String.valueOf(increment ? currentGuests + 1 : Math.max(currentGuests - 1, 1)));
    }

    private void updateDuration(boolean increment) {
        int currentDuration = eDuration.getText().toString().isEmpty() ? 1 : Integer.parseInt(eDuration.getText().toString());
        eDuration.setText(String.valueOf(increment ? currentDuration + 1 : Math.max(currentDuration - 1, 1)));
    }

    private void updateDurationType(boolean isChecked, String type) {
        if (isChecked) {
            cbMonths.setChecked(type.equals("months"));
            cbNights.setChecked(type.equals("nights"));
            String duration = eDuration.getText().toString();
            tvSummaryDuration.setText("Duration: " + duration + " " + type);
        }
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    etReservationDate.setText(selectedDate);
                    tvSummaryDate.setText("Date: " + selectedDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
