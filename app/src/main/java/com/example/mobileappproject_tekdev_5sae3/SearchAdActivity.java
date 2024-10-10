package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class SearchAdActivity extends AppCompatActivity {

    private EditText editLocation, editPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ad);

        editLocation = findViewById(R.id.editLocation);
        editPrice = findViewById(R.id.editPrice);
        Button btnSearch = findViewById(R.id.btnSearch);

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editLocation.getText().toString();
                String price = editPrice.getText().toString();

                // Ajoutez ici la logique pour rechercher des annonces
                // Par exemple, afficher une notification ou appeler une méthode pour filtrer les annonces

                Toast.makeText(SearchAdActivity.this, "Recherche d'annonces à " + location + " avec un prix maximum de " + price, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
