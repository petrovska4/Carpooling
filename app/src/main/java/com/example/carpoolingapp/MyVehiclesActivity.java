package com.example.carpoolingapp;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class MyVehiclesActivity extends AppCompatActivity {
    private RecyclerView vehiclesRecyclerView;
    private MyVehiclesAdapter vehiclesAdapter;
    private List<Vehicle> vehiclesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_vehicles);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        vehiclesRecyclerView = findViewById(R.id.vehiclesRecyclerView);
        vehiclesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        vehiclesList = new ArrayList<>();
        vehiclesAdapter = new MyVehiclesAdapter(vehiclesList);
        vehiclesRecyclerView.setAdapter(vehiclesAdapter);

        fetchVehicles();
    }

    private void fetchVehicles() {
        FirebaseUtils.getVehiclesByUserId(new FirebaseUtils.VehiclesFetchCallback() {
            @Override
            public void onSuccess(List<Vehicle> vehicles) {
                vehiclesList.clear();
                vehiclesList.addAll(vehicles);

                vehiclesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MyVehiclesActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}