package com.example.mobileappproject_tekdev_5sae3.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappproject_tekdev_5sae3.R;

public class ReservationActivity extends AppCompatActivity {
    // Déclaration des EditText pour capturer les informations de l'utilisateur
    private EditText firstName, lastName, email, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        // Initialisation des vues avec findViewById
        firstName = findViewById(R.id.editText_firstName_event_booking);
        lastName = findViewById(R.id.editText_lastName_event_booking);
        email = findViewById(R.id.editText_email_event_booking);
        phone = findViewById(R.id.editText_phoneNumber_event_booking);

        // Récupération des données passées via Intent
        String firstNameReceived = getIntent().getStringExtra("firstName");
        String lastNameReceived = getIntent().getStringExtra("lastName");
        String emailReceived = getIntent().getStringExtra("email");
        String phoneReceived = getIntent().getStringExtra("phone");
        String date = getIntent().getStringExtra("date");
        int guests = getIntent().getIntExtra("guests", 1);
        int duration = getIntent().getIntExtra("duration", 1);
        String durationType = getIntent().getStringExtra("durationType");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        String selectedPaymentMethod = getIntent().getStringExtra("paymentMethod");

        // Initialiser les champs avec les valeurs passées
        if (firstNameReceived != null) {
            firstName.setText(firstNameReceived);
        }
        if (lastNameReceived != null) {
            lastName.setText(lastNameReceived);
        }
        if (emailReceived != null) {
            email.setText(emailReceived);
        }
        if (phoneReceived != null) {
            phone.setText(phoneReceived);
        }

        // Bouton pour aller à l'étape suivante
        Button btnNextStep = findViewById(R.id.nextStepButton);

        // Navigation vers la prochaine étape
        btnNextStep.setOnClickListener(v -> {
            // Récupération des informations saisies
            String firstNameInput = firstName.getText().toString();
            String lastNameInput = lastName.getText().toString();
            String emailInput = email.getText().toString();
            String phoneInput = phone.getText().toString();

            // Création d'un Intent pour passer les données à l'activité suivante
            Intent intent = new Intent(ReservationActivity.this, ReservationPaymentActivity.class);
            intent.putExtra("firstName", firstNameInput);
            intent.putExtra("lastName", lastNameInput);
            intent.putExtra("email", emailInput);
            intent.putExtra("phone", phoneInput);
            intent.putExtra("date", date);
            intent.putExtra("guests", guests);
            intent.putExtra("duration", duration);
            intent.putExtra("durationType", durationType);
            intent.putExtra("totalPrice", totalPrice);
            intent.putExtra("paymentMethod", selectedPaymentMethod);

            startActivity(intent);
        });
    }
}
