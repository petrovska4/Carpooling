package com.example.carpoolingapp;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FirebaseUtils {
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static void createUser(String userId, String name, String email, String phone, boolean driver, Integer revSum, Integer revCnt,final UserCreationCallback callback) {
        User user = new User(name, email, phone, driver, revSum, revCnt);

        DocumentReference userRef = db.collection("users").document(userId);
        userRef.set(user)
                .addOnSuccessListener(aVoid -> {
                    callback.onSuccess(true);
                })
                .addOnFailureListener(e -> {
                    callback.onSuccess(false);
                });
    }

    public static void fetchUserDriverStatus(final FirebaseUserCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            String userId = user.getUid();
            DocumentReference userRef = db.collection("users").document(userId);

            userRef.get().addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    Boolean driver = documentSnapshot.getBoolean("driver");
                    if (driver != null) {
                        callback.onSuccess(driver);  // Pass the 'driver' status to the callback
                    } else {
                        callback.onFailure("driver field missing");
                    }
                } else {
                    callback.onFailure("User data not found");
                }
            }).addOnFailureListener(e -> {
                callback.onFailure("Error fetching user data: " + e.getMessage());
            });
        } else {
            callback.onFailure("User not logged in");
        }
    }

    public static void createVehicle(String type, String brand, String seats, String color, String licence, String location, String price, VehicleCreateCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Vehicle vehicle = new Vehicle(type, brand, seats, color, licence, userId, location, price);

            db.collection("vehicles")
                    .add(vehicle)
                    .addOnSuccessListener(documentReference -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
        } else {
            callback.onFailure("User not logged in");
        }
    }

    public static void getAllVehicles(final VehiclesFetchCallback callback) {
        db.collection("vehicles")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Vehicle> vehicleList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Vehicle vehicle = document.toObject(Vehicle.class);
                            vehicleList.add(vehicle);
                        }
                        callback.onSuccess(vehicleList);
                    } else {
                        callback.onFailure("No vehicles found");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Error fetching vehicles: " + e.getMessage());
                });
    }

    public static void createTrip(String name, String email, String phone, String dateFrom, String dateTo, String type, String brand, String seats, String color, String licence, String owner, TripCreateCallback callback) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            Trip trip = new Trip(name, email, phone, dateFrom, dateTo, type, brand, seats, color, licence, owner, userId);

            db.collection("trips")
                    .add(trip)
                    .addOnSuccessListener(documentReference -> callback.onSuccess())
                    .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
        } else {
            callback.onFailure("User not logged in");
        }
    }

    public static void getAllTrips(final TripsFetchCallback callback) {
        db.collection("trips")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Trip> tripList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Trip trip = document.toObject(Trip.class);
                            tripList.add(trip);
                        }
                        callback.onSuccess(tripList);
                    } else {
                        callback.onFailure("No trips found");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Error fetching trips: " + e.getMessage());
                });
    }

    public static void getTripsByUserId(final TripsFetchCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("trips")
                .whereEqualTo("createdBy", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Trip> tripList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Trip trip = document.toObject(Trip.class);
                            trip.setId(document.getId());
                            tripList.add(trip);
                        }
                        callback.onSuccess(tripList);
                    } else {
                        callback.onFailure("No trips found for this user.");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Error fetching trips: " + e.getMessage());
                });
    }

    public static void getVehiclesByUserId(final VehiclesFetchCallback callback) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db.collection("vehicles")
                .whereEqualTo("ownerId", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        List<Vehicle> vehicleList = new ArrayList<>();
                        for (DocumentSnapshot document : queryDocumentSnapshots) {
                            Vehicle vehicle = document.toObject(Vehicle.class);
                            vehicleList.add(vehicle);
                        }
                        callback.onSuccess(vehicleList);
                    } else {
                        callback.onFailure("No vehicles found for this user.");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onFailure("Error fetching vehicles: " + e.getMessage());
                });
    }



    public interface VehicleCreateCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface TripCreateCallback {
        void onSuccess();
        void onFailure(String error);
    }

    public interface TripsFetchCallback {
        void onSuccess(List<Trip> trips);
        void onFailure(String error);
    }

    public interface VehiclesFetchCallback {
        void onSuccess(List<Vehicle> vehicles);
        void onFailure(String error);
    }

    public interface FirebaseUserCallback {
        void onSuccess(Boolean driver);
        void onFailure(String error);
    }

    public interface UserCreationCallback {
        void onSuccess(boolean success);
    }
}
