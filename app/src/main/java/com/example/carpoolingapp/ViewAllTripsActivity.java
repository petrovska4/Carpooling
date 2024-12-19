package com.example.carpoolingapp;

import static com.example.carpoolingapp.FirebaseUtils.getAllTrips;

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

public class ViewAllTripsActivity extends AppCompatActivity {
    private RecyclerView tripsRecyclerView;
    private TripsAdapter tripsAdapter;
    private List<Trip> tripsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_view_all_trips);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tripsRecyclerView = findViewById(R.id.tripsRecyclerView);
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripsList = new ArrayList<>();
        tripsAdapter = new TripsAdapter(tripsList);
        tripsRecyclerView.setAdapter(tripsAdapter);

        fetchTrips();
    }

    private void fetchTrips() {
        FirebaseUtils.getAllTrips(new FirebaseUtils.TripsFetchCallback() {
            @Override
            public void onSuccess(List<Trip> trips) {
                tripsList.clear();
                tripsList.addAll(trips);

                tripsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(ViewAllTripsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
}