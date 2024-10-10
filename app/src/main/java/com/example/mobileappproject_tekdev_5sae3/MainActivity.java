package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpenChat = findViewById(R.id.btn_open_chat);
        btnOpenChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir ChatActivity
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                startActivity(intent);
            }
        });

        // Ajouter le bouton pour ouvrir les conversations
        Button btnOpenConversations = findViewById(R.id.btn_open_conversations);
        btnOpenConversations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir ConversationsActivity
                Intent intent = new Intent(MainActivity.this, ConversationsActivity.class);
                startActivity(intent);
            }
        });
    }
}
