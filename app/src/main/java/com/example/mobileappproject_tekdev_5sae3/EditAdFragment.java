package com.example.mobileappproject_tekdev_5sae3;

import static android.app.Activity.RESULT_OK;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditAdFragment extends Fragment {

    private EditText editTitle, editDescription, editPrice, editDate;
    private ImageView imageView;
    private Uri imageUri;
    private int adId;
    private Date selectedDate; // Store selected date as Date type

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_ad, container, false);

        // Initialize UI components
        editTitle = view.findViewById(R.id.editTitle);
        editDescription = view.findViewById(R.id.editDescription);
        editPrice = view.findViewById(R.id.editPrice);
        editDate = view.findViewById(R.id.editDate);
        imageView = view.findViewById(R.id.imageView);

        // Retrieve data passed from the parent activity (via arguments)
        if (getArguments() != null) {
            adId = getArguments().getInt("adId", -1);
            editTitle.setText(getArguments().getString("adTitle"));
            editDescription.setText(getArguments().getString("adDescription"));
            editPrice.setText(String.valueOf(getArguments().getInt("adPrice", 0)));

            String dateString = getArguments().getString("adDate");
            if (dateString != null) {
                editDate.setText(dateString);
                try {
                    selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            String imageUriString = getArguments().getString("adImageUri");
            if (imageUriString != null) {
                imageUri = Uri.parse(imageUriString);
                Glide.with(this).load(imageUri).into(imageView);
            }
        }

        // Set up the date picker for the date input field
        editDate.setOnClickListener(v -> showDatePickerDialog());

        // Initialize the image picker
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Glide.with(getContext()).load(imageUri).into(imageView);
                    }
                }
        );

        // Button to select a new image
        Button btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(pickImageIntent);
        });

        // Button to submit changes (update the ad)
        //Button btnSubmit = view.findViewById(R.id.btnSubmit);
        //btnSubmit.setOnClickListener(v -> updateAd());
        // Button to submit changes (update the ad)
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> {
            // Update the ad first
            updateAd();

            // After updating, navigate to ViewAdFragment
            // Create a new instance of ViewAdFragment
            ViewAdsFragment viewAdFragment = new ViewAdsFragment();

            // Optionally, you can pass data to ViewAdFragment using Bundle
            Bundle bundle = new Bundle();
            bundle.putInt("adId", adId);  // Pass the ad ID or any other necessary data
            viewAdFragment.setArguments(bundle);

            // Perform fragment transaction to replace EditAdFragment with ViewAdFragment
            getActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, viewAdFragment)  // Ensure to replace with the correct container ID
                    .addToBackStack(null)  // Optional: Add to back stack if you want to allow the user to navigate back
                    .commit();
        });


        return view;
    }

    // Show date picker dialog to choose a date
    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) {
            calendar.setTime(selectedDate);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = new GregorianCalendar(selectedYear, selectedMonth, selectedDay).getTime();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editDate.setText(dateFormat.format(selectedDate));
                }, year, month, day);
        datePickerDialog.show();
    }

    // Update the advertisement with the new data
    private void updateAd() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String priceString = editPrice.getText().toString().trim();

        // Validation checks
        if (title.isEmpty() || description.isEmpty() || priceString.isEmpty() || selectedDate == null || imageUri == null) {
            if (title.isEmpty()) {
                editTitle.setError("Le titre ne peut pas être vide");
            }
            if (description.isEmpty()) {
                editDescription.setError("La description ne peut pas être vide");
            }
            if (priceString.isEmpty()) {
                editPrice.setError("Le prix ne peut pas être vide");
            }
            if (selectedDate == null) {
                Toast.makeText(getContext(), "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show();
            }
            if (imageUri == null) {
                Toast.makeText(getContext(), "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Le prix doit être un nombre valide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a new Annonce object with the updated details
        Annonce updatedAnnonce = new Annonce(title, description, price, imageUri.toString(), selectedDate.getTime());
        updatedAnnonce.setId(adId);

        // Run the update in a background thread
        new Thread(() -> {
            int rowsUpdated = AppDatabase.getInstance(getContext())
                    .annonceDao()
                    .updateAnnonce(updatedAnnonce);

            // Notify the user of the update result on the main thread
            getActivity().runOnUiThread(() -> {
                if (rowsUpdated > 0) {
                    Toast.makeText(getContext(), "Annonce modifiée avec succès", Toast.LENGTH_SHORT).show();
                    // Notify the activity that the data has been updated
                    getActivity().setResult(RESULT_OK);
                } else {
                    Toast.makeText(getContext(), "Erreur lors de la modification de l'annonce", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();

}}
