package com.example.mobileappproject_tekdev_5sae3;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Calendar;

public class SearchAdActivity extends AppCompatActivity {

    private EditText editLocation, editPrice, editNumRoommates, editNumBedrooms;
    private EditText editArrivalDate, editDepartureDate;

    // List for amenities
    private ArrayList<String> amenitiesList;
    private boolean[] checkedItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_ad);

        // Initialize EditText fields
        editLocation = findViewById(R.id.editLocation);
        editPrice = findViewById(R.id.editPrice);
        editNumRoommates = findViewById(R.id.editNumRoommates);
        editNumBedrooms = findViewById(R.id.editNumBedrooms);
        editArrivalDate = findViewById(R.id.editArrivalDate);
        editDepartureDate = findViewById(R.id.editDepartureDate);

        Button btnSelectArrivalDate = findViewById(R.id.btnSelectArrivalDate);
        Button btnSelectDepartureDate = findViewById(R.id.btnSelectDepartureDate);
        Button btnSearch = findViewById(R.id.btnSearch);
        Button btnSelectAmenities = findViewById(R.id.btnSelectAmenities);

        // Initialize amenities list and checked items
        amenitiesList = new ArrayList<>();
        amenitiesList.add("Climatisation");
        amenitiesList.add("Entrée privée");
        amenitiesList.add("Piscine");
        amenitiesList.add("Ascenseur");
        amenitiesList.add("Parking gratuit sur place");
        amenitiesList.add("Animaux acceptés");
        amenitiesList.add("Gardien");
        amenitiesList.add("Gym");

        checkedItems = new boolean[amenitiesList.size()];

        // Set OnClickListener for arrival date button
        btnSelectArrivalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editArrivalDate);
            }
        });

        // Set OnClickListener for departure date button
        btnSelectDepartureDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(editDepartureDate);
            }
        });

        // Set OnClickListener for amenities button
        btnSelectAmenities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAmenitiesDialog();
            }
        });

        // Set OnClickListener for the search button
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = editLocation.getText().toString();
                String price = editPrice.getText().toString();
                String numRoommates = editNumRoommates.getText().toString();
                String numBedrooms = editNumBedrooms.getText().toString();
                String arrivalDate = editArrivalDate.getText().toString();
                String departureDate = editDepartureDate.getText().toString();

                // Collect selected amenities
                StringBuilder selectedAmenities = new StringBuilder();
                for (int i = 0; i < checkedItems.length; i++) {
                    if (checkedItems[i]) {
                        selectedAmenities.append(amenitiesList.get(i)).append(", ");
                    }
                }

                // Remove last comma and space if amenities are selected
                if (selectedAmenities.length() > 0) {
                    selectedAmenities.setLength(selectedAmenities.length() - 2); // Remove the last ", "
                }

                // Show the search information
                Toast.makeText(SearchAdActivity.this,
                        "Recherche d'annonces à " + location +
                                " avec un prix maximum de " + price +
                                ", " + numRoommates +
                                " colocataires, " + numBedrooms + " chambres, " +
                                "date d'arrivée: " + arrivalDate + ", " +
                                "date de départ: " + departureDate + ".\n" +
                                "Équipements: " + selectedAmenities.toString(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void showDatePickerDialog(EditText dateEditText) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(SearchAdActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    dateEditText.setText(selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void showAmenitiesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sélectionnez les équipements")
                .setMultiChoiceItems(amenitiesList.toArray(new CharSequence[0]), checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        checkedItems[which] = isChecked;
                    }
                })
                .setPositiveButton("OK", (dialog, which) -> {
                    // You can do something with the selected items if needed
                })
                .setNegativeButton("Annuler", null)
                .show();
    }
}
