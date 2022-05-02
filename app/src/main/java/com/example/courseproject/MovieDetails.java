package com.example.courseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class MovieDetails extends AppCompatActivity {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private ImageView imageView;
    private TextView movieTitle, synopsis;
    private RatingBar ratingStars;
    private Button rateButton;

    Context context = null;

    private float myRating = 0;

    private String givenRating = "";
    private String title = "";
    private String image = "";
    private String name = "";

    ArrayList<User> users = new ArrayList<>();
    ArrayList<User> usersRating = new ArrayList<>();

    UserActionHandler uah = UserActionHandler.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        context = getApplicationContext();

        imageView = (ImageView) findViewById(R.id.imageView);

        movieTitle = (TextView) findViewById(R.id.movie_title);
        synopsis = (TextView) findViewById(R.id.synopsis);

        ratingStars = (RatingBar) findViewById(R.id.ratingBar);

        rateButton = (Button) findViewById(R.id.button);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        getMovieInfo();
        getJsonInfoToList();

        //Get user information (name)
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    name = userProfile.getName();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MovieDetails.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });

        ratingStars.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {

                int rating = (int) v;
                String message = "";
                myRating = ratingBar.getRating();

                switch (rating) {
                    case 1:
                        message = "1 Star";
                        break;

                    case 2:
                        message = "2 Stars";
                        break;

                    case 3:
                        message = "3 Stars";
                        break;

                    case 4:
                        message = "4 Stars";
                        break;

                    case 5:
                        message = "5 Stars";
                        break;
                }
                Toast.makeText(MovieDetails.this, message, Toast.LENGTH_SHORT).show();
                givenRating = String.valueOf(myRating);
            }
        });

        rateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(MovieDetails.this, "Star rating: " + String.valueOf(myRating), Toast.LENGTH_LONG).show();

                ArrayList<Rating> ratedMovie = new ArrayList<>();

                Rating rating = new Rating(givenRating, title, image);
                ratedMovie.add(rating);

                User currentUser = getUser(name, user.getEmail());

                //Only used if json file doesn't already exist
                usersRating.add(new User(name, user.getEmail(), ratedMovie));

                uah.setMovieRating(currentUser, rating);
                uah.writeUserActionToFile(context, users);
                startActivity(new Intent(MovieDetails.this, Entry.class));
            }
        });
    }

    private void getJsonInfoToList() {
        try {
            String jsonString = uah.getJsonFileAsString(context);

            Type userList = new TypeToken<ArrayList<User>>() {}.getType();
            Gson gson = new Gson();
            users = gson.fromJson(jsonString, userList);

        } catch (IOException e) {
            uah.createUserActionFile(usersRating, context);
            System.out.println(e.getMessage());
        }

    }

    //Get users information. If user is not in <User> add it. Return the current user
    private User getUser(String name, String email) {

        for (User user: users) {
            //Check if a specific user email is already in user
            if (user.getEmail().equals(email)) {
                return user;
            }
        }
        User aUser = new User(name, email, null);
        users.add(aUser);
        return aUser;

    }

    // Get a movies info that was clicked
    private void getMovieInfo() {
        if (getIntent().hasExtra("movie")) {
            Event event = (Event) getIntent().getSerializableExtra("movie");

            title = event.getTitle();
            image = event.getImage();

            movieTitle.setText(event.getTitle());
            synopsis.setText(event.getSynopsis());

            Glide.with(this).load(event.getImage()).into(imageView);
        }
    }

}