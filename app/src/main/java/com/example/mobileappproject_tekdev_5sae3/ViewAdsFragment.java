package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewAdsFragment extends Fragment implements AdsAdapter.OnAdClickListener {

    private RecyclerView recyclerView;
    private AdsAdapter adsAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        return inflater.inflate(R.layout.fragment_view_ads, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // Load ads from the database
        loadAdsFromDatabase();

        // Set up SearchView to filter results
        setupSearchView(view);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadAdsFromDatabase(); // Reload ads when returning to the fragment
    }

    private void loadAdsFromDatabase() {
        new Thread(() -> {
            List<Annonce> adsList = AppDatabase.getInstance(getActivity()).annonceDao().getAllAnnonces();
            getActivity().runOnUiThread(() -> {
                if (adsList != null && !adsList.isEmpty()) {
                    adsAdapter = new AdsAdapter(getActivity(), adsList, this);
                    recyclerView.setAdapter(adsAdapter);
                } else {
                    Toast.makeText(getActivity(), "No ads found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void setupSearchView(View view) {
        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (adsAdapter != null) {
                    adsAdapter.getFilter().filter(query);
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (adsAdapter != null) {
                    adsAdapter.getFilter().filter(newText);
                }
                return true;
            }
        });
    }

    @Override
    public void onAdClick(@NonNull Annonce annonce) {
        Intent intent = new Intent(getActivity(), EditAdFragment.class);
        intent.putExtra("adId", annonce.getId());
        intent.putExtra("adTitle", annonce.getTitle());
        intent.putExtra("adDescription", annonce.getDescription());
        intent.putExtra("adPrice", annonce.getPrice());
        intent.putExtra("adDate", new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(new Date(annonce.getDate()))); // Convert date to string
        intent.putExtra("adImageUri", annonce.getImageUri());
        startActivity(intent);
    }

    @Override
    public void onAdDelete(@NonNull Annonce ad) {
        deleteAnnonce(ad);
    }

    public void deleteAnnonce(@NonNull Annonce ad) {
        new Thread(() -> {
            AppDatabase.getInstance(getActivity()).annonceDao().deleteAnnonce(ad);
            getActivity().runOnUiThread(() -> {
                Toast.makeText(getActivity(), "Annonce deleted", Toast.LENGTH_SHORT).show();
                loadAdsFromDatabase(); // Refresh the ads list after deletion
            });
        }).start();
    }
}
