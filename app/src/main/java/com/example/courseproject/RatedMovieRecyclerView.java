package com.example.courseproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RatedMovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Rating> ratedMovies;

    public RatedMovieRecyclerView(ArrayList<Rating> list) {
        ratedMovies = list;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rated_movies_list, parent, false);
        return new RatedMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((RatedMovieViewHolder)holder).movieTitle.setText(ratedMovies.get(position).getTitle());
        ((RatedMovieViewHolder)holder).ratingBar.setRating(Float.parseFloat(ratedMovies.get(position).getRating()));

        //Show a .jpg image in RecyclerView with the usage of Glide library
        Glide.with(holder.itemView.getContext()).load(ratedMovies.get(position).getImage())
                .into(((RatedMovieViewHolder)holder).movieImage);
    }

    @Override
    public int getItemCount() {
        if (ratedMovies != null) {
            return ratedMovies.size();
        }
        return 0;
    }

    public void setRatedMoviesList(ArrayList<Rating> ratedMovies) {
        this.ratedMovies = ratedMovies;
        notifyDataSetChanged();
    }
}
