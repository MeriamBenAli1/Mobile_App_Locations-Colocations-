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
import android.util.Log;
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
    private static final String TAG = "ProfileFragment";

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
            Log.d(TAG, "Profile image loaded from SharedPreferences.");
        } else {
            Log.d(TAG, "No profile image found in SharedPreferences.");
        }

        // Set up profile image selection
        profileImageView.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Profile image clicked", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Profile image clicked.");
            openImageChooser();
        });
        editProfileButton.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Edit profile button clicked", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Edit profile button clicked.");
            openImageChooser();
        });

        // Set logout button functionality
        logoutButton.setOnClickListener(v -> logoutUser(sharedPreferences));

        return view;
    }

    // Open the image chooser
    private void openImageChooser() {
        Log.d("ProfileFragment", "openImageChooser called");  // Add this line to confirm function entry
        if (checkPermissionForReadExternalStorage()) {
            Log.d("ProfileFragment", "Permission already granted, opening image chooser");  // Add this line to confirm permission status
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        } else {
            Log.d("ProfileFragment", "Requesting READ_EXTERNAL_STORAGE permission");  // Add this line before requesting permission
            requestPermissionForReadExternalStorage();
        }
    }
    // Check if read external storage permission is granted
    private boolean checkPermissionForReadExternalStorage() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, check individual media permissions
            boolean hasImagesPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED;
            boolean hasVideoPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) == PackageManager.PERMISSION_GRANTED;
            boolean hasAudioPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) == PackageManager.PERMISSION_GRANTED;

            return hasImagesPermission && hasVideoPermission && hasAudioPermission; // Adjust based on your needs
        } else {
            // For older Android versions, check READ_EXTERNAL_STORAGE
            return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        }
    }

    // Request read external storage permission
    private void requestPermissionForReadExternalStorage() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            // For Android 13 and above, request specific media permissions
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{
                            Manifest.permission.READ_MEDIA_IMAGES,
                            Manifest.permission.READ_MEDIA_VIDEO,
                            Manifest.permission.READ_MEDIA_AUDIO
                    },
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        } else {
            // For older Android versions, request READ_EXTERNAL_STORAGE
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_REQUEST_CODE);
        }
    }

    // Handle the result of permission requests
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_STORAGE_PERMISSION_REQUEST_CODE) {
            boolean allPermissionsGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }

            if (allPermissionsGranted) {
                Log.d("ProfileFragment", "All media permissions granted.");
                openImageChooser();
            } else {
                Log.d("ProfileFragment", "Media permissions denied.");
                Toast.makeText(getContext(), "Please enable media access permissions in settings.", Toast.LENGTH_LONG).show();
                // Optionally, direct the user to settings if permissions are denied permanently
            }
        }
    }

    // Handle the result of image selection
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST) {
            Log.d(TAG, "Received result from image picker.");
            if (resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                profileImageView.setImageURI(imageUri);
                Log.d(TAG, "Image selected and displayed.");

                // Save the image URI to SharedPreferences
                saveImageUriToPreferences(imageUri.toString());
            } else {
                Log.d(TAG, "Image selection failed or was canceled.");
                Toast.makeText(getContext(), "Image selection failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Save the selected image URI in SharedPreferences
    private void saveImageUriToPreferences(String uri) {
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profileImageUri", uri);
        editor.apply();
        Log.d(TAG, "Profile image URI saved to SharedPreferences: " + uri);
    }

    // Logout the user by clearing SharedPreferences and redirecting to LoginActivity
    private void logoutUser(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        Log.d(TAG, "User data cleared from SharedPreferences.");

        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);
        requireActivity().finish(); // Close the current activity to prevent going back
    }
}
