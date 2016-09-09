package com.fivepins.matchtracker;

/**
 * Created by e30 on 9/9/2016.
 */
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

        switch (position) {
            case 0:
                MatchFragment1 tab1 = new MatchFragment1();
                return tab1;
            case 1:
                MatchFragment2 tab2 = new MatchFragment2();
                return tab2;
            case 2:
                MatchFragment3 tab3 = new MatchFragment3();
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
