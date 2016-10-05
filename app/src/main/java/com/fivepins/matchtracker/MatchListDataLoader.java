package com.fivepins.matchtracker;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

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
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by e30 on 1/2/2016.
 * MatchListDataLoader uses AsyncTask to load the MatchList data from json URL.
 */
public class MatchListDataLoader extends AsyncTask<String, Void, String> {

    private MatchAdapter mAdapter;
    private ProgressDialog dialog;
    private View matchListView;
    private View emptyView;
    private Context activityContext;

    public MatchListDataLoader(MatchAdapter adapter, Context activityContext, View matchListView, View emptyView) {
        this.mAdapter = adapter;
        this.dialog = new ProgressDialog(activityContext);
        this.matchListView = matchListView;
        this.emptyView = emptyView;
        this.activityContext = activityContext;
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

        //Check if the activity is not already destroyed (on change orientation for example)
        Activity activity = (Activity) activityContext;
        if (activity.isDestroyed()) {
            return;
        }

        this.emptyView.setVisibility(View.GONE);
        this.matchListView.setVisibility(View.VISIBLE);

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
            // Reverse arrayOfMatches to show the newest matches first.
            Collections.sort(arrayOfMatches);

            System.out.println("Matches from JSON loaded.");


        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

        mAdapter.upDateEntries(arrayOfMatches);
        System.out.println("Async load data done");

        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }

        if (arrayOfMatches.size() == 0) {
            this.matchListView.setVisibility(View.GONE);
            this.emptyView.setVisibility(View.VISIBLE);
        }
    }
}