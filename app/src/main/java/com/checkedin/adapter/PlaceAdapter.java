package com.checkedin.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;

import com.checkedin.fragment.CheckinPlacesFrg;
import com.checkedin.fragment.CheckinPlacesFrg.Fragment;


public class PlaceAdapter extends FragmentStatePagerAdapter implements OnPageChangeListener {
    private int currentPosition;
    private Fragment[] fragments;

    public PlaceAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position) {
        String title = " ";
        switch (position) {
            case 0:
                title = "CheckIn";
                break;
            case 1:
                title = "Near By";
                break;
            case 2:
                title = "Personal";
                break;

        }
        return title;
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    public void searchPlace(String query) {
        fragments[currentPosition].searchPlace(query);
    }

    public void cancelSearchPlace() {
        fragments[currentPosition].cancelSearchPlace();
    }

    @Override
    public void onPageSelected(int position) {
        currentPosition = position;
        ((CheckinPlacesFrg) fragments[currentPosition].getParentFragment()).closeSearch();
    }
}
