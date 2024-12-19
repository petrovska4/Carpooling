package com.example.carpoolingapp;

public class Review {
    private String userId;
    private String tripId;
    private int review;

    public Review() {
    }

    public Review(String userId, String tripId, int review) {
        this.userId = userId;
        this.tripId = tripId;
        this.review = review;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
    }

    public int getReview() {
        return review;
    }

    public void setReview(int review) {
        this.review = review;
    }
}
