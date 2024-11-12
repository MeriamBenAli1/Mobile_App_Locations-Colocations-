package com.example.mobileappproject_tekdev_5sae3.reservation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import com.example.mobileappproject_tekdev_5sae3.R;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.entity.Reservation;

public class ReservationPaymentActivity extends AppCompatActivity {

    // Déclaration des champs d'édition et RadioGroup
    private EditText cardNumberEditText, expiryDateEditText, cvvEditText;
    private RadioGroup paymentMethodGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_payment);

        // Initialisation des champs de texte et du RadioGroup
        cardNumberEditText = findViewById(R.id.card_number);
        expiryDateEditText = findViewById(R.id.expiry_date);
        cvvEditText = findViewById(R.id.cvv);
        paymentMethodGroup = findViewById(R.id.payment_method_group);

        // Récupération des données de l'activité précédente
        String firstName = getIntent().getStringExtra("firstName");
        String lastName = getIntent().getStringExtra("lastName");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        String date = getIntent().getStringExtra("date");
        int guests = getIntent().getIntExtra("guests", 1);
        int duration = getIntent().getIntExtra("duration", 1);
        String durationType = getIntent().getStringExtra("durationType");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        // Bouton pour sauvegarder la réservation
        Button btnSaveReservation = findViewById(R.id.nextStepButton);
        btnSaveReservation.setOnClickListener(v -> {
            // Récupérer les données saisies par l'utilisateur dans les champs de texte
            String cardNumber = cardNumberEditText.getText().toString().trim();
            String expiryDate = expiryDateEditText.getText().toString().trim();
            String cvv = cvvEditText.getText().toString().trim();

            // Vérification de la validité des informations
            if (cardNumber.isEmpty() || expiryDate.isEmpty() || cvv.isEmpty()) {
                Toast.makeText(ReservationPaymentActivity.this, "Please fill all payment details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Récupérer le mode de paiement sélectionné
            int selectedPaymentMethodId = paymentMethodGroup.getCheckedRadioButtonId();
            RadioButton selectedPaymentMethodButton = findViewById(selectedPaymentMethodId);
            String paymentMethod = selectedPaymentMethodButton != null ? selectedPaymentMethodButton.getText().toString() : "";

            // Vérification du mode de paiement
            if (paymentMethod.isEmpty()) {
                Toast.makeText(ReservationPaymentActivity.this, "Please select a payment method", Toast.LENGTH_SHORT).show();
                return;
            }

            // Créer une instance de la réservation avec toutes les données
            Reservation reservation = new Reservation(
                    firstName, lastName, email, phone, date, guests, duration,
                    durationType, totalPrice, paymentMethod, cardNumber, expiryDate, cvv
            );

            // Accéder à la base de données et insérer la réservation
            AppDataBase db = Room.databaseBuilder(getApplicationContext(),
                            AppDataBase.class, "reservation-database")
                    .fallbackToDestructiveMigration()
                    .build();
            new Thread(() -> {
                db.reservationDao().insert(reservation);
                runOnUiThread(() -> {
                    // Afficher un message ou rediriger l'utilisateur après l'enregistrement
                    Toast.makeText(ReservationPaymentActivity.this,
                            "Reservation saved successfully", Toast.LENGTH_SHORT).show();
                    finish(); // Fermer l'activité actuelle et revenir à l'écran précédent
                });
            }).start();
        });
    }
}
