package com.example.courseproject;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView title, genre, duration;
    public ImageView imageView;

    OnMovieClickListener onMovieClickListener;

    public MovieViewHolder(@NonNull View itemView, OnMovieClickListener onMovieClickListener) {
        super(itemView);

        this.onMovieClickListener = onMovieClickListener;

        title = itemView.findViewById(R.id.movieTitle);
        genre = itemView.findViewById(R.id.movieGenre);
        duration = itemView.findViewById(R.id.movieDuration);

        imageView = itemView.findViewById(R.id.movieImage);

        itemView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        System.out.println("ADAPTER POSITION IS: " + getAdapterPosition());
        onMovieClickListener.onMovieClick(getAdapterPosition());
    }
}
