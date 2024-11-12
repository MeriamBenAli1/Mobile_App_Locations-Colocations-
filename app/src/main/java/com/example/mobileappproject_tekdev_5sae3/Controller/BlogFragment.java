package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
    private Button buttonImportImage;
    private ImageView imageViewImported;
    private Uri selectedImageUri;

    private static final int REQUEST_IMAGE_PICK = 1;

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

        // Demander des permissions si nécessaire
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
        }

        // Ouvrir le sélecteur d'images
        buttonImportImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        buttonPost.setOnClickListener(v -> {
            String description = editTextPublication.getText().toString();
            String type = spinnerPublicationType.getSelectedItem().toString();
            int userId = 1; // Utilisez l'utilisateur actuel ici

            if (description.isEmpty()) {
                editTextPublication.setError("La description ne peut pas être vide");
                return;
            }
            if (description.length() < 10) {
                editTextPublication.setError("La description doit être plus longue");
                return;
            }

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
            String currentDate = sdf.format(new Date());
            String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : "";

            Publication publication = new Publication("New Post", description, type, userId, currentDate, imageUriString);

            new Thread(() -> {
                publicationDao.insertPublication(publication);

                requireActivity().runOnUiThread(() -> {
                    Toast.makeText(getActivity(), "Publication ajoutée", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    requireActivity().finish();
                });
            }).start();
        });

        return view;
    }

    // Gestion de l'activité résultante pour récupérer l'image
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            if (selectedImageUri != null) {
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
                    imageViewImported.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Erreur lors de l'importation de l'image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
