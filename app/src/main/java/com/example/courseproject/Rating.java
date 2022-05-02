package com.example.courseproject;

public class Rating {

    private String rating = "";
    private String title = "";
    private String image = "";

    public Rating(String rating, String title, String image) {
        this.rating = rating;
        this.title = title;
        this.image = image;
    }

    public String getRating() {
        return this.rating;
    }

    public String getTitle() {
        return this.title;
    }

    public String getImage() {
        return this.image;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
