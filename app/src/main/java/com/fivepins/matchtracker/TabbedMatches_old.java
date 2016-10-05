package com.fivepins.matchtracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class TabbedMatches_old extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbed_matches);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        String dateBeforeYesterday = Utils.getDateDaysBack(2);
        String dateYesterday = Utils.getDateDaysBack(1);
        String dateToday = "today";

        tabLayout.addTab(tabLayout.newTab().setText(dateBeforeYesterday));
        tabLayout.addTab(tabLayout.newTab().setText(dateYesterday));
        tabLayout.addTab(tabLayout.newTab().setText(dateToday));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
//        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getContext());
        viewPager.setAdapter(adapter);

        // Load rightmost date first
        viewPager.setCurrentItem(2);
        tabLayout.getTabAt(2).select();

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_tabbed_matches, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.refresh_button:
                // Refresh the activity
                Intent tabbedMatchesIntent = new Intent(this, TabbedMatches_old.class);
                finish();
                startActivity(tabbedMatchesIntent);
                return true;
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
            case R.id.action_settings:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
