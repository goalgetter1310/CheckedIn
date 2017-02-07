package com.checkedin.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.checkedin.R;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.fragment.ActivityCategoryFrg;
import com.checkedin.fragment.ChatListFrg;
import com.checkedin.fragment.CheckinLocationFrg;
import com.checkedin.fragment.CheckinLoungeFrg;
import com.checkedin.fragment.CheckinPlacesFrg;
import com.checkedin.fragment.FriendsActivityFrg;
import com.checkedin.fragment.PlanningCategoryFrg;

public class HomeScreenAdapter extends FragmentPagerAdapter {
    public HomeScreenAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new FriendsActivityFrg();
            case 1: // Fragment # 1 - This will show image
                return new CheckinPlacesFrg();
//                return new ActivityCategoryFrg();

            case 2:
//                return new PlanningCategoryFrg();
                return new CheckinLoungeFrg();
            case 3:
                return new ActivityCategoryFrg();

            case 4:
                return new PlanningCategoryFrg();
        }

        return null;
    }


}