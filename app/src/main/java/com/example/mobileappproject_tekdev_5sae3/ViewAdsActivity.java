package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewAdsActivity extends AppCompatActivity implements AdsAdapter.OnAdClickListener {

    private RecyclerView recyclerView;
    private AdsAdapter adsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_ads);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load ads from the database
        loadAdsFromDatabase();

        // Set up SearchView to filter results
        setupSearchView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAdsFromDatabase(); // Reload ads when returning to the activity
    }

    private void loadAdsFromDatabase() {
        new Thread(() -> {
            List<Annonce> adsList = AppDatabase.getInstance(ViewAdsActivity.this).annonceDao().getAllAnnonces();
            runOnUiThread(() -> {
                if (adsList != null && !adsList.isEmpty()) {
                    adsAdapter = new AdsAdapter(ViewAdsActivity.this, adsList, this);
                    recyclerView.setAdapter(adsAdapter);
                } else {
                    Toast.makeText(ViewAdsActivity.this, "No ads found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void setupSearchView() {
        SearchView searchView = findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adsAdapter != null) {
                    adsAdapter.getFilter().filter(query);
                }
                return true; // Return true to indicate the query has been handled
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adsAdapter != null) {
                    adsAdapter.getFilter().filter(newText);
                }
                return true; // Return true to indicate the query has been handled
            }
        });
    }

    @Override
    public void onAdClick(@NonNull Annonce annonce) {
        Intent intent = new Intent(ViewAdsActivity.this, EditAdActivity.class);
        intent.putExtra("adId", annonce.getId());
        intent.putExtra("adTitle", annonce.getTitle());
        intent.putExtra("adDescription", annonce.getDescription());
        intent.putExtra("adPrice", annonce.getPrice());
        intent.putExtra("adDate", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(annonce.getDate()))); // Convert date to string
        intent.putExtra("adImageUri", annonce.getImageUri());
        startActivity(intent);
    }

    @Override
    public void onAdDelete(@NonNull Annonce ad) {
        deleteAnnonce(ad);
    }

    public void deleteAnnonce(@NonNull Annonce ad) {
        new Thread(() -> {
            AppDatabase.getInstance(this).annonceDao().deleteAnnonce(ad);
            runOnUiThread(() -> {
                Toast.makeText(ViewAdsActivity.this, "Annonce deleted", Toast.LENGTH_SHORT).show();
                loadAdsFromDatabase(); // Refresh the ads list after deletion
            });
        }).start();
    }
}
