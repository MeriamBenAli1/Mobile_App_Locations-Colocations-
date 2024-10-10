package com.example.mobileappproject_tekdev_5sae3.reservation;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappproject_tekdev_5sae3.R;

public class ReservationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associe le fichier XML à cette activité
        setContentView(R.layout.activity_reservation);
    }
}