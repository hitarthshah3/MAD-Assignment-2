package com.example.assignment2.model;

public class UserProfile {
    private String uid;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String favoriteCuisine;

    public UserProfile() {
    }

    public UserProfile(String uid, String name, String email, String phone, String address, String favoriteCuisine) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.favoriteCuisine = favoriteCuisine;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getFavoriteCuisine() {
        return favoriteCuisine;
    }

    public void setFavoriteCuisine(String favoriteCuisine) {
        this.favoriteCuisine = favoriteCuisine;
    }
}
