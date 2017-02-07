package com.checkedin.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.PlaceAdapter;
import com.checkedin.databinding.FrgCheckinPlacesBinding;
import com.checkedin.utility.Utility;

import java.util.Locale;

public class CheckinPlacesFrg extends Fragment implements OnQueryTextListener {

    private PlaceAdapter adptPlace;

    private SearchView searchView;
    private FrgCheckinPlacesBinding mBinding;

//    private ArrayList<Place> alGooglePlaces;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_checkin_places, container, false);
        setHasOptionsMenu(true);

//        alGooglePlaces = new ArrayList<>();
//        Utility.checkLocationSetting(getActivity());
        Fragment[] fragment;
        if (getChildFragmentManager().getFragments() == null) {
            fragment = new Fragment[3];
            fragment[0] = new CheckinLocationFrg();
            fragment[1] = new NearbyFrg();
            fragment[2] = new PersonalLocationFrg();
        } else {
            fragment = new Fragment[getChildFragmentManager().getFragments().size()];
            for (int counter = 0; counter < getChildFragmentManager().getFragments().size(); counter++) {
                android.support.v4.app.Fragment frg = getChildFragmentManager().getFragments().get(counter);
                if (frg instanceof CheckinLocationFrg) {
                    fragment[0] = (CheckinLocationFrg) getChildFragmentManager().getFragments().get(counter);
                } else if (frg instanceof NearbyFrg) {
                    fragment[1] = (NearbyFrg) getChildFragmentManager().getFragments().get(counter);
                } else if (frg instanceof PersonalLocationFrg) {
                    fragment[2] = (PersonalLocationFrg) getChildFragmentManager().getFragments().get(counter);
                }
            }
        }

        adptPlace = new PlaceAdapter(getChildFragmentManager(), fragment);

        return mBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().trackScreenView(AnalyticsTrackers.ANALYTICS_PAGE_LOCATION);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_search));
            ((MainActivity) getActivity()).ivChat.setVisibility(View.INVISIBLE);
            Utility.checkLocationSetting(getActivity());
            ((MainActivity) getActivity()).showSearch(View.GONE);

        }
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
//        ((MainActivity) getActivity()).showSearch(View.GONE);
//        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_search));


        mBinding.vpSearchPlace.setOffscreenPageLimit(3);
        mBinding.vpSearchPlace.setAdapter(adptPlace);
        mBinding.tlSearchPlace.setupWithViewPager(mBinding.vpSearchPlace);
        mBinding.vpSearchPlace.addOnPageChangeListener(adptPlace);
//        mBinding.tlSearchPlace.setTabsFromPagerAdapter(adptPlace);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
        MenuItem item = menu.findItem(R.id.itm_menu_search_place);

        item.setVisible(true);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        setSearchView(searchView);
        this.searchView = searchView;
    }

    private void setSearchView(final SearchView searchView) {

        SearchView.SearchAutoComplete searchAutoComplete = (SearchView.SearchAutoComplete) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        searchAutoComplete.setHint(R.string.search_place);
        searchAutoComplete.setHintTextColor(Color.WHITE);
        searchAutoComplete.setTextColor(Color.WHITE);

        ImageView searchCloseIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setColorFilter(Color.WHITE);
        searchCloseIcon.setImageResource(R.drawable.ic_cross_grey_16dp);
        ImageView searchIcon = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setImageResource(R.drawable.ic_search_white_16dp);

        searchCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchView.onActionViewCollapsed();
                adptPlace.cancelSearchPlace();
            }
        });
        searchView.setOnQueryTextListener(this);
    }

    @Override
    public boolean onQueryTextChange(String query) {
        adptPlace.searchPlace(query.toLowerCase(Locale.getDefault()));
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        adptPlace.searchPlace(query.toLowerCase(Locale.getDefault()));
        return true;
    }

    public void closeSearch() {
        if (searchView != null)
            searchView.onActionViewCollapsed();
    }

    public void onResumeCustom() {
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_search));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeSearch();
    }


    public abstract static class Fragment extends android.support.v4.app.Fragment {
        public abstract void searchPlace(String query);

        public abstract void cancelSearchPlace();
    }

}
