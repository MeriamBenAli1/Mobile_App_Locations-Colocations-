package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenChat = findViewById(R.id.btn_open_chat);
        btnOpenChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the fragment instead of an activity
                ReservationDetailsFragment paymentFragment = new ReservationDetailsFragment();

                // Pass necessary data as arguments (for example, reservation details)
                Bundle bundle = new Bundle();
                bundle.putString("firstName", "John");
                bundle.putString("lastName", "Doe");
                bundle.putString("email", "john.doe@example.com");
                bundle.putString("phone", "1234567890");
                bundle.putString("date", "2024-11-12");
                bundle.putInt("guests", 2);
                bundle.putInt("duration", 3);
                bundle.putString("durationType", "nights");
                bundle.putDouble("totalPrice", 300.0);
                paymentFragment.setArguments(bundle);

                // Load the fragment
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, paymentFragment);
                transaction.addToBackStack(null); // Optional: adds fragment to back stack for navigation
                transaction.commit();
            }
        });
        Button btnOpenHistory = findViewById(R.id.btn_history);
        btnOpenHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReservationHistoryFragment historyFragment = new ReservationHistoryFragment();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, historyFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
    }

}
