package com.example.courseproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class MovieRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private ArrayList<Event> eventList;
    private OnMovieClickListener onMovieClickListener;

    public MovieRecyclerView(OnMovieClickListener onMovieClickListener, ArrayList<Event> arrayList) {
        eventList = arrayList;
        this.onMovieClickListener = onMovieClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, parent, false);
        return new MovieViewHolder(view, onMovieClickListener);
    }

    @Override // Set the recyclerview with specific Event (Object) info
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        ((MovieViewHolder)holder).title.setText(eventList.get(position).getTitle());
        ((MovieViewHolder)holder).duration.setText(eventList.get(position).getDuration());
        ((MovieViewHolder)holder).genre.setText(eventList.get(position).getGenre());

        //Show a .jpg image in RecyclerView with the usage of Glide library
        Glide.with(holder.itemView.getContext()).load(eventList.get(position).getImage())
                .into(((MovieViewHolder)holder).imageView);

    }

    @Override
    public int getItemCount() {
        if (eventList != null) {
            return eventList.size();
        }
        return 0;
    }

    public void setEventsList(ArrayList<Event> eventList) {
        this.eventList = eventList;
        notifyDataSetChanged();
    }

    //Get selected movie's position in Arraylist<Event>
    public Event getSelectedMovie(int position) {
        if (eventList != null) {
            if (eventList.size() > 0) {
                return eventList.get(position);
            }
        }
        return null;
    }

}
