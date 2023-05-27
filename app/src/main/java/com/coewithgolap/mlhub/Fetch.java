package com.coewithgolap.mlhub;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.coewithgolap.mlhub.bottomActivities.DetectActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import im.delight.android.location.SimpleLocation;

public class Fetch extends AsyncTask<Void, Void, Void> {
    private String city = "Guwahati";
    private DetectActivity activity;

    public Fetch(DetectActivity act) {

        activity = new DetectActivity();

        activity = act;
    }


    @Override
    protected Void doInBackground(Void... urls) {
        try {

            SimpleLocation location = new SimpleLocation(activity);

            //checks if location is enabled. if not prompts user to enable it
            if(!location.hasLocationEnabled()) {
                Toast toast = Toast.makeText(activity, "Location not enabled. Please enable location to retrieve weather", Toast.LENGTH_SHORT);
                toast.show();

                SimpleLocation.openSettings(activity);

            }

            double latitude = location.getLatitude();
            double longitude = location.getLongitude();


            location.endUpdates();

            URL url = new URL("http://api.openweathermap.org/data/2.5/weather?lat=" + latitude + "&lon=" + longitude + "&appid=98ed13acba37b8f64a391fd3a3748865");


            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            try {
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder stringBuilder = new StringBuilder();
                String line = "";


                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                reader.close();
                JSONObject obj = null;

                try {
                    obj = new JSONObject(stringBuilder.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                JSONParser.Parser(obj);
                JSONParser.setActivity(activity);


                cancel(true);

                return null;


            } finally {
                if (connection != null) {
                    connection.disconnect();

                }
                cancel(true);

            }
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage());
            return null;
        }


    }

}


