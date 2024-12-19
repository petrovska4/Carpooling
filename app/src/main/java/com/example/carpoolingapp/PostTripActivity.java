package com.example.carpoolingapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class PostTripActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone, etDateFrom, etDateTo, etVehicle, etBrand, etColor, etSeats, etLicencePlate, etOwner;
    private Button btnSubmitTrip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_trip);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Vehicle vehicle = getIntent().getParcelableExtra("vehicle_object");

        String ownerId = getIntent().getStringExtra("owner_id");

        if (vehicle != null) {
            EditText etVehicle = findViewById(R.id.etVehicle);
            EditText etBrand = findViewById(R.id.etBrand);
            EditText etColor = findViewById(R.id.etColor);
            EditText etSeats = findViewById(R.id.etSeats);
            EditText etLicencePlate = findViewById(R.id.etLicencePlate);
            EditText etOwner = findViewById(R.id.etOwner);

            etVehicle.setText(vehicle.getType());
            etBrand.setText(vehicle.getBrand());
            etColor.setText(vehicle.getColor());
            etSeats.setText(String.valueOf(vehicle.getSeats()));
            etLicencePlate.setText(vehicle.getLicence());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("users").document(vehicle.getOwnerId()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String ownerName = documentSnapshot.getString("name");
                            etOwner.setText(ownerName);
                        }
                    });
        }

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDateFrom = findViewById(R.id.etDateFrom);
        etDateTo = findViewById(R.id.etDateTo);
        etVehicle = findViewById(R.id.etVehicle);
        etBrand = findViewById(R.id.etBrand);
        etColor = findViewById(R.id.etColor);
        etSeats = findViewById(R.id.etSeats);
        etLicencePlate = findViewById(R.id.etLicencePlate);
        etOwner = findViewById(R.id.etOwner);
        btnSubmitTrip = findViewById(R.id.btnSubmitTrip);

        btnSubmitTrip.setOnClickListener(v -> submitTrip(ownerId));
    }

    private void submitTrip(String ownerId) {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dateFrom = etDateFrom.getText().toString().trim();
        String dateTo = etDateTo.getText().toString().trim();
        String type = etVehicle.getText().toString().trim();
        String brand = etBrand.getText().toString().trim();
        String seats = etSeats.getText().toString().trim();
        String color = etColor.getText().toString().trim();
        String licence = etLicencePlate.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(phone) ||
                TextUtils.isEmpty(dateFrom) || TextUtils.isEmpty(dateTo) || TextUtils.isEmpty(type) ||
                TextUtils.isEmpty(brand) || TextUtils.isEmpty(seats) || TextUtils.isEmpty(color) ||
                TextUtils.isEmpty(licence)) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUtils.createTrip(name, email, phone, dateFrom, dateTo, type, brand, seats, color, licence, ownerId, new FirebaseUtils.TripCreateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PostTripActivity.this, "Trip created successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PostTripActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(PostTripActivity.this, "Failed to create trip: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}

