package com.fivepins.dotaapp;

import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

//    //URL request params
//    HashMap<String, String> params = new HashMap<>();
//
//    //JSON Node Names
//    private static final String TAG_MATCHES = "result";
//    private static final String TAG_RADIANT_TEAM = "radiant_team";
//    private static final String TAG_DIRE_TEAM = "dire_team";
//    private static final String TAG_TEAM_NAME = "team_name";
//    private static final String TAG_SCOREBOARD = "scoreboard";
//    private static final String TAG_RADIANT_STATS = "radiant";
//    private static final String TAG_DIRE_STATS = "dire";
//    private static final String TAG_SCORE = "score";

    JSONArray allMatches = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (android.os.Build.VERSION.SDK_INT > 9) {
//            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//            StrictMode.setThreadPolicy(policy);
//        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Construct the data source
        ArrayList<Match> arrayOfMatches = new ArrayList<>();
        Match match1 = new Match("Dummy_Navi", "Secret", 4, 10);
        Match match2 = new Match("Dummy_IG", "NIP", 8, 2);
        Match match3 = new Match("Dummy_EG", "Secret", 6, 12);
        Match match4 = new Match("Dummy_Navi", "EG", 17, 2);
        arrayOfMatches.add(match1);
        arrayOfMatches.add(match2);
        arrayOfMatches.add(match3);
        arrayOfMatches.add(match4);

        //URL to get JSON Array
        //Contains an array of allMatches objects
        String url = getString(R.string.matches_json_url);

//        // Creating new JSON Parser
//        JSONParser jParser = new JSONParser();
//
//
//        // Getting JSON from URL
//        JSONObject json = jParser.makeHttpRequest(url, "GET", params);
//
//
//        try {
//            // This will get the array of matches
//            allMatches = json.getJSONArray(TAG_MATCHES);
//
//            if (allMatches.length() != 0) {
//                JSONObject firstMatch = allMatches.getJSONObject(0);
//
//                // Storing  JSON item in a Variable
//                JSONObject radiantTeamJSON = firstMatch.getJSONObject(TAG_RADIANT_TEAM);
//                String radiantTeamName = radiantTeamJSON.getString(TAG_TEAM_NAME);
//
//                JSONObject direTeamJSON = firstMatch.getJSONObject(TAG_DIRE_TEAM);
//                String direTeamName = direTeamJSON.getString(TAG_TEAM_NAME);
//
//                JSONObject scoreboard = firstMatch.getJSONObject(TAG_SCOREBOARD);
//                JSONObject radiantTeamStats = scoreboard.getJSONObject(TAG_RADIANT_STATS);
//                JSONObject direTeamStats = scoreboard.getJSONObject(TAG_DIRE_STATS);
//                int radiantTeamKills = Integer.parseInt(radiantTeamStats.getString(TAG_SCORE));
//                int direTeamKills = Integer.parseInt(direTeamStats.getString(TAG_SCORE));
//
//
//                //Add data to the adapter
//                Match matchActual = new Match(radiantTeamName, direTeamName, radiantTeamKills, direTeamKills);
//                arrayOfMatches.add(matchActual);
//            } else {
//                Toast.makeText(MainActivity.this, "No Live PRO Matches",
//                        Toast.LENGTH_LONG).show();
//                System.out.println("No matches");
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }


        // Create and set Adapter for the ListView.
        MatchAdapter adapter = new MatchAdapter(this, arrayOfMatches);
        ListView listView = (ListView) findViewById(R.id.match_list_view);
        listView.setAdapter(adapter);


        // Load data asynchronously.
        LoadFeedData loadFeedData = new LoadFeedData(adapter);
        loadFeedData.execute(url);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
