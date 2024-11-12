package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappproject_tekdev_5sae3.DAO.AnnonceDao;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;

public class MainActivity extends AppCompatActivity {

    private AnnonceDao annonceDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the database
        AppDatabase db = AppDatabase.getInstance(this);
        annonceDao = db.annonceDao();

        // Check if the fragment is already in the container
        if (savedInstanceState == null) {
            // Load the fragment into the activity
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new MainFragment());
            transaction.commit();
        }
    }
}
