package edu.uci.ics.fabflixmobile;

import android.content.Context;
import android.graphics.Movie;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class MovieListViewAdapter extends ArrayAdapter<SingleMovie>{
    private ArrayList<SingleMovie> movies;

    public MovieListViewAdapter(ArrayList<SingleMovie> movies, Context context)
    {
        super(context, R.layout.layout_listview_row, movies);
        this.movies = movies;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.layout_listview_row, parent, false);
        SingleMovie movie = movies.get(position);

        TextView titleView = (TextView)view.findViewById(R.id.title);
        TextView subtitleView = (TextView)view.findViewById(R.id.subtitle);
        TextView movie_director = (TextView)view.findViewById(R.id.movie_director);
        TextView movie_genre = (TextView)view.findViewById(R.id.movie_genre);
        TextView movie_star = (TextView)view.findViewById(R.id.movie_star);

        titleView.setText("Movie Title: " + movie.getTitle());
        subtitleView.setText("Released Year: " + movie.getYear());
        movie_director.setText("Movie Director: " + movie.getDirector());
        movie_genre.setText("List of Genre: " + movie.getGenre());
        movie_star.setText("List of Star: " + movie.getStar());


        return view;
    }

}
