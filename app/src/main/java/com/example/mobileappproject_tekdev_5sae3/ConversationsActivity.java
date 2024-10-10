package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;

public class ConversationsActivity extends AppCompatActivity {

    private ListView conversationsListView;
    private String[] conversations = {"Person 1", "Person 2", "Person 3"};  // Liste des noms de contacts

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversations);

        conversationsListView = findViewById(R.id.conversations_list_view);

        // Utilisation de l'adaptateur personnalisé ConversationsAdapter
        ConversationsAdapter adapter = new ConversationsAdapter(this, conversations);
        conversationsListView.setAdapter(adapter);

        // Gestion du clic sur un élément de la liste
        conversationsListView.setOnItemClickListener((parent, view, position, id) -> {
            String selectedConversation = conversations[position];
            Intent intent = new Intent(ConversationsActivity.this, ChatActivity.class);
            intent.putExtra("conversation_name", selectedConversation);  // Passer le nom de la personne à ChatActivity
            startActivity(intent);
        });
    }
}
