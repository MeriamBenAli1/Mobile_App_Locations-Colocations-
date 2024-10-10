package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associe le fichier XML à cette activité
        setContentView(R.layout.chat);
    }
}
