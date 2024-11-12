package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappproject_tekdev_5sae3.ChatFragment;
import com.example.mobileappproject_tekdev_5sae3.R;

import java.util.ArrayList;
import java.util.List;

public class ConversationsFragment extends Fragment {

    private ListView conversationsListView;
    private ArrayAdapter<String> adapter;
    private List<String> conversationsList;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater la vue
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);

        // Initialiser la ListView et les données
        conversationsListView = view.findViewById(R.id.conversations_list_view);  // Utiliser l'ID exact de la ListView
        conversationsList = new ArrayList<>();

        // Exemple de données (à remplacer par des données réelles)
        conversationsList.add("Conversation 1");
        conversationsList.add("Conversation 2");
        conversationsList.add("Conversation 3");

        // Créer un adaptateur pour la ListView
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, conversationsList);
        conversationsListView.setAdapter(adapter);

        // Définir l'écouteur de clics
        conversationsListView.setOnItemClickListener((parent, view1, position, id) -> {
            // Remplacer ConversationsFragment par ChatFragment
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, new ChatFragment());
            transaction.addToBackStack(null); // Permet de revenir à ConversationsFragment
            transaction.commit();
        });

        return view;
    }
}
