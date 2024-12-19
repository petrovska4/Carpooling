package com.example.carpoolingapp;


import android.os.Parcel;
import android.os.Parcelable;

public class Vehicle implements Parcelable {
    private String type;
    private String brand;
    private String seats;
    private String color;
    private String licence;
    private String ownerId;
    private String location;
    private String price;

    public Vehicle() {
    }

    public Vehicle(String type, String brand, String color, String seats, String licence, String ownerId, String location, String price) {
        this.type = type;
        this.brand = brand;
        this.color = color;
        this.seats = seats;
        this.licence = licence;
        this.ownerId = ownerId;
        this.location = location;
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSeats() {
        return seats;
    }

    public void setSeats(String seats) {
        this.seats = seats;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getLicence() {
        return licence;
    }

    public void setLicence(String licence) {
        this.licence = licence;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }


    protected Vehicle(Parcel in) {
        type = in.readString();
        brand = in.readString();
        color = in.readString();
        seats = String.valueOf(in.readInt());
        licence = in.readString();
        ownerId = in.readString();
        location = in.readString();
        price = in.readString();
    }

    public static final Creator<Vehicle> CREATOR = new Creator<Vehicle>() {
        @Override
        public Vehicle createFromParcel(Parcel in) {
            return new Vehicle(in);
        }

        @Override
        public Vehicle[] newArray(int size) {
            return new Vehicle[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type);
        dest.writeString(brand);
        dest.writeString(color);
        dest.writeInt(Integer.parseInt(seats));
        dest.writeString(licence);
        dest.writeString(ownerId);
        dest.writeString(location);
        dest.writeString(price);
    }
}
