package com.example.courseproject;

import java.io.Serializable;

public class Event implements Serializable {

    private String ID;
    private String title;
    private String duration;
    private String image;
    private String genre;
    private String synopsis;

    public Event(String ID, String title, String duration, String image, String genre, String synopsis) {
        this.ID = ID;
        this.title = title;
        this.duration = duration;
        this.image = image;
        this.genre = genre;
        this.synopsis = synopsis;
    }

    public String getID() {
        return this.ID;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDuration() {
        return this.duration;
    }

    public String getImage() {
        return this.image;
    }

    public String getGenre() {
        return this.genre;
    }

    public String getSynopsis() {
        return this.synopsis;
    }

    // Display json file in its string format
    public String asString() {
        return "ID: " + this.ID + ", Title: " + this.title + ", Duration: "
                + this.duration + ", Image: " + this.image + ", Genre: "
                + this.genre + ", Synopsis: " + this.synopsis;
    }
}
