package edu.uci.ics.fabflixmobile;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ListViewActivity extends Activity{
    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);

        final ArrayList<SingleMovie> movies = new ArrayList<>();

        MovieListViewAdapter adapter = new MovieListViewAdapter(movies, this);

        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                SingleMovie movie = movies.get(position);
                String message = String.format("Clicked on position: %d, %s", position + 1, movie.getTitle());
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
