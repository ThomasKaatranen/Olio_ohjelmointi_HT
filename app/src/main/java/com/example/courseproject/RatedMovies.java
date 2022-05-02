package com.example.courseproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;

public class RatedMovies extends AppCompatActivity {

    private FirebaseUser user;
    private String name;

    private RecyclerView recycle;
    private RatedMovieRecyclerView ratedMovieRecyclerAdapter;
    private TextView userName;
    private EditText searchParameter;

    Context context;

    UserActionHandler uah = UserActionHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rated_movies);
        context = getApplicationContext();
        Objects.requireNonNull(getSupportActionBar()).hide();

        userName = (TextView) findViewById(R.id.textView6);
        recycle = (RecyclerView) findViewById(R.id.getRecyclerView);
        searchParameter = (EditText) findViewById(R.id.searchBar);

        //Get userInfo (email)
        user = FirebaseAuth.getInstance().getCurrentUser();


        //Get users name
        if (getIntent().hasExtra("userinfo")) {
            String str = (String) getIntent().getSerializableExtra("userinfo");
            name = str;
        }

        //Display username
        userName.setText("Name: " + name);

        ArrayList<Rating> ratedMovies;

        ratedMovies = readUserActionFromJson();

        if (ratedMovies != null) {

            //https://stackoverflow.com/questions/13434143/sorting-a-double-value-of-an-object-within-an-arraylist
            //Sort list from best rating to worst
            Collections.sort(ratedMovies, new Comparator<Rating>() {
                @Override
                public int compare(Rating c1, Rating c2) {
                    return Double.compare(Float.parseFloat(c2.getRating()),
                            Float.parseFloat(c1.getRating()));
                }
            });
            setUpRatedMoviesRecyclerView(ratedMovies);
        }

        if (searchParameter.getText().toString().equals("")) {
            setUpRatedMoviesRecyclerView(ratedMovies);
        }

        //If searchParameters are given get specific movies
        EditText searchParameter = (EditText) findViewById(R.id.searchBar);
        searchParameter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    ArrayList<Rating> specificMovies = new ArrayList<>();
                    if (ratedMovies != null) {
                        for (int i = 0; i < ratedMovies.size(); i++) {
                            if (ratedMovies.get(i).getTitle().toLowerCase().contains(searchParameter.getText().toString())) {
                                specificMovies.add(ratedMovies.get(i));
                            }
                        }
                        setUpRatedMoviesRecyclerView(specificMovies);
                        ratedMovieRecyclerAdapter.setRatedMoviesList(specificMovies);
                        return true;
                    }
                }
                return false;
            }
        });

    }

    private void setUpRatedMoviesRecyclerView(ArrayList<Rating> ratedMovies) {

        ratedMovieRecyclerAdapter = new RatedMovieRecyclerView(ratedMovies);

        recycle.setAdapter(ratedMovieRecyclerAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    // Read a specific users ratings from json file
    private ArrayList<Rating> readUserActionFromJson() {
        String jsonString = "";
        try {
            jsonString = uah.getJsonFileAsString(context);

            Type userList = new TypeToken<ArrayList<User>>() {}.getType();
            Gson gson = new Gson();
            ArrayList<User> listOfUsers = gson.fromJson(jsonString, userList);

            User aUser = null;
            for (int i = 0; i < listOfUsers.size(); i++) {
                aUser = listOfUsers.get(i);
                if (aUser.getEmail().equals(user.getEmail())) {
                    return aUser.getRatings();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}