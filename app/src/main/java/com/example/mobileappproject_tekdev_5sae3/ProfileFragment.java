package com.example.mobileappproject_tekdev_5sae3;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.mobileappproject_tekdev_5sae3.Controller.LoginActivity;

public class ProfileFragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int READ_STORAGE_PERMISSION_REQUEST_CODE = 2;

    private ImageView profileImageView;
    private Button editProfileButton, logoutButton;
    private TextView profileName, profileEmail;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize views
        profileImageView = view.findViewById(R.id.profile_image);
        profileName = view.findViewById(R.id.profile_name);
        profileEmail = view.findViewById(R.id.profile_email);
        editProfileButton = view.findViewById(R.id.edit_profile_button);
        logoutButton = view.findViewById(R.id.Logout);

        // Load user data from SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String name = sharedPreferences.getString("name", "John Doe");
        String email = sharedPreferences.getString("email", "johndoe@example.com");

        // Set user information
        profileName.setText(name);
        profileEmail.setText(email);

        // Load the profile image from SharedPreferences if available
        String imageUri = sharedPreferences.getString("profileImageUri", null);
        if (imageUri != null) {
            profileImageView.setImageURI(Uri.parse(imageUri));
        }

        // Set up profile image selection
        profileImageView.setOnClickListener(v -> openImageChooser());
        editProfileButton.setOnClickListener(v -> openImageChooser());

        // Set logout button functionality
        logoutButton.setOnClickListener(v -> logoutUser(sharedPreferences));

        return view;
    }

    // Open the image chooser
    private void openImageChooser() {
        if (checkPermissionForReadExternalStorage()) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            requestPermissionForReadExternalStorage();
        }
    }

    // Check if read external storage permission is granted
    private boolean checkPermissionForReadExternalStorage() {
        int result = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    // Request read external storage permission
    private void requestPermissionForReadExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(requireActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            Toast.makeText(getContext(), "Storage permission is required to select a profile image.", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(requireActivity(),
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                READ_STORAGE_PERMISSION_REQUEST_CODE);
    }

    // Handle the result of permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImageChooser();
            } else {
                Toast.makeText(getContext(), "Permission denied to access storage", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Handle the result of image selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            profileImageView.setImageURI(imageUri);

            // Save the image URI to SharedPreferences
            saveImageUriToPreferences(imageUri.toString());
        }
    }

    // Save the selected image URI in SharedPreferences
    private void saveImageUriToPreferences(String uri) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImageUri", uri);
        editor.apply();
    }

    // Logout the user by clearing SharedPreferences and redirecting to LoginActivity
    private void logoutUser(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Close the current activity to prevent going back
    }
}
