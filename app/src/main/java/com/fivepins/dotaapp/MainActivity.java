package com.fivepins.dotaapp;

import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
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

    JSONArray allMatches = null;
    private SwipeRefreshLayout swipeContainer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Context context = this;

        //URL to get JSON Array
        //Contains an array of allMatches objects
        final String url = getString(R.string.matches_json_url);

        // TODO make this one method a
        // Construct the data source
        ArrayList<Match> arrayOfMatches = new ArrayList<>();

        // Create and set Adapter for the ListView.
        MatchAdapter adapter = new MatchAdapter(context, arrayOfMatches);
        ListView listView = (ListView) findViewById(R.id.match_list_view);
        listView.setAdapter(adapter);


        // Load data asynchronously.
        LoadFeedData loadFeedData = new LoadFeedData(adapter);
        loadFeedData.execute(url);


        // Swipe refresh part
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data asynchronously.
                // TODO make this one method a
                ArrayList<Match> arrayOfMatches2 = new ArrayList<>();
                MatchAdapter adapter = new MatchAdapter(context, arrayOfMatches2);
                LoadFeedData loadFeedData = new LoadFeedData(adapter);
                loadFeedData.execute(url);
                // Stop the refresh animation
                swipeContainer.setRefreshing(false);
            }
        });

        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
