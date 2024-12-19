package com.example.carpoolingapp;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.List;

public class ViewAllVehiclesActivity extends AppCompatActivity {
    private RecyclerView vehiclesRecyclerView;
    private VehiclesAdapter vehiclesAdapter;
    private List<Vehicle> vehiclesList;

    private double userLatitude;
    private double userLongitude;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_vehicles);

        EdgeToEdge.enable(this);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vehiclesRecyclerView = findViewById(R.id.vehiclesRecyclerView);
        vehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        vehiclesList = new ArrayList<>();
        vehiclesAdapter = new VehiclesAdapter(vehiclesList);
        vehiclesRecyclerView.setAdapter(vehiclesAdapter);

        getUserLocation();

    }

    private void getUserLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Check if location permissions are granted
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, 100); // Request permissions
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            userLatitude = location.getLatitude();
                            userLongitude = location.getLongitude();

                            vehiclesAdapter.setUserLocation(userLatitude, userLongitude);
                            fetchVehicles();
                        } else {
                            Toast.makeText(ViewAllVehiclesActivity.this, "Location not found", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void fetchVehicles() {
        FirebaseUtils.getAllVehicles(new FirebaseUtils.VehiclesFetchCallback() {
            @Override
            public void onSuccess(List<Vehicle> vehicles) {
                vehiclesList.clear();
                vehiclesList.addAll(vehicles);

                vehiclesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ViewAllVehiclesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            // Check if permission was granted
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, now get location
                getUserLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
