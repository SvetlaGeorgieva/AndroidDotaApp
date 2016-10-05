package com.fivepins.matchtracker;

/**
 * Created by e30 on 9/9/2016.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MatchFragment extends Fragment{

    private SwipeRefreshLayout swipeContainer;
    public MatchAdapter matchAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.match_list_fragment, container, false);
        final Context context = getContext();

        String date = getArguments().getString("date");
        String baseUrl = getString(R.string.matches_json_base_url);
        String dateToday = Utils.getDateToday();

        //URL to get JSON Array
        //Contains an array of allMatches objects
        final String url = baseUrl + date;

        // Construct the data source
        ArrayList<Match> arrayOfMatches = new ArrayList<>();

        // Create and set Adapter for the ListView.
//        matchAdapter = new MatchAdapter(context, arrayOfMatches);
        matchAdapter = new MatchAdapter(context);

        final ListView listView = (ListView) fragmentView.findViewById((R.id.match_list_view));
        final View emptyView = fragmentView.findViewById(R.id.empty_match_list);
        final TextView emptyTextView = (TextView) fragmentView.findViewById(R.id.empty_match_list_text);

        if (dateToday.equals(date)) {
            emptyTextView.setText(R.string.no_live_matches);
        }

        emptyView.setVisibility(View.GONE);
        listView.setAdapter(matchAdapter);

        // Load data asynchronously.
        MatchListDataLoader matchListDataLoader = new MatchListDataLoader(matchAdapter, context, listView, emptyView);
        matchListDataLoader.execute(url);

        // Swipe refresh part
        swipeContainer = (SwipeRefreshLayout) fragmentView.findViewById(R.id.swipeContainer);

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Load data asynchronously.
                MatchListDataLoader matchListDataLoader = new MatchListDataLoader(MatchFragment.this.matchAdapter, context, listView, emptyView);
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
        getActivity().getIntent().setAction("Already created");

        return fragmentView;

    }

    // TODO Swipe view refresh not being triggered.
    @Override
    public void onResume() {
        String action = getActivity().getIntent().getAction();
        // Prevent endless loop by adding a unique action, don't restart if action is present
        if(action == null || !action.equals("Already created")) {
            System.out.println("Force restart activity from onResume");
            Intent intent = new Intent(getActivity(), MatchFragment.class);
            startActivity(intent);
            getActivity().finish();
        }
        // Remove the unique action so the next time onResume is called it will restart
        else {
            System.out.println("From onResume else");
            getActivity().getIntent().setAction(null);
        }

        super.onResume();
    }
}
