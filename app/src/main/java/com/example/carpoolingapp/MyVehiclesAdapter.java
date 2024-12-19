package com.example.carpoolingapp;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyVehiclesAdapter extends RecyclerView.Adapter<MyVehiclesAdapter.MyVehicleViewHolder> {
    private List<Vehicle> vehiclesList;

    public MyVehiclesAdapter(List<Vehicle> vehiclesList) {
        this.vehiclesList = vehiclesList;
    }

    @NonNull
    @Override
    public MyVehiclesAdapter.MyVehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_vehicle, parent, false);
        return new MyVehiclesAdapter.MyVehicleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVehiclesAdapter.MyVehicleViewHolder holder, int position) {
        Vehicle vehicle = vehiclesList.get(position);

        holder.vehicleType.setText("Type: " + vehicle.getType());
        holder.vehicleBrand.setText("Brand: " + vehicle.getBrand());
        holder.vehicleColor.setText("Color: " + vehicle.getColor());
        holder.vehicleSeats.setText("Seats: " + vehicle.getSeats());
        holder.vehicleLicence.setText("Licence: " + vehicle.getLicence());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(vehicle.getOwnerId()).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String ownerName = documentSnapshot.getString("name");
                        holder.vehicleOwner.setText("Owner: " + ownerName);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return vehiclesList.size();
    }

    public static class MyVehicleViewHolder extends RecyclerView.ViewHolder {
        TextView vehicleType, vehicleBrand, vehicleColor, vehicleSeats, vehicleLicence, vehicleOwner;

        public MyVehicleViewHolder(View itemView) {
            super(itemView);

            vehicleType = itemView.findViewById(R.id.vehicleType);
            vehicleBrand = itemView.findViewById(R.id.vehicleBrand);
            vehicleColor = itemView.findViewById(R.id.vehicleColor);
            vehicleSeats = itemView.findViewById(R.id.vehicleSeats);
            vehicleLicence = itemView.findViewById(R.id.vehicleLicence);
            vehicleOwner = itemView.findViewById(R.id.vehicleOwner);
        }
    }
}
