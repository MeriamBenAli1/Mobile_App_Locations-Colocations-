package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;


import com.example.mobileappproject_tekdev_5sae3.dao.ReservationDao;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.reservation.ReservationDetailsActivity;

public class MainActivity extends AppCompatActivity {
    private ReservationDao reservationDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenChat = findViewById(R.id.btn_open_chat);
        btnOpenChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ReservationDetailsActivity.class);
                startActivity(intent);
            }
        });
        // Obtenir une instance de la base de données
        AppDataBase db = AppDataBase.getInstance(this);
        reservationDao = db.reservationDao();

        // Log pour vérifier l'instance de la base de données
        Log.d("Database", "Instance de la base de données obtenue");




    }
}