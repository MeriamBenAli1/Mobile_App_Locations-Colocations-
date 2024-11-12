package com.example.mobileappproject_tekdev_5sae3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

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
    private SearchView searchView;

    public ConversationsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflater la vue
        View view = inflater.inflate(R.layout.fragment_conversations, container, false);

        // Initialiser la ListView, SearchView et les données
        conversationsListView = view.findViewById(R.id.conversations_list_view);
        searchView = view.findViewById(R.id.search_view);
        conversationsList = new ArrayList<>();

        // Exemple de données (à remplacer par des données réelles)
        conversationsList.add("Ben Ali Meriam");
        conversationsList.add("Ben Houria Eya");
        conversationsList.add("Ben Hmida Rima");
        conversationsList.add("Rhimi Dhaker");
        conversationsList.add("Akrimi Amine");

        // Créer un adaptateur pour la ListView
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, conversationsList);
        conversationsListView.setAdapter(adapter);

        // Ajouter un écouteur de texte pour SearchView
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false; // Nous n'avons pas besoin d'agir ici
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filtrer les conversations en fonction de l'entrée de recherche
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        // Définir l'écouteur de clics pour la ListView
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
