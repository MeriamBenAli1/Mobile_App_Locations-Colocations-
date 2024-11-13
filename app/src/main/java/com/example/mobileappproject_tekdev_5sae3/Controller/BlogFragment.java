package com.example.mobileappproject_tekdev_5sae3.Controller;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class BlogFragment extends Fragment {
    private EditText editTextPublication;
    private Spinner spinnerPublicationType;
    private Button buttonPost;
    private PublicationDao publicationDao;
    private Button buttonImportImage;
    private ImageView imageViewImported;
    private Uri selectedImageUri;
    private Publication publicationToUpdate = null;
    private static final int REQUEST_IMAGE_PICK = 1;
    public static BlogFragment newInstance(Publication publication) {
        BlogFragment fragment = new BlogFragment();
        Bundle args = new Bundle();
        args.putSerializable("publication", publication);
        fragment.setArguments(args);
        return fragment;
    }
    private final List<String> badWordsList = Arrays.asList("badword1", "badword2", "badword3");

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blog, container, false);

        editTextPublication = view.findViewById(R.id.editTextPublication);
        spinnerPublicationType = view.findViewById(R.id.spinnerPublicationType);
        buttonPost = view.findViewById(R.id.buttonPost);
        buttonImportImage = view.findViewById(R.id.buttonImportImage);
        imageViewImported = view.findViewById(R.id.imageViewImported);

        // Récupère la base de données et le DAO
        AppDataBase db = AppDataBase.getInstance(getActivity().getApplicationContext());
        publicationDao = db.publicationDao();

        // Initialisation du Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.publication_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPublicationType.setAdapter(adapter);

        // Vérifie si une publication est passée en argument pour l'édition
        if (getArguments() != null) {
            publicationToUpdate = (Publication) getArguments().getSerializable("publication");
            if (publicationToUpdate != null) {
                // Charger la description
                editTextPublication.setText(publicationToUpdate.getDescription());

                // Charger le type de publication dans le Spinner
                int spinnerPosition = adapter.getPosition(publicationToUpdate.getType());
                if (spinnerPosition >= 0) {
                    spinnerPublicationType.setSelection(spinnerPosition);
                }

                // Charger l'image, si disponible
                String imageUriString = publicationToUpdate.getImageUri();
                if (imageUriString != null && !imageUriString.isEmpty()) {
                    selectedImageUri = Uri.parse(imageUriString);
                    imageViewImported.setImageURI(selectedImageUri);
                }
            }
        }

        // Limiter la description à 250 caractères
        editTextPublication.setFilters(new InputFilter[]{ new InputFilter.LengthFilter(250) });
// Demander des permissions si nécessaire
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_IMAGE_PICK);
        }
        // Bouton pour importer une image
        buttonImportImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        buttonPost.setOnClickListener(v -> {
            String description = editTextPublication.getText().toString();
            String type = spinnerPublicationType.getSelectedItem().toString();

            // Validation de la description (min 10, max 250 caractères)
            if (description.isEmpty()) {
                editTextPublication.setError("La description ne peut pas être vide");
                return;
            }
            if (description.length() < 10) {
                editTextPublication.setError("La description doit contenir au moins 10 caractères");
                return;
            }
            if (description.length() > 250) {
                editTextPublication.setError("La description ne peut pas dépasser 250 caractères");
                return;
            }
            if (containsBadWords(description)) {
                editTextPublication.setError("La description contient des mots inappropriés");
                return;
            }
            // Validation du Spinner (doit être différent de "Type de publication")
            if (type.equals("Type de publication")) {
                Toast.makeText(getActivity(), "Veuillez sélectionner un type de publication valide", Toast.LENGTH_SHORT).show();
                return;
            }

            String imageUriString = selectedImageUri != null ? selectedImageUri.toString() : "";
            String currentDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(new Date());

            if (publicationToUpdate != null) {
                publicationToUpdate.setDescription(description);
                publicationToUpdate.setType(type);
                publicationToUpdate.setImageUri(imageUriString);

                new Thread(() -> {
                    publicationDao.updatePublication(publicationToUpdate);
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Publication mise à jour", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    });
                }).start();
            } else {
                Publication newPublication = new Publication(description, type, 1, currentDate, imageUriString);
                new Thread(() -> {
                    publicationDao.insertPublication(newPublication);
                    requireActivity().runOnUiThread(() -> {
                        Toast.makeText(getActivity(), "Publication ajoutée", Toast.LENGTH_SHORT).show();
                        getActivity().onBackPressed();
                    });
                }).start();
            }
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
    // Méthode pour vérifier si la description contient des mots interdits
    private boolean containsBadWords(String text) {
        for (String badWord : badWordsList) {
            if (text.toLowerCase().contains(badWord.toLowerCase())) {
                return true;
            }
        }
        return false;
    }
}