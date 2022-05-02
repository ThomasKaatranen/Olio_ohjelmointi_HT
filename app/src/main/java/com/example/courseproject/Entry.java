package com.example.courseproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Entry extends AppCompatActivity implements OnMovieClickListener {

    private FirebaseUser user;
    private DatabaseReference reference;
    private String userID;

    private TextView showName;
    private ImageButton signOut;
    private ImageButton star;
    private EditText searchParameter;

    private RecyclerView recycle;
    private MovieRecyclerView movieRecyclerAdapter;
    private String name = "";
    private int num = 0;

    Context context = null;

    ArrayList<Event> eventArrayList = new ArrayList<>();
    ArrayList<Event> movieEventList = new ArrayList<>();

    FinnkinoReader fr = FinnkinoReader.getInstance();
    ReadingWritingFile rwf = ReadingWritingFile.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        context = getApplicationContext();

        getSupportActionBar().hide();


        recycle = (RecyclerView) findViewById(R.id.recyclerView);
        showName = (TextView) findViewById(R.id.textView3);
        signOut = (ImageButton) findViewById(R.id.sign_out);
        star = (ImageButton) findViewById(R.id.star);
        searchParameter = (EditText) findViewById(R.id.getText);

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = user.getUid();

        searchParameter.setText("");

        if (getIntent().hasExtra("mainActivity")) {
            num = (int) getIntent().getSerializableExtra("mainActivity");
        }

        if (num == 1) {
            eventArrayList = fr.readEventsXML();
            num++;
        }

        movieEventList = rwf.readJsonFile(eventArrayList, context);

        //If no search parameters given show all movies
        if (searchParameter.getText().toString().equals("")) {
            setUpRecyclerView(movieEventList);
        }


        //If searchParameters are given get specific movies
        EditText searchParameter = (EditText) findViewById(R.id.getText);
        searchParameter.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press

                    ArrayList<Event> specificMovies = new ArrayList<>();
                    if (movieEventList != null) {
                        for (int i = 0; i < movieEventList.size(); i++) {
                            if (movieEventList.get(i).getTitle().toLowerCase().contains(searchParameter.getText().toString())) {
                                System.out.println("This movie title contains " + searchParameter.getText().toString() + ": " + movieEventList.get(i).getTitle());
                                specificMovies.add(movieEventList.get(i));
                            }
                        }
                        setUpRecyclerView(specificMovies);
                        movieRecyclerAdapter.setEventsList(specificMovies);
                        return true;
                    }
                }
                return false;
            }
        });

        //Get current users information
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if (userProfile != null) {
                    name = userProfile.getName();
                    showName.setText("Welcome " + name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Entry.this, "Something went wrong!", Toast.LENGTH_LONG).show();
            }
        });


        //Sing out user if button is clicked
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Entry.this, MainActivity.class));
            }
        });

        //Go to new activity to watch rated movies
        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Entry.this, RatedMovies.class);
                intent.putExtra("userinfo", name);
                startActivity(intent);
            }
        });
    }

    private void setUpRecyclerView(ArrayList<Event> movies) {

        movieRecyclerAdapter = new MovieRecyclerView(this, movies);

        recycle.setAdapter(movieRecyclerAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    /* If movie is clicked get its information and go to a different activity
    https://stackoverflow.com/questions/2736389/how-to-pass-an-object-from-one-activity-to-another-on-android */
    @Override
    public void onMovieClick(int position) {
        Intent intent = new Intent(Entry.this, MovieDetails.class);
        intent.putExtra("movie", movieRecyclerAdapter.getSelectedMovie(position));
        startActivity(intent);
    }
}
