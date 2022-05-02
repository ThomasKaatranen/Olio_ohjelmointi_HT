package com.example.courseproject;

import android.content.Context;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class UserActionHandler {


    Context getContext = null;


    private static UserActionHandler uah = null;

    public static UserActionHandler getInstance() {
        if (uah == null) {
            uah = new UserActionHandler();
        }
        return uah;
    }

    /* This method is only called if there is no UserAction.json file
    Method takes in ArrayList of a users rated movies and writes them into a json file */
    public void createUserActionFile(ArrayList<User> ratedMovieToFile, Context getContext) {
        try {
            File file = new File(getContext.getFilesDir(), "UserAction.json");
            FileWriter fileWriter = new FileWriter(file, false);
            new Gson().toJson(ratedMovieToFile, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //Method takes in an ArrayList of a users rated movies and writes them into a json file
    public void writeUserActionToFile(Context context, ArrayList<User> ratedMovieToFile) {
        getContext = context;
        try {
            File file = new File(getContext.getFilesDir(), "UserAction.json");
            FileWriter fileWriter = new FileWriter(file, false);
            new Gson().toJson(ratedMovieToFile, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Get jsonFile information as string
    public String getJsonFileAsString (Context getContext) throws IOException {
        File file = new File(getContext.getFilesDir(), "UserAction.json");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        return sb.toString();
    }

    public void setMovieRating(User user, Rating rating) {

            boolean found = false;
            for (Rating userRating: user.getRatings()) {
                //Checking if rated movies is already rated and override the previous rating
                if (userRating.getTitle().equals(rating.getTitle())) {
                    userRating.setRating(rating.getRating());
                    found = true;
                }
            }
            if (!found) {
                user.addRating(rating);
            }
    }

}

