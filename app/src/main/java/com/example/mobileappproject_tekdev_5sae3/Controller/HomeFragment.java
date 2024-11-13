package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappproject_tekdev_5sae3.Dao.PublicationDao;
import com.example.mobileappproject_tekdev_5sae3.Entity.Publication;
import com.example.mobileappproject_tekdev_5sae3.R;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements PublicationAdapter.OnPublicationDeleteListener,
        PublicationAdapter.OnPublicationUpdateListener {

    private PublicationDao publicationDao;
    private RecyclerView recyclerView;
    private PublicationAdapter publicationAdapter;
    private List<Publication> publicationList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        Button buttonAddPost = view.findViewById(R.id.buttonAddPost);
        buttonAddPost.setOnClickListener(v -> {
            BlogFragment blogFragment = new BlogFragment();

            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, blogFragment)
                    .addToBackStack(null)
                    .commit();
        });



        recyclerView = view.findViewById(R.id.recyclerViewPublications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        AppDataBase db = AppDataBase.getInstance(getActivity());
        publicationDao = db.publicationDao();
        publicationList = publicationDao.getAllPublications();
        publicationAdapter = new PublicationAdapter(publicationList, publicationDao, this, this); // Passer le listener update
        recyclerView.setAdapter(publicationAdapter);



        EditText searchBar = view.findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Ne rien faire ici
            }
        });
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

            }
        });
        return view;
    }

    private void filter(String text) {
        List<Publication> filteredList = new ArrayList<>();
        try {
            for (Publication publication : publicationList) {
                if (publication.getDescription().toLowerCase().contains(text.toLowerCase()) ||
                        publication.getType().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(publication);
                }
            }
            publicationAdapter.filterList(filteredList);
        } catch (Exception e) {
            Log.e("FilterError", "Error while filtering publications", e);
        }
    }


    @Override
    public void onPublicationDelete(Publication publication) {
        new Thread(() -> {
            try {
                publicationDao.deletePublication(publication);
                publicationList = publicationDao.getAllPublications();
                getActivity().runOnUiThread(() -> publicationAdapter.updateList(publicationList)); // Mettre à jour l'adaptateur sur le fil principal
            } catch (Exception e) {
                Log.e("HomeFragment", "Error deleting publication", e);
            }
        }).start();
    }
    @Override
    public void onPublicationUpdate(Publication publication) {
        BlogFragment blogFragment = BlogFragment.newInstance(publication); // Passer la publication
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, blogFragment)
                .addToBackStack(null)
                .commit();
    }
}