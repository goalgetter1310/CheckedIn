package com.checkedin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class FriendAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

    private Fragment[] fragment;

    public FriendAdapter(FragmentManager fm, Fragment[] fragment) {
        super(fm);
        this.fragment = fragment;
    }

    @Override
    public Fragment getItem(int position) {
        return fragment[position];
    }

    @Override
    public int getCount() {
        return fragment.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "FRIENDS";
        } else if(position==1){
            return "REQUEST";
        }
        else {
            return "SUGGESTED";
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        ((PageChange) fragment[position]).onPageChange();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public interface PageChange {
        void onPageChange();
    }

}
