package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlueActivity extends ActionBarActivity {

    String message = "";
    int current_num = 0;
    int range = 0;
    int  movie_num = 0;
    String mTitle = "";
    String m_id = "";
    final ArrayList<SingleMovie> list_movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        Bundle bundle = getIntent().getExtras();

        message= bundle.getString("message");

        if (message != null && !"".equals(message)) {
            try {
                JSONArray jsonArray = new JSONArray(message);
                JSONObject movie = (JSONObject)jsonArray.get(0);
                String movies = movie.getString("num");
                String first_record = movie.getString("mfirstRecord");
                String movie_range = movie.getString("mnumRecord");
                mTitle = movie.getString("mtitle");
                movie_num = Integer.valueOf(movies);
                range = Integer.valueOf(movie_range);
                current_num = Integer.valueOf(first_record);

            }catch(Exception e){e.printStackTrace();}
        }
        create_table();

    }
    public void goToPrevious(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // 10.0.2.2 is the host machine when running the android emulator
        int previous_num = 0;
        if(current_num - range<0){
            previous_num = 0;
        }
        else{
            previous_num = current_num - range;
        }
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "https://ec2-18-217-240-243.us-east-2.compute.amazonaws.com:8443/project4/api/movies?title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+Integer.toString(previous_num)+"&numRecord="+Integer.toString(range)+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        fresh_page(view);
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
        queue.add(afterSearchRequest);
    }
    public void goToNext(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        int next = 0;
        if(current_num + range >= movie_num){
            next = current_num;
        }
        else{
            next = current_num + range;
        }
        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "https://ec2-18-217-240-243.us-east-2.compute.amazonaws.com:8443/project4/api/movies?title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+Integer.toString(next)+"&numRecord="+Integer.toString(range)+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        fresh_page(view);
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
        queue.add(afterSearchRequest);
    }
    public void refresh(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;


        String item = ((EditText) findViewById(R.id.item)).getText().toString();

        Log.d("something!!!!!!!!!", item);

        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "https://ec2-18-217-240-243.us-east-2.compute.amazonaws.com:8443/project4/api/movies?title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+Integer.valueOf(current_num)+"&numRecord="+Integer.valueOf(item)+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        fresh_page(view);
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
        queue.add(afterSearchRequest);
    }
    public void create_table(){
        Log.d("lll","lll");

        for(int i = -1; i<range; ++i){
            try {
                // get the movie data
                JSONArray jsonArray = new JSONArray(message);
                JSONObject movie = (JSONObject) jsonArray.get(i);

                String movie_title = movie.getString("movie_title");
                m_id = movie.getString("movie_id");
                mTitle = movie.getString("mtitle");
                String movie_director = movie.getString("movie_director");
                String release_year = movie.getString("movie_year");
                JSONArray movie_genre = movie.getJSONArray("movie_genre");
                JSONArray movie_star = movie.getJSONArray("movie_star");

                list_movies.add(new SingleMovie(m_id, movie_title, release_year, movie_director, movie_genre, movie_star));
                MovieListViewAdapter adapter = new MovieListViewAdapter(list_movies, this);

                ListView listView = (ListView) findViewById(R.id.list);
                listView.setAdapter(adapter);

                listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                    {
                        SingleMovie movie = list_movies.get(position);

                        Intent intent = new Intent(BlueActivity.this, SingleMovieActivity.class);
                        intent.putExtra("ClickValue", movie.getMovie_id());
                        intent.putExtra("MTITLE", mTitle);
                        intent.putExtra("First", Integer.toString(current_num));
                        intent.putExtra("Range", Integer.toString(range));
                        startActivity(intent);
                    }
                });

            }catch (Exception e){e.printStackTrace();}

        }

        Log.d("lll","222");
    }
    public void goBackToSearch(View view) {
        String msg = "";

        Intent goToIntent = new Intent(this, GreenActivity.class);

        goToIntent.putExtra("last_activity", "blue");
        goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }
    public void fresh_page(View view) {

        Intent goToIntent = new Intent(this, BlueActivity.class);

        goToIntent.putExtra("last_activity", "green");
        goToIntent.putExtra("message", message);

        startActivity(goToIntent);
    }

}

