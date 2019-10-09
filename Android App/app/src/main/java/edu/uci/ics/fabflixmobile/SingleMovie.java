package edu.uci.ics.fabflixmobile;

import org.json.JSONArray;

import java.util.ArrayList;

public class SingleMovie {
    private String movie_id;
    private String movie_title;

    // May need to change to string
    private String ReleaseYear;

    private String director;
    private JSONArray genres;
    private JSONArray stars;

    public SingleMovie(String movie_id, String title, String year, String director, JSONArray genres, JSONArray stars)
    {
        this.movie_id = movie_id;
        this.movie_title = title;
        this.ReleaseYear = year;
        this.director = director;
        this.genres = genres;
        this.stars = stars;
    }

    public String getMovie_id() {return movie_id;}

    public String getTitle()
    {
        return movie_title;
    }

    public String getYear()
    {
        return ReleaseYear;
    }

    public String getDirector()
    {
        return director;
    }

    public JSONArray getGenre()
    {
        return genres;
    }

    public JSONArray getStar()
    {
        return stars;
    }
}
