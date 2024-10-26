package com.example.mobileappproject_tekdev_5sae3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;

import java.util.Calendar;

public class PublishAdActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editPrice, editDate;
    private ImageView imageView;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private Annonce annonceToEdit;  // Annonce à modifier, si elle existe
    private Calendar selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_ad);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        editDate = findViewById(R.id.editDate);
        imageView = findViewById(R.id.imageView);

        selectedDate = Calendar.getInstance();

        editDate.setOnClickListener(v -> openDatePicker());

        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(v -> selectImage());

        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> submitAd());

        Button btnViewAds = findViewById(R.id.btnViewAds);
        btnViewAds.setOnClickListener(v -> {
            Intent intent = new Intent(PublishAdActivity.this, ViewAdsActivity.class);
            startActivity(intent);
        });

        Intent intent = getIntent();
        if (intent.hasExtra("adId")) {
            int annonceId = intent.getIntExtra("adId", -1);
            loadAnnonceData(annonceId);
        }
    }

    private void openDatePicker() {
        int year = selectedDate.get(Calendar.YEAR);
        int month = selectedDate.get(Calendar.MONTH);
        int day = selectedDate.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate.set(year1, month1, dayOfMonth);
                    editDate.setText(dayOfMonth + "/" + (month1 + 1) + "/" + year1);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    private void loadAnnonceData(int annonceId) {
        new Thread(() -> {
            annonceToEdit = AppDatabase.getInstance(PublishAdActivity.this).annonceDao().getAnnonceById(annonceId);
            runOnUiThread(() -> {
                if (annonceToEdit != null) {
                    editTitle.setText(annonceToEdit.getTitle());
                    editDescription.setText(annonceToEdit.getDescription());
                    editPrice.setText(String.valueOf(annonceToEdit.getPrice()));
                    editDate.setText(annonceToEdit.getFormattedDate());
                    imageUri = Uri.parse(annonceToEdit.getImageUri());
                    imageView.setImageURI(imageUri);
                    selectedDate.setTimeInMillis(annonceToEdit.getDate()); // Utiliser setTimeInMillis
                } else {
                    Toast.makeText(PublishAdActivity.this, "Annonce introuvable", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Veuillez remplir tous les champs et sélectionner une image", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Le prix doit être un nombre valide", Toast.LENGTH_SHORT).show();
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
                AppDatabase.getInstance(PublishAdActivity.this).annonceDao().updateAnnonce(annonceToEdit);
                runOnUiThread(() -> {
                    Toast.makeText(PublishAdActivity.this, "Annonce modifiée avec succès !", Toast.LENGTH_SHORT).show();
                    clearFields();
                    finish();
                });
            }).start();
        } else {
            // Nouvelle annonce
            Annonce newAd = new Annonce(title, description, price, imageUri.toString(), selectedDate.getTimeInMillis());

            new Thread(() -> {
                AppDatabase.getInstance(PublishAdActivity.this).annonceDao().insertAnnonce(newAd);
                runOnUiThread(() -> {
                    Toast.makeText(PublishAdActivity.this, "Annonce publiée avec succès !", Toast.LENGTH_SHORT).show();
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
