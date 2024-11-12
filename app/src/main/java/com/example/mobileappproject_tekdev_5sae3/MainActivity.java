package com.example.mobileappproject_tekdev_5sae3;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Si l'activité est lancée pour la première fois, on remplace le fragment par défaut
        if (savedInstanceState == null) {
            ConversationsFragment conversationsFragment = new ConversationsFragment();
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, conversationsFragment);
            transaction.commit();
        }
    }
}