/*
package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class BlueActivity extends ActionBarActivity {


    String message = "";
    int current_num = 0;
    int range = 0;
    int  movie_num = 0;
    String mTitle = "";
    String m_id = "";
    private TableLayout movie_table;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue);

        Bundle bundle = getIntent().getExtras();
        //  Toast.makeText(this, "Last activity was " + bundle.get("last_activity") + ".", Toast.LENGTH_LONG).show();

        movie_table = (TableLayout)findViewById(R.id.movie_table);
        movie_table.setStretchAllColumns(true);

        message= bundle.getString("message");

        if (message != null && !"".equals(message)) {
            //((TextView) findViewById(R.id.last_page_msg_container)).setText(message);
            try {
                JSONArray jsonArray = new JSONArray(message);
                JSONObject movie = (JSONObject)jsonArray.get(0);
                String movies = movie.getString("num");
                String first_record = movie.getString("mfirstRecord");
                String movie_range = movie.getString("mnumRecord");
                mTitle = movie.getString("mtitle");
                movie_num = Integer.valueOf(movies);
                range = Integer.valueOf(movie_range);
                current_num = Integer.valueOf(first_record);

            }catch(Exception e){e.printStackTrace();}
        }
        create_table();

    }
    public void goToPrevious(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        // 10.0.2.2 is the host machine when running the android emulator
        int previous_num = 0;
        if(current_num - range<0){
            previous_num = 0;
        }
        else{
            previous_num = current_num - range;
        }
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/project4/api/movies?title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+Integer.toString(previous_num)+"&numRecord="+Integer.toString(range)+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        fresh_page(view);
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
        queue.add(afterSearchRequest);
    }
    public void goToNext(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        int next = 0;
        if(current_num + range >= movie_num){
            next = current_num;
        }
        else{
            next = current_num + range;
        }
        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/project4/api/movies?title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+Integer.toString(next)+"&numRecord="+Integer.toString(range)+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        fresh_page(view);
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
        queue.add(afterSearchRequest);
    }
    public void refresh(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        String item = ((EditText) findViewById(R.id.item)).getText().toString();
        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/project4/api/movies?title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+Integer.valueOf(current_num)+"&numRecord="+Integer.valueOf(item)+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        fresh_page(view);
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
        queue.add(afterSearchRequest);
    }
    public void goToSingleMovie(View view) {

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;

        //String item = ((EditText) findViewById(R.id.item)).getText().toString();

        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "http://10.0.2.2:8080/project4/api/single-movie?id="+m_id+"&title=" + mTitle + "&year=null&director=null&star=null&firstRecord="+Integer.valueOf(current_num)+"&numRecord="+Integer.valueOf(range)+"&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        message = response;
                        singleMovie(view);
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
        queue.add(afterSearchRequest);
    }
    public void create_table(){
        Log.d("lll","lll");
        TableRow table = new TableRow(this);
        TextView infor = null;
        movie_table.removeAllViews();
        for(int i = -1; i<range; ++i){
            try {
                JSONArray jsonArray = new JSONArray(message);
                JSONObject movie = (JSONObject) jsonArray.get(i);
                String m_title = movie.getString("movie_title");
                m_id = movie.getString("movie_id");
                String m_director = movie.getString("movie_director");
                String m_year = movie.getString("movie_year");
                JSONArray genre_l = movie.getJSONArray("movie_genre");
                JSONArray star_l = movie.getJSONArray("movie_star");
                final TextView mTv = new TextView(this);

                if(i == -1){
                    mTv.setText("Title");
                }
                else{
                    mTv.setClickable(true);
                    mTv.setOnClickListener(this::goToSingleMovie);
                    mTv.setText(m_title);
                }
                final TextView spaceTv1 = new TextView(this);
                spaceTv1.setText("  ");
                final TextView dTv = new TextView(this);
                if(i == -1){
                    dTv.setText("Director");
                }
                else{
                    dTv.setText(m_director);
                }
                final TextView spaceTv4 = new TextView(this);
                spaceTv4.setText("  ");
                final TextView yTv = new TextView(this);
                if(i == -1){
                    yTv.setText("Year");
                }
                else{
                    yTv.setText(m_year);
                }
                final TextView spaceTv2 = new TextView(this);
                spaceTv2.setText("  ");
                final TableLayout genre_table = new TableLayout(this);
                for(int x = 0; x < genre_l.length(); ++x){
                    final TextView gTv = new TextView(this);
                    String genre = String.valueOf(genre_l.get(x));
                    gTv.setText(genre);
                    final TableRow gtr = new TableRow(this);
                    gtr.addView(gTv);
                    genre_table.addView(gtr);
                }
                final TextView spaceTv3 = new TextView(this);
                spaceTv3.setText("  ");
                final TableLayout star_table = new TableLayout(this);
                for(int y = 0; y < star_l.length(); ++y){
                    final TextView sTv = new TextView(this);
                    String star = String.valueOf(star_l.get(y));
                    sTv.setText(star);
                    final TableRow str = new TableRow(this);
                    str.addView(sTv);
                    star_table.addView(str);
                }

                final TableRow tr = new TableRow(this);
//                TableLayout.LayoutParams trParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT);
//                tr.setPadding(0,0,0,0);
//                tr.setLayoutParams(trParams);
                tr.addView(mTv);
                tr.addView(spaceTv1);
                tr.addView(dTv);
                tr.addView(spaceTv4);
                tr.addView(yTv);
                tr.addView(spaceTv2);
                tr.addView(genre_table);
                tr.addView(spaceTv3);
                tr.addView(star_table);
                movie_table.addView(tr);

            }catch (Exception e){e.printStackTrace();}

        }
        Log.d("lll","222");
    }


    public void goBackToSearch(View view) {
        String msg = "";

        Intent goToIntent = new Intent(this, GreenActivity.class);

        goToIntent.putExtra("last_activity", "blue");
        goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }
    public void singleMovie(View view) {
        //String msg = "";

        Intent goToIntent = new Intent(this, SingleMovieActivity.class);

        goToIntent.putExtra("last_activity", "blue");
        goToIntent.putExtra("message", message);

        startActivity(goToIntent);
    }
    public void fresh_page(View view) {

        Intent goToIntent = new Intent(this, BlueActivity.class);

        goToIntent.putExtra("last_activity", "green");
        goToIntent.putExtra("message", message);

        startActivity(goToIntent);
    }

}
*/