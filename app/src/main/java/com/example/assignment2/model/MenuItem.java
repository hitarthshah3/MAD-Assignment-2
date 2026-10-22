package com.example.assignment2.model;

public class MenuItem {
    private String id;
    private String name;
    private double price;
    private int imageResId;
    private String restaurantName;

    public MenuItem() {
    }

    public MenuItem(String id, String name, double price, int imageResId, String restaurantName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.restaurantName = restaurantName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
