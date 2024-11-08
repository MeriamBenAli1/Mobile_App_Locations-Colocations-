package com.example.mobileappproject_tekdev_5sae3.ChatsController;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappproject_tekdev_5sae3.R;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;
import com.example.mobileappproject_tekdev_5sae3.entity.message;

public class ChatActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Associe le fichier XML à cette activité
        setContentView(R.layout.chat);

        // Accès à la base de données
        AppDataBase db = AppDataBase.getInstance(this);

        // Exemple d'insertion d'un message
        message newMessage = new message("senderName", "receiverName", "Hello, how are you?", System.currentTimeMillis());
        new Thread(() -> db.messageDAO().insertMessage(newMessage)).start();  // Insertion dans un thread séparé

    }
}