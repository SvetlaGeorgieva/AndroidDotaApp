package com.fivepins.matchtracker;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by e30 on 10/5/2016.
 */
public class MatchListFragment extends ListFragment {
    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;
    private SwipeRefreshLayout swipeContainer;
    private MatchAdapter matchAdapter;
    private String url;
    private ListView listView;
    private TextView emptyView;


    public static MatchListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        MatchListFragment fragment = new MatchListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.match_list_fragment, container, false);
        final Context context = getContext();

        String date = getArguments().getString("date");
        String baseUrl = getString(R.string.matches_json_base_url);
        String dateToday = Utils.getDateToday();

        //URL to get JSON Array
        this.url = baseUrl + date;

        // Create and set Adapter for the ListView.
        this.matchAdapter = new MatchAdapter(context);
        this.listView = (ListView) view.findViewById((android.R.id.list));
        this.emptyView = (TextView) view.findViewById(android.R.id.empty);

        if (dateToday.equals(date)) {
            emptyView.setText(R.string.no_live_matches);
        }
        this.emptyView.setVisibility(View.GONE);
        this.setListAdapter(this.matchAdapter);

        // Load data asynchronously.
        MatchListDataLoader matchListDataLoader = new MatchListDataLoader(this.matchAdapter, context, this.listView, this.emptyView);
        matchListDataLoader.execute(this.url);

        /*
         * Sets up a SwipeRefreshLayout.OnRefreshListener that is invoked when the user
         * performs a swipe-to-refresh gesture.
         */
        this.swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        this.swipeContainer.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        updateFragment();
                    }
                }
        );

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
                // TODO how to get the top-most position when the empty view is visible?
                swipeContainer.setEnabled(firstVisibleItem == 0 && topRowVerticalPosition >= 0);
            }
        });

        return view;
    }

    private void updateFragment() {
        // Load data asynchronously.
        MatchListDataLoader matchListDataLoader = new MatchListDataLoader(this.matchAdapter, getContext(), this.listView, this.emptyView);
        matchListDataLoader.execute(this.url);

        // Stop the refresh animation
        this.swipeContainer.setRefreshing(false);
    }

}
