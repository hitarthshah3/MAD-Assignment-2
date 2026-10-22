package com.example.assignment2.model;

public class Restaurant {
    private int id;
    private String name;
    private String cuisine;
    private String location;

    public Restaurant() {
    }

    public Restaurant(int id, String name, String cuisine, String location) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.location = location;
    }

    public Restaurant(String name, String cuisine, String location) {
        this.name = name;
        this.cuisine = cuisine;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
