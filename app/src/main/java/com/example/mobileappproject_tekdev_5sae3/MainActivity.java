package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappproject_tekdev_5sae3.ChatsController.ConversationsActivity;
import com.example.mobileappproject_tekdev_5sae3.DAO.MessageDAO;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.entity.message;

public class MainActivity extends AppCompatActivity {


    private MessageDAO messageDAO;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



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

        // Ajouter le bouton pour ouvrir les conversations
        Button btnOpenAvis = findViewById(R.id.btn_open_conversations);
        btnOpenConversations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ouvrir ConversationsActivity
                Intent intent = new Intent(MainActivity.this, ConversationsActivity.class);
                startActivity(intent);
            }
        });


        //meriambase
        // Obtenir une instance de la base de données
        AppDataBase db = AppDataBase.getInstance(this);
        messageDAO = db.messageDAO();

        // Log pour vérifier l'instance de la base de données
        Log.d("Database", "Instance de la base de données obtenue");

        // Insérer une publication pour tester
        message msg = new message();


        // Insertion dans la base de données
        new Thread(() -> {

            Log.d("Database", "message added ");
        }).start();
    }
    }
