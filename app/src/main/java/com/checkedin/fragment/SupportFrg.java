package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.SupportAdapter;

public class SupportFrg extends Fragment {

    private RecyclerView rvSupport;
    private SupportAdapter adptSupport;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_settings, container, false);
        initViews(view);

        rvSupport.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvSupport.setAdapter(adptSupport);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().trackScreenView(AnalyticsTrackers.ANALYTICS_PAGE_SUPPORT);
    }

    private void initViews(View view) {
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_support));

        rvSupport = (RecyclerView) view.findViewById(R.id.rv_settings);
        adptSupport = new SupportAdapter(getActivity(), this);
    }
}
