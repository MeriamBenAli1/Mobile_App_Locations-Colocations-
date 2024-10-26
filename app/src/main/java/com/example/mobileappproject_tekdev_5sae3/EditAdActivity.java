package com.example.mobileappproject_tekdev_5sae3;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.mobileappproject_tekdev_5sae3.database.AppDatabase;
import com.example.mobileappproject_tekdev_5sae3.entities.Annonce;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class EditAdActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editPrice, editDate;
    private ImageView imageView;
    private Uri imageUri;
    private int adId;
    private Date selectedDate; // Keep selectedDate as Date type

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_ad);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        editDate = findViewById(R.id.editDate);
        imageView = findViewById(R.id.imageView);

        // Retrieve data from the intent
        Intent intent = getIntent();
        adId = intent.getIntExtra("adId", -1);
        editTitle.setText(intent.getStringExtra("adTitle"));
        editDescription.setText(intent.getStringExtra("adDescription"));
        editPrice.setText(String.valueOf(intent.getIntExtra("adPrice", 0)));

        // Load existing date
        String dateString = intent.getStringExtra("adDate");
        if (dateString != null) {
            editDate.setText(dateString);
            // Convert string date to Date object
            try {
                selectedDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // Load existing image
        String imageUriString = intent.getStringExtra("adImageUri");
        if (imageUriString != null) {
            imageUri = Uri.parse(imageUriString);
            Glide.with(this).load(imageUri).into(imageView);
        }

        // Set up the date picker
        editDate.setOnClickListener(v -> showDatePickerDialog());

        // Initialize the image picker
        ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        imageUri = result.getData().getData();
                        Glide.with(this).load(imageUri).into(imageView);
                    }
                }
        );

        // Button to select a new image
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        btnSelectImage.setOnClickListener(v -> {
            Intent pickImageIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            imagePickerLauncher.launch(pickImageIntent);
        });

        // Button to submit changes
        Button btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(v -> updateAd());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        if (selectedDate != null) {
            calendar.setTime(selectedDate);
        }
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    selectedDate = new GregorianCalendar(selectedYear, selectedMonth, selectedDay).getTime(); // Create Date object
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    editDate.setText(dateFormat.format(selectedDate)); // Set formatted date string
                }, year, month, day);
        datePickerDialog.show();
    }

    private void updateAd() {
        String title = editTitle.getText().toString().trim();
        String description = editDescription.getText().toString().trim();
        String priceString = editPrice.getText().toString().trim();

        // Validate fields
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
                Toast.makeText(this, "Veuillez sélectionner une date", Toast.LENGTH_SHORT).show();
            }
            if (imageUri == null) {
                Toast.makeText(this, "Veuillez sélectionner une image", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceString);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Le prix doit être un nombre valide", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create Annonce object with new values
        Annonce updatedAnnonce = new Annonce(title, description, price, imageUri.toString(), selectedDate.getTime()); // Use long value for date
        updatedAnnonce.setId(adId);

        // Update the ad in the database
        new Thread(() -> {
            int rowsUpdated = AppDatabase.getInstance(EditAdActivity.this)
                    .annonceDao()
                    .updateAnnonce(updatedAnnonce);

            runOnUiThread(() -> {
                if (rowsUpdated > 0) {
                    Toast.makeText(EditAdActivity.this, "Annonce modifiée avec succès", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK); // Indicate the modification
                    finish();
                } else {
                    Toast.makeText(EditAdActivity.this, "Erreur lors de la modification de l'annonce", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }
}
