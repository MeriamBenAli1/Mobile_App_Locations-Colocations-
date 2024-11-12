package com.example.mobileappproject_tekdev_5sae3.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.example.mobileappproject_tekdev_5sae3.R;
import java.util.Calendar;

public class ReservationDetailsActivity extends AppCompatActivity {
    private TextView tvSummaryDate, tvSummaryGuests, tvSummaryDuration, tvSummaryPrice;
    private EditText etReservationDate, etGuests, eDuration;
    private CheckBox cbMonths, cbNights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_details);

        // Initialiser les vues
        etReservationDate = findViewById(R.id.etReservationDate);
        etGuests = findViewById(R.id.etGuests);
        eDuration = findViewById(R.id.eDuration);
        cbMonths = findViewById(R.id.cbMonths);
        cbNights = findViewById(R.id.cbNights);
        tvSummaryDate = findViewById(R.id.tvSummaryDate);
        tvSummaryGuests = findViewById(R.id.tvSummaryGuests);
        tvSummaryDuration = findViewById(R.id.tvSummaryDuration);
        tvSummaryPrice = findViewById(R.id.tvSummaryPrice);

        // Setup des listeners
        setupListeners();

        Button btnNextStep = findViewById(R.id.btnNextStep);
        Button btnIncreaseGuests = findViewById(R.id.btnIncreaseGuests);
        Button btnDecreaseGuests = findViewById(R.id.btnDecreaseGuests);
        Button btnIncreaseDuration = findViewById(R.id.btnIncreaseDuration);
        Button btnDecreaseDuration = findViewById(R.id.btnDecreaseDuration);

        // Incrémenter/Décrémenter le nombre d'invités
        btnIncreaseGuests.setOnClickListener(v -> updateGuestsCount(true));
        btnDecreaseGuests.setOnClickListener(v -> updateGuestsCount(false));

        // Incrémenter/Décrémenter la durée
        btnIncreaseDuration.setOnClickListener(v -> updateDuration(true));
        btnDecreaseDuration.setOnClickListener(v -> updateDuration(false));

        // Navigation vers la prochaine étape
        btnNextStep.setOnClickListener(v -> {
            String totalPriceString = tvSummaryPrice.getText().toString();


            double totalPrice = 0.0;
            try {
                // Exemple de calcul basé sur la durée et les invités
                int guests = Integer.parseInt(etGuests.getText().toString());
                int duration = Integer.parseInt(eDuration.getText().toString());
                double pricePerGuest = 20.0;
                totalPrice = guests * duration * pricePerGuest;
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(ReservationDetailsActivity.this, "Invalid input for price calculation", Toast.LENGTH_SHORT).show();
            }

            // Passer la valeur sous forme de Double
            Intent intent = new Intent(ReservationDetailsActivity.this, ReservationActivity.class);
            intent.putExtra("date", etReservationDate.getText().toString());
            intent.putExtra("guests", Integer.parseInt(etGuests.getText().toString()));
            intent.putExtra("duration", Integer.parseInt(eDuration.getText().toString()));
            intent.putExtra("durationType", cbMonths.isChecked() ? "months" : "nights");
            intent.putExtra("totalPrice", totalPrice);  // Passer en tant que Double
            startActivity(intent);
        });


        // Sélecteur de date
        etReservationDate.setOnClickListener(v -> showDatePicker());
    }

    private void setupListeners() {
        // Mise à jour de l'affichage des invités
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

        // Mise à jour de l'affichage de la durée
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

        // Écoute des changements de case pour le type de durée
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(ReservationDetailsActivity.this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    String selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                    etReservationDate.setText(selectedDate);
                    tvSummaryDate.setText("Date: " + selectedDate);
                }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
}
