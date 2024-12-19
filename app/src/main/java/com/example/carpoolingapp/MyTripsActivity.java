package com.example.carpoolingapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyTripsActivity extends AppCompatActivity {
    private RecyclerView tripsRecyclerView;
    private MyTripsAdapter tripsAdapter;
    private List<Trip> tripsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_trips);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tripsRecyclerView = findViewById(R.id.tripsRecyclerView);
        tripsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        tripsList = new ArrayList<>();
        tripsAdapter = new MyTripsAdapter(tripsList, this);
        tripsRecyclerView.setAdapter(tripsAdapter);

        fetchTrips();

    }

    private void fetchTrips() {
        FirebaseUtils.getTripsByUserId(new FirebaseUtils.TripsFetchCallback() {
            @Override
            public void onSuccess(List<Trip> trips) {
                tripsList.clear();
                tripsList.addAll(trips);

                tripsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(MyTripsActivity.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }

}