package com.fivepins.matchtracker;

/**
 * Pager adapter to display 3 tabs with Matches. The right-most tab is for the current day.
 * The other 2 tabs are for the 2 previous days.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class PagerAdapter extends FragmentPagerAdapter {
    final int mNumOfTabs = 3;
    ArrayList<String> tabTitles = new ArrayList<>();

    public PagerAdapter(FragmentManager fm) {
        super(fm);
        setTabTitles();
    }

    @Override
    public int getCount() {
        return this.mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        MatchListFragment matchListFragment = MatchListFragment.newInstance(position + 1);

        // Link the date of the matches displayed in the fragment
        Bundle bundle = new Bundle();
        String date = getPageTitle(position);
        bundle.putString("date", date);
        matchListFragment.setArguments(bundle);

        return matchListFragment;
    }

    @Override
    public String getPageTitle(int position) {
        // Generate title based on item position
        return this.tabTitles.get(position);
    }

    private void setTabTitles() {
        String dateBeforeYesterday = Utils.getDateDaysBack(2);
        String dateYesterday = Utils.getDateDaysBack(1);
        String dateToday = Utils.getDateToday();

        this.tabTitles.add(dateBeforeYesterday);
        this.tabTitles.add(dateYesterday);
        this.tabTitles.add(dateToday);
    }

}
