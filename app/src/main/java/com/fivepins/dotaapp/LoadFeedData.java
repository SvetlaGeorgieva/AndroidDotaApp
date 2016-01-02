package com.fivepins.dotaapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by e30 on 1/2/2016.
 */
//public class LoadFeedData extends AsyncTask<Void, Void, ArrayList> {

public class LoadFeedData extends AsyncTask<String, Void, String> {

    private MatchAdapter mAdapter;

    public LoadFeedData(MatchAdapter adapter) {
        mAdapter = adapter;
    }

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        try {
            URL aURL = new URL(urls[0]);

            /* Open a connection to that URL. */
            final HttpURLConnection aHttpURLConnection = (HttpURLConnection) aURL.openConnection();

            /* Define InputStreams to read from the URLConnection. */
            InputStream aInputStream = aHttpURLConnection.getInputStream();

            BufferedReader r = new BufferedReader(new InputStreamReader(aInputStream));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            result = total.toString();

        } catch (IOException e) {
            Log.d("Data retrieve ERROR.", e.toString());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String response) {

        // try parse the string to a JSON object
        JSONObject jObj = null;
        ArrayList<Match> arrayOfMatches = new ArrayList<>();
        try {
            jObj = new JSONObject(response);
            JSONArray arrayResult = jObj.getJSONArray("result");

            for (int i = 0; i < arrayResult.length(); i++) {
                Match match = new Match(arrayResult.getJSONObject(i));
                arrayOfMatches.add(match);
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // Construct dummy data source
        Match match1 = new Match("Dummy_Navi", "Secret", 4, 10);
        Match match2 = new Match("Dummy_IG", "NIP", 8, 2);
        arrayOfMatches.add(match1);
        arrayOfMatches.add(match2);

        mAdapter.upDateEntries(arrayOfMatches);
        System.out.println("Async load data done");

    }
}