package com.checkedin.container;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.fragment.InviteFriendFrg;
@SuppressLint({"InflateParams", "ValidFragment"})
public class InviteFriendContainer extends BaseContainerFragment {
	private boolean mIsViewInited;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		return inflater.inflate(R.layout.container_fragment, container, false);
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
		replaceFragment(new InviteFriendFrg(), false);
	}
}
