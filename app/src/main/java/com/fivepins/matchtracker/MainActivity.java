package com.fivepins.matchtracker;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
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


        // Check if the first row being shown matches the first top-most position, and then enable the SwipeRefreshLayout.
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int topRowVerticalPosition = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });


        // Mark that the activity is created with Intent action
        getIntent().setAction("Already created");
    }

    @Override
    protected void onResume() {
        String action = getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            System.out.println("Force restart activity from onResume");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else {
            getIntent().setAction(null);
        }

        super.onResume();
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
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.follow_teams:
                // Do something to choose teams to follow
                Intent followTeamsIntent = new Intent(this, FollowTeamsActivity.class);
                this.startActivity(followTeamsIntent);
                return true;
            case R.id.follow_tournaments:
                // Do something to choose tournaments to follow
                Intent followTournamentsIntent = new Intent(this, FollowTournamentsActivity.class);
                this.startActivity(followTournamentsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
