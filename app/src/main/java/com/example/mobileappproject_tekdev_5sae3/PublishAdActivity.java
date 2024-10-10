package com.example.mobileappproject_tekdev_5sae3;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class PublishAdActivity extends AppCompatActivity {

    private EditText editTitle, editDescription, editPrice;
    private ImageView imageView;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri; // Pour stocker l'URI de l'image sélectionnée

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_ad);

        editTitle = findViewById(R.id.editTitle);
        editDescription = findViewById(R.id.editDescription);
        editPrice = findViewById(R.id.editPrice);
        imageView = findViewById(R.id.imageView);
        Button btnSelectImage = findViewById(R.id.btnSelectImage);
        Button btnSubmit = findViewById(R.id.btnSubmit);

        // Gestion du clic sur le bouton pour sélectionner une image
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, PICK_IMAGE);
            }
        });

        // Gestion du clic sur le bouton pour soumettre l'annonce
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTitle.getText().toString();
                String description = editDescription.getText().toString();
                String price = editPrice.getText().toString();

                // Ajouter la logique pour enregistrer l'annonce, y compris l'image
                if (imageUri != null) {
                    // Logic to save the ad, including the image
                    Toast.makeText(PublishAdActivity.this, "Annonce publiée!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PublishAdActivity.this, "Veuillez sélectionner une image.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Gérer le résultat de l'activité de sélection d'image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData();
            imageView.setImageURI(imageUri); // Afficher l'image sélectionnée dans l'ImageView
        }
    }
}
