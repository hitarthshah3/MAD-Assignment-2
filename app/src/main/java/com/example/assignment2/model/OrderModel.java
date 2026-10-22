package com.example.assignment2.model;

import java.io.Serializable;

public class OrderModel implements Serializable {
    private String orderId;
    private String restaurantName;
    private String date;
    private String itemName;
    private double price;
    private String status;
    private int imageResId;
    private String userUid;

    public OrderModel() {
    }

    public OrderModel(String orderId, String restaurantName, String date, String itemName, double price, String status, int imageResId, String userUid) {
        this.orderId = orderId;
        this.restaurantName = restaurantName;
        this.date = date;
        this.itemName = itemName;
        this.price = price;
        this.status = status;
        this.imageResId = imageResId;
        this.userUid = userUid;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }
}
