package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.mobileappproject_tekdev_5sae3.Dao.PublicationDao;
import com.example.mobileappproject_tekdev_5sae3.Entity.Publication;
import com.example.mobileappproject_tekdev_5sae3.MainActivity;
import com.example.mobileappproject_tekdev_5sae3.R;
import com.example.mobileappproject_tekdev_5sae3.database.AppDataBase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BlogFragment extends Fragment {
    private EditText editTextPublication;
    private Spinner spinnerPublicationType;
    private Button buttonPost;
    private PublicationDao publicationDao;

    private static final int REQUEST_IMAGE_PICK = 1;
    private Button buttonImportImage;
    private ImageView imageViewImported;
    private Uri selectedImageUri;

    // Utilisation de ActivityResultContracts pour la sélection d'image
    private ActivityResultLauncher<Intent> imagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            // Affichage de l'image dans l'ImageView
                            imageViewImported.setImageURI(selectedImageUri);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(), "Erreur lors de l'importation de l'image", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Aucune image sélectionnée", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);

        editTextPublication = view.findViewById(R.id.editTextPublication);
        spinnerPublicationType = view.findViewById(R.id.spinnerPublicationType);
        buttonPost = view.findViewById(R.id.buttonPost);

        buttonImportImage = view.findViewById(R.id.buttonImportImage);
        imageViewImported = view.findViewById(R.id.imageViewImported);
        AppDataBase db = AppDataBase.getInstance(getActivity().getApplicationContext());
        publicationDao = db.publicationDao();

        // Définir le clic du bouton pour lancer l'importation d'image
        buttonImportImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            imagePickerLauncher.launch(intent);
        });

        buttonPost.setOnClickListener(v -> {
            String description = editTextPublication.getText().toString();
            String type = spinnerPublicationType.getSelectedItem().toString();
            int userId = 1; // Changez cela en fonction de l'utilisateur actuel
            // Vérifier que la description n'est pas vide et respecte les contraintes de longueur
            if (description.isEmpty()) {
                editTextPublication.setError("La description ne peut pas être vide");
                editTextPublication.requestFocus();
                return;
            }
            if (description.length() < 10) {
                editTextPublication.setError("La description doit contenir au moins 10 caractères");
                editTextPublication.requestFocus();
                return;
            }
            if (description.length() > 255) {
                editTextPublication.setError("La description ne doit pas dépasser 255 caractères");
                editTextPublication.requestFocus();
                return;
            }
            // Vérifier les mots interdits
            if (containsBadWords(description)) {
                editTextPublication.setError("La description contient des mots inappropriés");
                editTextPublication.requestFocus();
                return;
            }

            // Vérifier que le type est valide (vous pouvez adapter la condition en fonction de votre cas)
            if (type.equals("Type de publication")) { // Assurez-vous que "Choisir un type" est le texte par défaut
                Toast.makeText(getActivity(), "Veuillez sélectionner un type de publication", Toast.LENGTH_SHORT).show();
                return;
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            String imageUriString = (selectedImageUri != null) ? selectedImageUri.toString() : "";  // Utiliser une chaîne vide si aucune image

            Publication publication = new Publication("New Post", description, type, userId, currentDate, imageUriString);

            new Thread(() -> {
                publicationDao.insertPublication(publication);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Publication ajoutée", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Optionnel : nettoie la pile d'activités
                    startActivity(intent);
                    requireActivity().finish();
                });
            }).start();
        });




        return view;
    }
    private boolean containsBadWords(String text) {
        // Liste des mots interdits (vous pouvez adapter cette liste)
        String[] badWords = {"mot1", "mot2", "mot3"}; // Remplacez par les mots indésirables réels

        // Convertir le texte en minuscule pour éviter les problèmes de casse
        String lowerText = text.toLowerCase();

        for (String badWord : badWords) {
            if (lowerText.contains(badWord)) {
                return true;
            }
        }
        return false;
    }

}
