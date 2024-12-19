package com.example.carpoolingapp;


import com.google.firebase.firestore.PropertyName;

public class User {
    private String name;
    private String email;
    private String phone;
    private boolean driver;
    private Integer revSum;
    private Integer revCnt;

    public User(){}

    public User(String name, String email, String phone, boolean driver, Integer revSum, Integer revCnt) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.driver = driver;
        this.revSum = revSum;
        this.revCnt = revCnt;
    }

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
    public boolean getDriver() {  // Getter for driver
        return driver;
    }

    public void setDriver(boolean driver) {
        this.driver = driver;
    }
    public Integer getRevSum() {  // Getter for driver
        return revSum;
    }

    public void setRevSum(Integer revSum) {
        this.revSum = revSum;
    }
    public Integer getRevCnt() {  // Getter for driver
        return revCnt;
    }

    public void setRevCnt(Integer revCnt) {
        this.revCnt = revCnt;
    }
}
