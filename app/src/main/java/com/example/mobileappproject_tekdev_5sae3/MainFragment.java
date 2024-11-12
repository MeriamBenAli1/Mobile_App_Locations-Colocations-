package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainFragment extends Fragment {

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Retrieve the buttons from the layout
        Button btnGoToPublish = view.findViewById(R.id.btnGoToPublish);


        // Action for the "Publish" button
        btnGoToPublish.setOnClickListener(v -> {
            // Begin a fragment transaction
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            // Replace the current fragment with the PublishAdFragment
            transaction.replace(R.id.fragment_container, new PublishAdFragment());
            // Add the transaction to the back stack so that the user can navigate back
            transaction.addToBackStack(null);
            // Commit the transaction to apply the changes
            transaction.commit();
        });




        return view;
    }
}
