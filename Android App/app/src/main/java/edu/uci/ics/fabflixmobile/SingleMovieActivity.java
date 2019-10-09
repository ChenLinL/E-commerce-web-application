package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class SingleMovieActivity extends ActionBarActivity {

    String message = "";
    String result= " ";
    String current_num = "";
    String range = "";
    String mTitle = "";
    String m_id = "";
    final ArrayList<SingleMovie> list_movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_movie);

        Bundle bundle = getIntent().getExtras();

        message= bundle.getString("ClickValue");
        mTitle = bundle.getString("MTITLE");
        current_num = bundle.getString("First");
        range = bundle.getString("Range");

        if (message != null && !"".equals(message)) {
            try {

                final RequestQueue queue = NetworkManager.sharedManager(this).queue;
                System.out.println(message);
                // 10.0.2.2 is the host machine when running the android emulator
                final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "https://ec2-18-217-240-243.us-east-2.compute.amazonaws.com:8443/project4/api/single-movie?id="+message+"&title=null&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("username.reponse", response);

                                result = response;
                                create_table();


                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("username.error", error.toString());
                            }
                        }
                );

                // !important: queue.add is where the login request is actually sent
                queue.add(afterSearchRequest);

            }catch(Exception e){e.printStackTrace();}
        }


    }


    public void BackToSearchResult(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "https://ec2-18-217-240-243.us-east-2.compute.amazonaws.com:8443/project4//api/movies?title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+current_num+"&numRecord="+range+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        goToBlue(view);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("username.error", error.toString());
                    }
                }
        );

        // !important: queue.add is where the login request is actually sent
        queue.add(afterSearchRequest);

    }
    public void create_table() {
        Log.d("lll", "creating table");

        try {
            System.out.println(result);
            JSONArray jsonArray = new JSONArray(result);
            JSONObject movie = (JSONObject) jsonArray.get(0);
            String movie_title = movie.getString("movie_title");
            m_id = movie.getString("movie_id");
            String movie_director = movie.getString("movie_director");
            String release_year = movie.getString("movie_year");
            JSONArray movie_genre = movie.getJSONArray("movie_genre");
            JSONArray movie_star = movie.getJSONArray("movie_star");
            System.out.println("?????????????????????" + movie.toString());

            list_movies.add(new SingleMovie(m_id, movie_title, release_year, movie_director, movie_genre, movie_star));
            MovieListViewAdapter adapter = new MovieListViewAdapter(list_movies, this);

            ListView listView = (ListView) findViewById(R.id.list);
            listView.setAdapter(adapter);

            /*
            final TableLayout star_table = new TableLayout(this);
            for (int y = 0; y < star_l.length(); ++y) {
                final TextView sTv = new TextView(this);
                String star = String.valueOf(star_l.get(y));
                sTv.setText(star);
                final TableRow str = new TableRow(this);
                str.addView(sTv);
                star_table.addView(str);
            }
            */

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("HERE", "!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }

    public void goToBlue(View view) {

        Intent goToIntent = new Intent(this, BlueActivity.class);

        goToIntent.putExtra("last_activity", "green");
        goToIntent.putExtra("message", message);

        startActivity(goToIntent);
    }

}

