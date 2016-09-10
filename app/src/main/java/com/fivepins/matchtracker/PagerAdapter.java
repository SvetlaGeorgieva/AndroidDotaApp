package com.fivepins.matchtracker;

/**
 * Created by e30 on 9/9/2016.
 */
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        Bundle bundleDay1 = new Bundle();
        Bundle bundleDay2 = new Bundle();
        Bundle bundleDay3 = new Bundle();
        bundleDay1.putString("date", "2016-08-12");
        bundleDay2.putString("date", "2016-08-13");
        bundleDay3.putString("date", "2016-08-14");

        switch (position) {
            case 0:
                MatchFragment tab1 = new MatchFragment();
                tab1.setArguments(bundleDay1);
                return tab1;
            case 1:
                MatchFragment tab2 = new MatchFragment();
                tab2.setArguments(bundleDay2);
                return tab2;
            case 2:
                MatchFragment tab3 = new MatchFragment();
                tab3.setArguments(bundleDay3);
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
