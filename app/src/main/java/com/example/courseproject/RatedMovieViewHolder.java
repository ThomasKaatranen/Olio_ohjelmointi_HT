package com.example.courseproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RatedMovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView movieTitle;
    public ImageView movieImage;
    public RatingBar ratingBar;


    public RatedMovieViewHolder(@NonNull View itemView) {
        super(itemView);

        movieTitle = itemView.findViewById(R.id.getTitle);
        movieImage = itemView.findViewById(R.id.getImage);
        ratingBar = itemView.findViewById(R.id.getRating);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        System.out.println("NOTHING HAPPENS!");
    }
}
