package com.fivepins.matchtracker;

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

/**
 * Created by e30 on 1/2/2016.
 * MatchListDataLoader uses AsyncTask to load the MatchList data from json URL.
 */
public class MatchListDataLoader extends AsyncTask<String, Void, String> {

    private MatchAdapter mAdapter;
    private ProgressDialog dialog;
    private View matchListView;
    private View emptyView;

    public MatchListDataLoader(MatchAdapter adapter, Context activityContext, View matchListView, View emptyView) {
        this.mAdapter = adapter;
        this.dialog = new ProgressDialog(activityContext);
        this.matchListView = matchListView;
        this.emptyView = emptyView;
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
            System.out.println("Matches from JSON loaded.");


        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

//        // Construct dummy data source
//        ArrayList<Player> radiantTeamPlayers = new ArrayList<>();
//        radiantTeamPlayers.add(new Player("DummY_1", "abaddon"));
//        radiantTeamPlayers.add(new Player("DummY_2", "antimage"));
//        radiantTeamPlayers.add(new Player("DummY_3", "axe"));
//        radiantTeamPlayers.add(new Player("DummY_4", "bane"));
//        radiantTeamPlayers.add(new Player("DummY_5", "bristleback"));
//        TeamScore radiantScore1 = new TeamScore(4, "000", "110", "100", "10", "11", "11", "111");
//        TeamScore radiantScore2 = new TeamScore(8, "110", "111", "111", "11", "11", "11", "111");
//
//        ArrayList<Player> direTeamPlayers = new ArrayList<>();
//        direTeamPlayers.add(new Player("DummY_1", "centaur"));
//        direTeamPlayers.add(new Player("DummY_2", "chen"));
//        direTeamPlayers.add(new Player("DummY_3", "furion"));
//        direTeamPlayers.add(new Player("DummY_4", "huskar"));
//        direTeamPlayers.add(new Player("DummY_5", "invoker"));
//        TeamScore direScore1 = new TeamScore(10, "111", "111", "111", "11", "11", "11", "111");
//        TeamScore direScore2 = new TeamScore(2, "110", "110", "110", "11", "11", "11", "111");
//
//        Match match1 = new Match("Dummy_Navi Super Duper Very Long Team Name", "Dummy_Secret", "Dummy The International", "0", "0", radiantTeamPlayers, direTeamPlayers, radiantScore1, direScore1, "10:15");
//        Match match2 = new Match("Dummy_IG", "Dummy_NIP", "Dummy Captains Draft 30 Presented by DotaCinema  MoonduckTV", "0", "0", radiantTeamPlayers, direTeamPlayers, radiantScore2, direScore2, "05:47");
//        arrayOfMatches.add(match1);
//        arrayOfMatches.add(match2);

        mAdapter.upDateEntries(arrayOfMatches);
        System.out.println("Async load data done");

        if (dialog.isShowing()) {
            dialog.dismiss();
        }

        if (arrayOfMatches.size() == 0) {
            this.matchListView.setVisibility(View.GONE);
            this.emptyView.setVisibility(View.VISIBLE);
        }
    }
}