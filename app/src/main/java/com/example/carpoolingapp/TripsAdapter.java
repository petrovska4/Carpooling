package com.example.carpoolingapp;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.TripViewHolder> {
    private List<Trip> tripsList;

    public TripsAdapter(List<Trip> tripsList) {
        this.tripsList = tripsList;
    }

    @NonNull
    @Override
    public TripsAdapter.TripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trip, parent, false);
        return new TripsAdapter.TripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TripViewHolder holder, int position) {
        Trip trip = tripsList.get(position);

        holder.tripName.setText("Name: " + trip.getName());
        holder.tripEmail.setText("Email: " + trip.getEmail());
        holder.tripPhone.setText("Phone: " + trip.getPhone());
        holder.tripDateFrom.setText("From: " + trip.getDateFrom());
        holder.tripDateTo.setText("To: " + trip.getDateTo());
        holder.tripVehicle.setText("Type: " + trip.getVehicle());
        holder.tripBrand.setText("Brand: " + trip.getBrand());
        holder.tripColor.setText("Color: " + trip.getColor());
        holder.tripSeats.setText("Seats: " + trip.getSeats());
        holder.tripLicence.setText("Licence: " + trip.getLicencePlate());
        holder.tripOwner.setText("Owner: " + trip.getOwner());

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(trip.getOwner())
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String ownerName = documentSnapshot.getString("name");
                        holder.tripOwner.setText("Owner: " + (ownerName != null ? ownerName : "Unknown"));
                    } else {
                        holder.tripOwner.setText("Owner: Not Found");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.tripOwner.setText("Owner: Error");
                    Log.e("Firestore", "Error fetching owner data", e);
                });

        db.collection("users").document(trip.getCreatedBy()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String createdBy = documentSnapshot.getString("name");
                        holder.tripCreatedBy.setText("Created By: " + createdBy);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
        TextView tripName, tripEmail, tripPhone, tripDateFrom, tripDateTo, tripVehicle, tripBrand, tripColor, tripSeats, tripLicence, tripOwner, tripCreatedBy;

        public TripViewHolder(View itemView) {
            super(itemView);

            tripName = itemView.findViewById(R.id.tripName);
            tripEmail = itemView.findViewById(R.id.tripEmail);
            tripPhone = itemView.findViewById(R.id.tripPhone);
            tripDateFrom = itemView.findViewById(R.id.tripDateFrom);
            tripDateTo = itemView.findViewById(R.id.tripDateTo);
            tripVehicle = itemView.findViewById(R.id.tripVehicle);
            tripBrand = itemView.findViewById(R.id.tripBrand);
            tripColor = itemView.findViewById(R.id.tripColor);
            tripSeats = itemView.findViewById(R.id.tripSeats);
            tripLicence = itemView.findViewById(R.id.tripLicence);
            tripOwner = itemView.findViewById(R.id.tripOwner);tripName = itemView.findViewById(R.id.tripName);
            tripEmail = itemView.findViewById(R.id.tripEmail);
            tripPhone = itemView.findViewById(R.id.tripPhone);
            tripDateFrom = itemView.findViewById(R.id.tripDateFrom);
            tripDateTo = itemView.findViewById(R.id.tripDateTo);
            tripCreatedBy = itemView.findViewById(R.id.tripCreatedBy);
        }
    }
}
