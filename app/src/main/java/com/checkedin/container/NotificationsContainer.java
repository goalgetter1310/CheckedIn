package com.checkedin.container;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.fragment.HomeTabFrg;
import com.checkedin.fragment.NotificationListFrg;

@SuppressLint({"InflateParams", "ValidFragment"})
public class NotificationsContainer extends BaseContainerFragment {
    private boolean mIsViewInited;
    private Fragment fragment;

    public NotificationsContainer() {
    }

    public NotificationsContainer(Fragment fragment) {
        this.fragment = fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.container_fragment, null);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
        menu.findItem(R.id.itm_menu_search_place).setVisible(false);
        menu.findItem(R.id.itm_menu_post).setVisible(false);
        menu.findItem(R.id.itm_menu_server_filter).setVisible(false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!mIsViewInited) {
            mIsViewInited = true;
            initView();
        }
    }

    private void initView() {
        replaceFragment(new NotificationListFrg(), false);
        if (fragment != null) {
            directreplaceFragment(fragment, true);
        }
    }
}
