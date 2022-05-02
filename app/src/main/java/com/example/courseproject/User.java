package com.example.courseproject;

import java.util.ArrayList;

public class User {

    private String name, email;
    private ArrayList<Rating> ratings = new ArrayList<>();

    public User() {

    }

    public User(String name, String email, ArrayList<Rating> ratings) {
        this.name = name;
        this.email = email;
        if (ratings != null) {
            this.ratings = ratings;
        }
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public ArrayList<Rating> getRatings() {
        return this.ratings;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setRatings(ArrayList<Rating> ratings) {
        this.ratings = ratings;
    }

    public void addRating(Rating rating) {
        this.ratings.add(rating);
    }
}
