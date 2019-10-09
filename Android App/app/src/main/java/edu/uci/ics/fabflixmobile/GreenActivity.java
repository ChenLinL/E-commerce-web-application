package edu.uci.ics.fabflixmobile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class GreenActivity extends ActionBarActivity {
    String msg = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green);

        Bundle bundle = getIntent().getExtras();


        String msg = bundle.getString("message");
       // if (msg != null && !"".equals(msg)) {
       //     ((TextView) findViewById(R.id.last_page_msg_container)).setText(msg);
      //  }
    }
    public void connectTomovie(View view) {

        // no user is logged in, so we must connect to the server

        // Use the same network queue across our application
        final RequestQueue queue = NetworkManager.sharedManager(this).queue;
        String query = ((EditText) findViewById(R.id.green_2_blue_message)).getText().toString();
        // 10.0.2.2 is the host machine when running the android emulator
        final StringRequest afterSearchRequest = new StringRequest(Request.Method.GET, "https://ec2-18-217-240-243.us-east-2.compute.amazonaws.com:8443/project4/api/movies?title="+query+"&year=null&director=null&star=null&firstRecord=0&numRecord=5&sortType=rating&sortOrder=DESC&title_i=null&genre=null",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("username.reponse", response);
                        msg = response;
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
        /*
        final StringRequest loginRequest = new StringRequest(Request.Method.POST, "http://10.0.2.2:8080/project4/api/login",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.d("login.success", response);
                        ((TextView) findViewById(R.id.http_response)).setText(response);
                        // Add the request to the RequestQueue.
                        //queue.add(afterLoginRequest);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("login.error", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                // Post request form data
                final Map<String, String> params = new HashMap<String, String>();
                String username = ((EditText) findViewById(R.id.red_2_blue_message)).getText().toString();
                String password = ((EditText) findViewById(R.id.red_2_green_message)).getText().toString();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        */

        // !important: queue.add is where the login request is actually sent
        queue.add(afterSearchRequest);

    }
    public void goToBlue(View view) {

        Intent goToIntent = new Intent(this, BlueActivity.class);

        goToIntent.putExtra("last_activity", "green");
        goToIntent.putExtra("message", msg);

        startActivity(goToIntent);
    }

}
