package com.example.carpoolingapp;

import android.Manifest;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class PostVehicleActivity extends AppCompatActivity  implements OnMapReadyCallback {
    private EditText typeInput, brandInput, seatsInput, colorInput, licenceInput, priceInput;
    private Button saveButton;
    private GoogleMap mMap;
    private Marker marker;
    private double latitude, longitude;
    private TextView locationValueTextView;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_post_vehical);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "Location permission is required to show your location", Toast.LENGTH_SHORT).show();
            }

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            initializeMap();
        }

        typeInput = findViewById(R.id.typeInput);
        brandInput = findViewById(R.id.brandInput);
        seatsInput = findViewById(R.id.seatsInput);
        colorInput = findViewById(R.id.colorInput);
        licenceInput = findViewById(R.id.licenceInput);
        priceInput = findViewById(R.id.priceInput);
        saveButton = findViewById(R.id.saveButton);
        locationValueTextView = findViewById(R.id.location_value);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        saveButton.setOnClickListener(v -> saveVehicle());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

//        mMap.setMyLocationEnabled(true);

        // Set a  for when the user taps on the map
        mMap.setOnMapClickListener(latLng -> {
            if (marker != null) {
                marker.remove();
            }

            marker = mMap.addMarker(new MarkerOptions().position(latLng).title("Selected Location"));

            latitude = latLng.latitude;
            longitude = latLng.longitude;

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

            String locationText = "Latitude: " + latitude + ", Longitude: " + longitude;
            locationValueTextView.setText(locationText);
        });

        LatLng defaultLocation = new LatLng(37.7749, -122.4194); // San Francisco, for example
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10));
    }

    private void saveVehicle() {
        String type = typeInput.getText().toString().trim();
        String brand = brandInput.getText().toString().trim();
        String seats = seatsInput.getText().toString().trim();
        String color = colorInput.getText().toString().trim();
        String licence = licenceInput.getText().toString().trim();
        String price = priceInput.getText().toString().trim();


        if (TextUtils.isEmpty(type)) {
            typeInput.setError("Vehicle Type is required");
            return;
        }

        if (TextUtils.isEmpty(brand)) {
            brandInput.setError("Brand is required");
            return;
        }

        if (TextUtils.isEmpty(seats)) {
            seatsInput.setError("Seats are required");
            return;
        }

        if (TextUtils.isEmpty(color)) {
            colorInput.setError("Color is required");
            return;
        }

        if (TextUtils.isEmpty(licence)) {
            licenceInput.setError("License Plate is required");
            return;
        }

        if (TextUtils.isEmpty(licence)) {
            priceInput.setError("License Plate is required");
            return;
        }

        String location = latitude + "," + longitude;

        FirebaseUtils.createVehicle(type, brand, seats, color, licence, location, price, new FirebaseUtils.VehicleCreateCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(PostVehicleActivity.this, "Vehicle saved successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(PostVehicleActivity.this, "Failed to save vehicle: " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Check if the permission was granted
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initialize map
                initializeMap();
            } else {
                // Permission denied, inform the user
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }
}