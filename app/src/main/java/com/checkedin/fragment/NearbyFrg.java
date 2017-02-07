package com.checkedin.fragment;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.CurrentLocation;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.NearByAdapter;
import com.checkedin.databinding.FrgCheckinLocListBinding;
import com.checkedin.model.Place;
import com.checkedin.model.response.GoogleApiPlaceModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;

@SuppressLint("ValidFragment")
public class NearbyFrg extends CheckinPlacesFrg.Fragment implements OnRefreshListener, VolleyStringRequest.AfterResponse {

    private NearByAdapter adptNearBy;
    private ArrayList<Place> alNearByPlace, alAllNearByPlace;
    private WebServiceCall webServiceCall;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading;

    private String pageToken;
    private Location currentLocation;
    private FrgCheckinLocListBinding mBinding;
    private boolean isSearching;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_checkin_loc_list, container, false);
        mLayoutManager = new LinearLayoutManager(getContext());
        webServiceCall = new WebServiceCall(this);
        alNearByPlace = new ArrayList<>();
        alAllNearByPlace = new ArrayList<>();
        adptNearBy = new NearByAdapter(getContext(), alNearByPlace);
        mLayoutManager = new LinearLayoutManager(getContext());
        currentLocation = new CurrentLocation(getContext()).getCurrentLocation();

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.rvSearchPlace.setLayoutManager(mLayoutManager);
        mBinding.rvSearchPlace.setAdapter(adptNearBy);
        mBinding.srlSearchPlace.setOnRefreshListener(this);
        mBinding.srlSearchPlace.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        mBinding.fabAdd.setVisibility(View.GONE);

        mBinding.rvSearchPlace.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && !isSearching && pageToken != null) {
                        isLoading = true;
                        webServiceCall.googlePlaceApiWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), pageToken, true);
                    }
                }
            }

        });
        mBinding.srlSearchPlace.setOnRefreshListener(this);
    }

    @Override
    public void cancelSearchPlace() {
        isSearching = false;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (alNearByPlace != null && alNearByPlace.size() <= 0) {
                if (currentLocation != null) {
                    isLoading = true;
                    mBinding.srlSearchPlace.setRefreshing(webServiceCall.googlePlaceApiWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), pageToken, false));
                } else {
                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.location_not_available));
                }
            }
        }
    }


    @Override
    public void onRefresh() {
        currentLocation = new CurrentLocation(getContext()).getCurrentLocation();
        if (currentLocation != null) {
            isLoading = true;
            pageToken = "";
            alNearByPlace.clear();
            alAllNearByPlace.clear();
            adptNearBy.notifyDataSetChanged();
            mBinding.srlSearchPlace.setRefreshing(webServiceCall.googlePlaceApiWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), pageToken, false));
        } else {
            mBinding.srlSearchPlace.setRefreshing(false);
            Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.location_not_available));
        }
    }

    @Override
    public void searchPlace(String query) {
        alNearByPlace.clear();
        query = query.trim();
        if (TextUtils.isEmpty(query)) {
            alNearByPlace.addAll(alAllNearByPlace);
        } else {
            for (int counter = 0; counter < alAllNearByPlace.size(); counter++) {
                if (alAllNearByPlace.get(counter).getName().toLowerCase(Locale.getDefault()).contains(query))
                    alNearByPlace.add(alAllNearByPlace.get(counter));
            }
        }
        adptNearBy.notifyDataSetChanged();
    }

    @Override
    public void onResponseReceive(int requestCode) {
        mBinding.srlSearchPlace.setRefreshing(false);
        isLoading = false;
        GoogleApiPlaceModel mGoogleApiPlace = (GoogleApiPlaceModel) webServiceCall.volleyRequestInstatnce().getModelObject(GoogleApiPlaceModel.class, GoogleApiPlaceModel.class.getSimpleName());
        if (mGoogleApiPlace != null && mGoogleApiPlace.getAlPlaceResult() != null) {
            for (int counter = 0; counter < mGoogleApiPlace.getAlPlaceResult().size(); counter++) {
                Place place = new Place();
                place.setLatitude(mGoogleApiPlace.getAlPlaceResult().get(counter).getGeometry().getLocation().getLat());
                place.setLongitude(mGoogleApiPlace.getAlPlaceResult().get(counter).getGeometry().getLocation().getLng());
                place.setName(mGoogleApiPlace.getAlPlaceResult().get(counter).getPlaceName());
                place.setDistance(currentLocation);
                alNearByPlace.add(place);
                alAllNearByPlace.add(place);
            }
            pageToken = mGoogleApiPlace.getNextPageToken();
            if (adptNearBy.getItemCount() == 0) {
                adptNearBy.notifyDataSetChanged();
            } else {
                int oldListSize = adptNearBy.getItemCount();
                adptNearBy.notifyItemRangeInserted(oldListSize, alNearByPlace.size() - oldListSize);
            }
        } else {
            Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        mBinding.srlSearchPlace.setRefreshing(false);
        isLoading = false;
        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }


}
