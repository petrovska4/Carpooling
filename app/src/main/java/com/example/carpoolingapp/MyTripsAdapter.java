package com.example.carpoolingapp;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class MyTripsAdapter  extends RecyclerView.Adapter<MyTripsAdapter.MyTripViewHolder> {
    private List<Trip> tripsList;

    private Context context;

    public MyTripsAdapter(List<Trip> tripsList, Context context) {
        this.tripsList = tripsList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyTripsAdapter.MyTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_trip, parent, false);
        return new MyTripsAdapter.MyTripViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyTripsAdapter.MyTripViewHolder holder, int position) {
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

        String tripId = trip.getId();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String ownerId = trip.getOwner();


        db.collection("reviews")
                .whereEqualTo("tripId", tripId)
                .whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot reviewSnapshot = querySnapshot.getDocuments().get(0);
                        Long reviewValue = reviewSnapshot.getLong("review");

                        if (reviewValue != null) { // Check for null
                            holder.reviewInput.setText(String.valueOf(reviewValue.intValue()));
                        }
                    }
                });

        holder.submitReviewButton.setTag(tripId);

        holder.submitReviewButton.setOnClickListener(v -> {
            String clickedTripId = (String) v.getTag();
            if (clickedTripId != null) {
                String reviewInputValue = holder.reviewInput.getText().toString().trim();
                submitReview(clickedTripId, reviewInputValue, ownerId);
            }
        });
    }

    private void submitReview(String tripId, String reviewInputValue, String ownerId) {
        if (reviewInputValue.isEmpty()) {
            Toast.makeText(context, "You need to input a value for review", Toast.LENGTH_SHORT).show();
            return;
        }

        int reviewValue;
        try {
            reviewValue = Integer.parseInt(reviewInputValue);
        } catch (NumberFormatException e) {
            Toast.makeText(context, "Invalid input for review", Toast.LENGTH_SHORT).show();
            return;
        }

        if (reviewValue < 1 || reviewValue > 5) {
            Toast.makeText(context, "Review value must be between 1 and 5", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Review review = new Review(userId, tripId, reviewValue);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference reviewsCollection = db.collection("reviews");

        reviewsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("tripId", tripId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        if (task.getResult() != null && !task.getResult().isEmpty()) {
                            // Update existing review if it exists
                            DocumentSnapshot existingReview = task.getResult().getDocuments().get(0);
                            existingReview.getReference().set(review)
                                    .addOnSuccessListener(aVoid ->
                                            Toast.makeText(context, "Review updated successfully", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context, "Failed to update review", Toast.LENGTH_SHORT).show());
                        } else {
                            // Add a new review if it doesn't exist
                            reviewsCollection.add(review)
                                    .addOnSuccessListener(documentReference ->
                                            Toast.makeText(context, "Review submitted successfully", Toast.LENGTH_SHORT).show())
                                    .addOnFailureListener(e ->
                                            Toast.makeText(context, "Failed to submit review", Toast.LENGTH_SHORT).show());
                        }
                    } else {
                        Toast.makeText(context, "Error fetching review", Toast.LENGTH_SHORT).show();
                    }
                });



        addReview(ownerId, reviewValue);
    }

    private void addReview(String userId, long reviewValue) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference userRef = db.collection("users").document(userId);

        db.runTransaction(transaction -> {
                    DocumentSnapshot userSnapshot = transaction.get(userRef);

                    long currentReviewSum = userSnapshot.getLong("revSum") != null ? userSnapshot.getLong("revSum") : 0;
                    long currentReviewCnt = userSnapshot.getLong("revCnt") != null ? userSnapshot.getLong("revCnt") : 0;

                    transaction.update(userRef, "revSum", currentReviewSum + reviewValue);
                    transaction.update(userRef, "revCnt", currentReviewCnt + 1);

                    return null;
                }).addOnSuccessListener(unused -> Log.d("Firestore", "Review added successfully"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error updating review data", e));
    }


    @Override
    public int getItemCount() {
        return tripsList.size();
    }

    public static class MyTripViewHolder extends RecyclerView.ViewHolder {
        TextView tripName, tripEmail, tripPhone, tripDateFrom, tripDateTo, tripVehicle, tripBrand, tripColor, tripSeats, tripLicence, tripOwner, tripCreatedBy;
        EditText reviewInput;
        Button submitReviewButton;

        public MyTripViewHolder(View itemView) {
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
            submitReviewButton = itemView.findViewById(R.id.submitReviewButton);
            reviewInput = itemView.findViewById(R.id.reviewInput);
        }
    }
}
