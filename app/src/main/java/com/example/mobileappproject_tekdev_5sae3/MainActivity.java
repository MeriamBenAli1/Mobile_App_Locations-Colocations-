package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileappproject_tekdev_5sae3.DAO.AnnonceDao;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;

public class MainActivity extends AppCompatActivity {
    private AnnonceDao annonceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialisation de la base de données
        AppDatabase db = AppDatabase.getInstance(this);
        annonceDao = db.annonceDao();

        // Récupération des boutons
        Button btnGoToPublish = findViewById(R.id.btnGoToPublish);
        Button btnGoToSearch = findViewById(R.id.btnGoToSearch);

        // Action pour le bouton de publication
        btnGoToPublish.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PublishAdActivity.class);
            startActivity(intent);  // Lancer l'activité de publication
        });

        // Action pour le bouton de recherche
        btnGoToSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, SearchAdActivity.class);
            startActivity(intent);
        });
    }

}