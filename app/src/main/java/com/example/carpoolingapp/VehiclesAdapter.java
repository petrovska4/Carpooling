package com.example.carpoolingapp;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class VehiclesAdapter extends RecyclerView.Adapter<VehiclesAdapter.VehicleViewHolder> {

    private List<Vehicle> vehiclesList;
    private double userLatitude;
    private double userLongitude;

    public VehiclesAdapter(List<Vehicle> vehiclesList) {
        this.vehiclesList = vehiclesList;
    }

    public void setUserLocation(double latitude, double longitude) {
        this.userLatitude = latitude;
        this.userLongitude = longitude;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vehicle, parent, false);
        return new VehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehicleViewHolder holder, int position) {
        Vehicle vehicle = vehiclesList.get(position);

        holder.vehicleType.setText("Type: " + vehicle.getType());
        holder.vehicleBrand.setText("Brand: " + vehicle.getBrand());
        holder.vehicleColor.setText("Color: " + vehicle.getColor());
        holder.vehicleSeats.setText("Seats: " + vehicle.getSeats());
        holder.vehicleLicence.setText("Licence: " + vehicle.getLicence());
        holder.vehiclePrice.setText("Price: " + vehicle.getPrice() + "$");
        holder.vehicleLocation.setText("Location: " + vehicle.getLocation());

        String locationStr = vehicle.getLocation();
        if (locationStr != null) {
            String[] locationParts = locationStr.split(",");

            if (locationParts.length == 2) {
                try {
                    double vehicleLatitude = Double.parseDouble(locationParts[0]);
                    double vehicleLongitude = Double.parseDouble(locationParts[1]);

                    float[] results = new float[1];
                    Location.distanceBetween(userLatitude, userLongitude, vehicleLatitude, vehicleLongitude, results);
                    float distanceInMeters = results[0];
                    String distanceText = "Distance: " + (distanceInMeters / 1000) + " km";
                    holder.vehicleLocation.setText(distanceText);

                } catch (NumberFormatException e) {
                    holder.vehicleLocation.setText("Invalid location format");
                }
            } else {
                holder.vehicleLocation.setText("Invalid location");
            }

        } else {
            holder.vehicleLocation.setText("Distance: No data available");
        }
        String ownerId = vehicle.getOwnerId();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(ownerId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String ownerName = documentSnapshot.getString("name");
                        holder.vehicleOwner.setText("Owner: " + ownerName);

                        Long revSum = documentSnapshot.getLong("revSum");
                        Long revCnt = documentSnapshot.getLong("revCnt");

                        if (revSum != null && revCnt != null && revCnt > 0) {
                            double review = (double) revSum / revCnt; // Calculate review
                            holder.vehicleReview.setText("Owner review: " + String.format("%.2f", review));
                        } else {
                            holder.vehicleReview.setText("Owner review: No reviews yet");
                        }
                    } else {
                        holder.vehicleOwner.setText("Owner not found");
                        holder.vehicleReview.setText("Owner review: No reviews available");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.vehicleOwner.setText("Failed to load owner");
                    holder.vehicleReview.setText("Owner review: Failed to load");
                });


        db.collection("users").document(ownerId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String ownerName = documentSnapshot.getString("name");
                        holder.vehicleOwner.setText("Owner: " + ownerName);
                    } else {
                        holder.vehicleOwner.setText("Owner not found");
                    }
                })
                .addOnFailureListener(e -> {
                    holder.vehicleOwner.setText("Failed to load owner");
                });

        holder.btnScheduleTrip.setOnClickListener(v -> {
            Intent intent = new Intent(holder.itemView.getContext(), PostTripActivity.class);
            intent.putExtra("vehicle_object", vehicle);
            intent.putExtra("owner_id", ownerId);
            holder.itemView.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return vehiclesList.size();
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleType, vehicleBrand, vehicleColor, vehicleSeats, vehicleLicence, vehicleOwner, vehicleReview, vehiclePrice, vehicleLocation;
        Button btnScheduleTrip;

        public VehicleViewHolder(View itemView) {
            super(itemView);

            vehicleType = itemView.findViewById(R.id.vehicleType);
            vehicleBrand = itemView.findViewById(R.id.vehicleBrand);
            vehicleColor = itemView.findViewById(R.id.vehicleColor);
            vehicleSeats = itemView.findViewById(R.id.vehicleSeats);
            vehicleLicence = itemView.findViewById(R.id.vehicleLicence);
            vehicleOwner = itemView.findViewById(R.id.vehicleOwner);
            vehicleReview = itemView.findViewById(R.id.vehicleReview);
            btnScheduleTrip = itemView.findViewById(R.id.btnScheduleTrip);
            vehiclePrice = itemView.findViewById(R.id.vehiclePrice);
            vehicleLocation = itemView.findViewById(R.id.vehicleLocation);
        }
    }
}
