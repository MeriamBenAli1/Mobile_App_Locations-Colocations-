package com.example.mobileappproject_tekdev_5sae3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;

import java.util.Calendar;

public class PublishAdFragment extends Fragment {

    private EditText editTitle, editDescription, editPrice, editDate;
    private ImageView imageView;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private Annonce annonceToEdit;  // Annonce à modifier, si elle existe
    private Calendar selectedDate;

    public PublishAdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_publish_ad, container, false);

        editTitle = rootView.findViewById(R.id.editTitle);
        editDescription = rootView.findViewById(R.id.editDescription);
        editPrice = rootView.findViewById(R.id.editPrice);
        editDate = rootView.findViewById(R.id.editDate);
        imageView = rootView.findViewById(R.id.imageView);

        selectedDate = Calendar.getInstance();

        editDate.setOnClickListener(v -> openDatePicker());

        Button btnSelectImage = rootView.findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(v -> selectImage());

        Button btnSubmit = rootView.findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> submitAd());

        Button btnViewAds = rootView.findViewById(R.id.btnViewAd);
        btnViewAds.setOnClickListener(v -> {
            // Create a new instance of the ViewAdsFragment
            ViewAdsFragment viewAdsFragment = new ViewAdsFragment();

            // Optionally, pass data to the fragment using a Bundle
            Bundle bundle = new Bundle();
            // You can add any data to the bundle here if needed, for example:
            // bundle.putString("someKey", "someValue");
            viewAdsFragment.setArguments(bundle);

            // Begin a fragment transaction to replace the current fragment with ViewAdsFragment
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.fragment_container, viewAdsFragment); // Use the correct container ID
            transaction.addToBackStack(null); // Add to back stack if you want to allow back navigation
            transaction.commit(); // Commit the transaction
        });


        if (getArguments() != null && getArguments().containsKey("adId")) {
            int annonceId = getArguments().getInt("adId", -1);
            loadAnnonceData(annonceId);
        }

        return rootView;
    }

    private void openDatePicker() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate.set(year1, month1, dayOfMonth);
                    editDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                },
                year, month, day);
        datePickerDialog.show();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == getActivity().RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void loadAnnonceData(int annonceId) {
        new Thread(() -> {
            annonceToEdit = AppDatabase.getInstance(getActivity()).annonceDao().getAnnonceById(annonceId);
            getActivity().runOnUiThread(() -> {
                if (annonceToEdit != null) {
                    editTitle.setText(annonceToEdit.getTitle());
                    editDescription.setText(annonceToEdit.getDescription());
                    editPrice.setText(String.valueOf(annonceToEdit.getPrice()));
                    editDate.setText(annonceToEdit.getFormattedDate());
                    imageUri = Uri.parse(annonceToEdit.getImageUri());
                    imageView.setImageURI(imageUri);
                    selectedDate.setTimeInMillis(annonceToEdit.getDate()); // Utiliser setTimeInMillis
                } else {
                    Toast.makeText(getActivity(), "Annonce introuvable", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void submitAd() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String priceString = editPrice.getText().toString().trim();
        String dateString = editDate.getText().toString().trim();

        if (title.isEmpty() || description.isEmpty() || priceString.isEmpty() || dateString.isEmpty() || imageUri == null) {
            Toast.makeText(getActivity(), "Veuillez remplir tous les champs et sélectionner une image", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(getActivity(), "Le prix doit être un nombre valide", Toast.LENGTH_SHORT).show();
            return;
        }

        if (annonceToEdit != null) {
            // Mise à jour de l'annonce existante
            annonceToEdit.setTitle(title);
            annonceToEdit.setDescription(description);
            annonceToEdit.setPrice(price);
            annonceToEdit.setDate(selectedDate.getTimeInMillis()); // Utiliser getTimeInMillis
            annonceToEdit.setImageUri(imageUri.toString());

            new Thread(() -> {
                AppDatabase.getInstance(getActivity()).annonceDao().updateAnnonce(annonceToEdit);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Annonce modifiée avec succès !", Toast.LENGTH_SHORT).show();
                    clearFields();
                });
            }).start();
        } else {
            // Nouvelle annonce
            Annonce newAd = new Annonce(title, description, price, imageUri.toString(), selectedDate.getTimeInMillis());

            new Thread(() -> {
                AppDatabase.getInstance(getActivity()).annonceDao().insertAnnonce(newAd);
                getActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Annonce publiée avec succès !", Toast.LENGTH_SHORT).show();
                    clearFields();
                });
            }).start();
        }
    }

    private void clearFields() {
        editTitle.setText("");
        editDescription.setText("");
        editPrice.setText("");
        editDate.setText("");
        imageView.setImageURI(null);
        imageUri = null;
        selectedDate = Calendar.getInstance(); // Réinitialiser la date sélectionnée
    }
}
