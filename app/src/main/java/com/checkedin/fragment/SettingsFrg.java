package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.SettingsAdapter;


public class SettingsFrg extends Fragment {

	private RecyclerView rvSettings;
	private SettingsAdapter adptSettings;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.frg_settings, container, false);
		initViews(view);

		rvSettings.setLayoutManager(new LinearLayoutManager(getActivity()));
		rvSettings.setAdapter(adptSettings);
		return view;
	}

	private void initViews(View view) {
		((MainActivity) getActivity()).toggleActionBarIcon(1, true);
		((MainActivity) getActivity()).showSearch(View.GONE);
		((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_setting));
		
		rvSettings = (RecyclerView) view.findViewById(R.id.rv_settings);
		adptSettings = new SettingsAdapter(getActivity(),this);
	}
}
