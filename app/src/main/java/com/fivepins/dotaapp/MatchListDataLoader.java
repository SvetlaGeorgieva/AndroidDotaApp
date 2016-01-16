package com.fivepins.dotaapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by e30 on 1/2/2016.
 * MatchListDataLoader uses AsyncTask to load the MatchList data from json URL.
 */
public class MatchListDataLoader extends AsyncTask<String, Void, String> {

    private MatchAdapter mAdapter;
    private ProgressDialog dialog;

    public MatchListDataLoader(MatchAdapter adapter, Context activityContext) {
        mAdapter = adapter;
        dialog = new ProgressDialog(activityContext);
    }

    protected void onPreExecute() {
        this.dialog.setMessage("Loading Matches...");
        this.dialog.show();
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
        ArrayList<Match> arrayOfMatches = new ArrayList<>();
        try {
            JSONObject jObj = new JSONObject(response);
            JSONArray arrayResult = jObj.getJSONArray("result");
            System.out.println("JSON data read.");

            for (int i = 0; i < arrayResult.length(); i++) {
                Match match = new Match(arrayResult.getJSONObject(i));
                arrayOfMatches.add(match);
            }
            System.out.println("Matches from JSON loaded.");
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        // Construct dummy data source
        Match match1 = new Match("Dummy_Navi", "Dummy_Secret", 4, 10, 1, "Dummy The International");
        Match match2 = new Match("Dummy_IG", "Dummy_NIP", 8, 2, 1, "Dummy The International");
        arrayOfMatches.add(match1);
        arrayOfMatches.add(match2);

        mAdapter.upDateEntries(arrayOfMatches);
        System.out.println("Async load data done");

        if (dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}