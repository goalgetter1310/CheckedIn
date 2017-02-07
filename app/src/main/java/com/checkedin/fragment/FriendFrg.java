package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.adapter.FriendAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.FrgFriendBinding;
import com.checkedin.utility.Utility;
public class FriendFrg extends Fragment {

    private View view;
    private FrgFriendBinding mBinding;

    public static boolean isFriendRequest = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            mBinding = DataBindingUtil.inflate(getLayoutInflater(savedInstanceState), R.layout.frg_friend, container, false);
            view = mBinding.getRoot();


            Fragment fragment[] = new Fragment[3];
            fragment[0] = new FriendListFrg();
            fragment[1] = new FriendRequestFrg();
            fragment[2] = new FriendSuggestionFrg();
            FriendAdapter adptFriend = new FriendAdapter(getChildFragmentManager(), fragment);

            mBinding.vpFriend.addOnPageChangeListener(adptFriend);
            mBinding.vpFriend.setAdapter(adptFriend);
            mBinding.tlFriend.setupWithViewPager(mBinding.vpFriend);
            mBinding.tlFriend.setTabsFromPagerAdapter(adptFriend);
            mBinding.vpFriend.setOffscreenPageLimit(adptFriend.getCount());

            mBinding.ivBack.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                        Utility.doubleTapTime = System.currentTimeMillis();
                        ((DialogFragmentContainer) getParentFragment()).popFragment();
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (isFriendRequest) {
            mBinding.vpFriend.setCurrentItem(1);
            isFriendRequest = false;
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void setFriendListTitle(String title, int position) {
        if (mBinding != null)
            mBinding.tlFriend.getTabAt(position).setText(title);
    }

}
