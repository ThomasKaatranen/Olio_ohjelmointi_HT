package com.example.courseproject;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ReadingWritingFile {

    ArrayList<Event> movieListFromJson = new ArrayList<>();
    ArrayList<Event> eventList = new ArrayList<>();
    Context getContext = null;


    private static ReadingWritingFile rwf = null;

    public static ReadingWritingFile getInstance() {
        if (rwf == null) {
            rwf = new ReadingWritingFile();
        }
        return rwf;
    }


    /* This method is only called if there is no myJSON.json file
    Method takes in ArrayList of events and writes them into a json file */
    public void createJsonFile(ArrayList<Event> listOfMovies) {
        try {
            File file = new File(getContext.getFilesDir(), "myJSON.json");
            FileWriter fileWriter = new FileWriter(file, false);
            new Gson().toJson(listOfMovies, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        readJsonFile(listOfMovies, getContext);

    }

    //Method takes in an ArrayList of events and writes them into a json file
    public void writeToFile(ArrayList<Event> listFromGson) {
        try {
            File file = new File(getContext.getFilesDir(), "myJSON.json");
            FileWriter fileWriter = new FileWriter(file, false);
            new Gson().toJson(listFromGson, fileWriter);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //This method reads a json files info and returns it as a string
    public String getJsonFileAsString () throws IOException {
        File file = new File(getContext.getFilesDir(), "myJSON.json");
        FileReader fileReader = new FileReader(file);
        BufferedReader br = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line);

        }
        return sb.toString();
    }

    /* This method takes in a ArrayList of events and compares them to the events in a json file
    Returns an ArrayList */
    public ArrayList<Event> readJsonFile(ArrayList<Event> listOfMovieEvents, Context context) {
        String jsonString = "";
        getContext = context;
        eventList = listOfMovieEvents;


        try {
            Gson json = new Gson();
            jsonString = getJsonFileAsString();
            System.out.println("Json String: " + jsonString);

            //https://stackoverflow.com/questions/50370859/how-to-convert-json-string-into-arraylist-of-objects
            movieListFromJson = json.fromJson(jsonString, new TypeToken<ArrayList<Event>>() {}.getType());

            int c = 0;
            boolean found = false;
            for (int i = 0; i < eventList.size(); i++) {
                found = false;
                for (int j = 0; j < movieListFromJson.size(); j++) {
                    //Compare events with 2 arraylists. 1 is given as a parameter and the other one is filled with json file info
                    if (movieListFromJson.get(j).asString().equals(eventList.get(i).asString())) {
                        found = true;
                        System.out.println("FOUND!" + i);
                        break;
                    }
                }
                //If an event is not found add it to the arraylist
                if (!found) {
                    for (int j = 0; j < movieListFromJson.size(); j++) {
                        if (movieListFromJson.get(j).getID().equals(eventList.get(i).getID())) {
                            //ID WAS FOUND BUT THE CONTENTS WERE DIFFERENT SO REPLACE IT
                            System.out.println("Removed!" + i);
                            movieListFromJson.remove(i);
                            break;
                        }
                    }
                    c++;
                    movieListFromJson.add(eventList.get(i));
                    System.out.println("NOT FOUND!" + i);
                }

            }
            if (c >= 1) {
                writeToFile(movieListFromJson);
            }

        } catch (IOException e) {
            createJsonFile(eventList);
        }

        return movieListFromJson;
    }

}
