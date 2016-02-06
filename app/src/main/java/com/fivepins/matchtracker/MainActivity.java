package com.fivepins.matchtracker;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SwipeRefreshLayout swipeContainer;
    public MatchAdapter matchAdapter;


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

        // Construct the data source
        ArrayList<Match> arrayOfMatches = new ArrayList<>();

        // Create and set Adapter for the ListView.
        matchAdapter = new MatchAdapter(context, arrayOfMatches);
        final ListView listView = (ListView) findViewById((R.id.match_list_view));
        final View emptyView = findViewById(R.id.empty_match_list);
        emptyView.setVisibility(View.GONE);
        listView.setAdapter(matchAdapter);


        // Load data asynchronously.
        MatchListDataLoader matchListDataLoader = new MatchListDataLoader(matchAdapter, context, listView, emptyView);
        matchListDataLoader.execute(url);


        // Swipe refresh part
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data asynchronously.
                MatchListDataLoader matchListDataLoader = new MatchListDataLoader(MainActivity.this.matchAdapter, context, listView, emptyView);
                matchListDataLoader.execute(url);

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
