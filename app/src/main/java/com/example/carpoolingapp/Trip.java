package com.example.carpoolingapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Trip implements Parcelable {
    private String id, name, email, phone, dateFrom, dateTo, vehicle, brand, color, seats, licencePlate, owner, createdBy;

    public Trip() {
    }

    public Trip(String name, String email, String phone, String dateFrom, String dateTo, String vehicle, String brand, String color, String seats, String licencePlate, String owner, String createdBy) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.vehicle = vehicle;
        this.brand = brand;
        this.color = color;
        this.seats = seats;
        this.licencePlate = licencePlate;
        this.owner = owner;
        this.createdBy = createdBy;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getLicencePlate() {
        return licencePlate;
    }

    public void setLicencePlate(String licencePlate) {
        this.licencePlate = licencePlate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    protected Trip(Parcel in) {
        name = in.readString();
        email = in.readString();
        phone = in.readString();
        dateFrom = in.readString();
        dateTo = in.readString();
        vehicle = in.readString();
        brand = in.readString();
        color = in.readString();
        seats = String.valueOf(in.readInt());
        licencePlate = in.readString();
        owner = in.readString();
        createdBy = in.readString();
    }

    public static final Creator<Trip> CREATOR = new Creator<Trip>() {
        @Override
        public Trip createFromParcel(Parcel in) {
            return new Trip(in);
        }

        @Override
        public Trip[] newArray(int size) {
            return new Trip[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(phone);
        dest.writeString(dateFrom);
        dest.writeString(dateTo);
        dest.writeString(vehicle);
        dest.writeString(brand);
        dest.writeString(color);
        dest.writeInt(Integer.parseInt(seats));
        dest.writeString(licencePlate);
        dest.writeString(owner);
        dest.writeString(createdBy);
    }
}
