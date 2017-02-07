package com.checkedin.fragment;

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
import com.checkedin.adapter.CheckinLocAdapter;
import com.checkedin.databinding.FrgCheckinLocListBinding;
import com.checkedin.dialog.AddPlaceDialog;
import com.checkedin.model.Place;
import com.checkedin.model.response.AddPlaceRes;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.GoogleApiPlaceModel;
import com.checkedin.model.response.SuggestedPlaceModel;
import com.checkedin.utility.Utility;
import com.checkedin.views.StickyHeaderDecoration;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;

public class CheckinLocationFrg extends CheckinPlacesFrg.Fragment implements OnRefreshListener, VolleyStringRequest.AfterResponse, View.OnClickListener {
    private CheckinLocAdapter adptCheckinLoc;
    private ArrayList<Place> alCheckinPlaces, alAllCheckinPlaces;

    private WebServiceCall webServiceCall;

    private LinearLayoutManager mLayoutManager;
    private Location currentLocation;
    private FrgCheckinLocListBinding mBinding;
    private boolean isLoading;
    private boolean isSearching;
    private String pageToken;
    private int suggestedPlaceCount;
    private StickyHeaderDecoration decor;
    private Place place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_checkin_loc_list, container, false);
        mLayoutManager = new LinearLayoutManager(getContext());
        webServiceCall = new WebServiceCall(this);
        currentLocation = new CurrentLocation(getContext()).getCurrentLocation();
        alCheckinPlaces = new ArrayList<>();
        alAllCheckinPlaces = new ArrayList<>();
        adptCheckinLoc = new CheckinLocAdapter(getContext(), alCheckinPlaces, (CheckinPlacesFrg) getParentFragment());
        decor = new StickyHeaderDecoration(adptCheckinLoc);


        return mBinding.getRoot();
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.rvSearchPlace.setLayoutManager(mLayoutManager);
        mBinding.rvSearchPlace.setAdapter(adptCheckinLoc);
        mBinding.rvSearchPlace.addItemDecoration(decor, 0);
        mBinding.srlSearchPlace.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        mBinding.fabAdd.setVisibility(View.VISIBLE);
        if (currentLocation != null) {
            isLoading = true;
            mBinding.srlSearchPlace.post(new Runnable() {
                @Override
                public void run() {
                    if (webServiceCall.checkinPlaceWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), false)) {
                        mBinding.srlSearchPlace.setRefreshing(true);
                    } else {
                        mBinding.srlSearchPlace.setRefreshing(false);

                    }

                }
            });

        }

        mBinding.rvSearchPlace.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading && (visibleItemCount + pastVisiblesItems) >= totalItemCount - suggestedPlaceCount && !isSearching && pageToken != null) {
                    isLoading = true;
                    webServiceCall.googlePlaceApiWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), pageToken, true);
                }
            }

        });
        mBinding.fabAdd.setOnClickListener(this);
        mBinding.srlSearchPlace.setOnRefreshListener(this);
    }

    @Override
    public void onRefresh() {
        currentLocation = new CurrentLocation(getContext()).getCurrentLocation();
        if (currentLocation != null) {
            isLoading = true;
            alCheckinPlaces.clear();
            alAllCheckinPlaces.clear();
            pageToken = "";
            decor.clearHeaderCache();
            mBinding.srlSearchPlace.setRefreshing(webServiceCall.checkinPlaceWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), false));
            adptCheckinLoc.notifyDataSetChanged();
        } else {
            mBinding.srlSearchPlace.setRefreshing(false);
            Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.location_not_available));
        }
    }

    @Override
    public void onResponseReceive(int requestCode) {
        mBinding.srlSearchPlace.setRefreshing(false);
        isLoading = false;
        switch (requestCode) {

            case WebServiceCall.REQUEST_CODE_CHECKED_LOCATION_LIST:
                SuggestedPlaceModel mSugPlace = (SuggestedPlaceModel) webServiceCall.volleyRequestInstatnce().getModelObject(SuggestedPlaceModel.class, "PlaceModel");

                if (mSugPlace != null) {
                    if (mSugPlace.getStatus() == BaseModel.STATUS_SUCCESS) {
                        suggestedPlaceCount = mSugPlace.getCheckinLocationList().size();

                        for (int counter = 0; counter < mSugPlace.getCheckinLocationList().size(); counter++) {
                            Place place = new Place();
                            place.setId(mSugPlace.getCheckinLocationList().get(counter).getId());
                            place.setLatitude(mSugPlace.getCheckinLocationList().get(counter).getLatitude());
                            place.setLongitude(mSugPlace.getCheckinLocationList().get(counter).getLongitude());
                            place.setName(mSugPlace.getCheckinLocationList().get(counter).getName());
                            place.setDistance(currentLocation);
                            alCheckinPlaces.add(place);
                            alAllCheckinPlaces.add(place);
                        }
                        if (adptCheckinLoc.getItemCount() == 0) {
                            adptCheckinLoc.notifyDataSetChanged();
                        } else {
                            int oldListSize = adptCheckinLoc.getItemCount();
                            adptCheckinLoc.notifyItemRangeInserted(oldListSize, alCheckinPlaces.size() - oldListSize);
                        }
                    } else {
                        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), mSugPlace.getMessage());
                    }

                } else {
                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                isLoading = true;
                webServiceCall.googlePlaceApiWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), pageToken, true);

                break;
            case WebServiceCall.REQUEST_CODE_GOOGLE_PLACE_API:
                GoogleApiPlaceModel mGoogleApiPlace = (GoogleApiPlaceModel) webServiceCall.volleyRequestInstatnce().getModelObject(GoogleApiPlaceModel.class, GoogleApiPlaceModel.class.getSimpleName());
                if (mGoogleApiPlace != null && mGoogleApiPlace.getAlPlaceResult() != null) {
                    for (int counter = 0; counter < mGoogleApiPlace.getAlPlaceResult().size(); counter++) {
                        Place place = new Place();
                        place.setLatitude(mGoogleApiPlace.getAlPlaceResult().get(counter).getGeometry().getLocation().getLat());
                        place.setLongitude(mGoogleApiPlace.getAlPlaceResult().get(counter).getGeometry().getLocation().getLng());
                        place.setName(mGoogleApiPlace.getAlPlaceResult().get(counter).getPlaceName());
                        place.setDistance(currentLocation);
                        place.setGooglePlace(true);
                        alCheckinPlaces.add(place);
                        alAllCheckinPlaces.add(place);
                    }
                    pageToken = mGoogleApiPlace.getNextPageToken();
                    if (adptCheckinLoc.getItemCount() == 0) {
                        adptCheckinLoc.notifyDataSetChanged();
                    } else {
                        adptCheckinLoc.notifyItemInserted(adptCheckinLoc.getItemCount());
                    }
                } else {
                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;
            case WebServiceCall.REQUEST_CODE_ADD_LOCATION:
                AddPlaceRes addPlaceRes = (AddPlaceRes) webServiceCall.volleyRequestInstatnce().getModelObject(AddPlaceRes.class, AddPlaceRes.class.getSimpleName());
                if (addPlaceRes != null) {
                    if (addPlaceRes.getStatus() == BaseModel.STATUS_SUCCESS) {
                        place.setId(addPlaceRes.getPlaceRes().getPlaceId());
                        if (suggestedPlaceCount != adptCheckinLoc.getItemCount()) {
                            alCheckinPlaces.add(suggestedPlaceCount, place);
                            alAllCheckinPlaces.add(suggestedPlaceCount, place);
                            adptCheckinLoc.notifyItemChanged(suggestedPlaceCount++);
                        }
                    } else {
                        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), addPlaceRes.getMessage());
                    }

                } else {

                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;
        }

    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        mBinding.srlSearchPlace.setRefreshing(false);
        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }


    @Override
    public void searchPlace(String query) {

        alCheckinPlaces.clear();
        query = query.trim();
        if (TextUtils.isEmpty(query)) {
            alCheckinPlaces.addAll(alAllCheckinPlaces);
        } else {
            isSearching = true;
            alCheckinPlaces.clear();
            for (int counter = 0; counter < alAllCheckinPlaces.size(); counter++) {
                if (alAllCheckinPlaces.get(counter).getName().toLowerCase(Locale.getDefault()).contains(query))
                    alCheckinPlaces.add(alAllCheckinPlaces.get(counter));
            }
        }
        adptCheckinLoc.notifyDataSetChanged();
    }

    @Override
    public void cancelSearchPlace() {
        isSearching = false;
    }


    @Override
    public void onClick(View v) {
        ((CheckinPlacesFrg) getParentFragment()).closeSearch();

        new AddPlaceDialog(getContext(), R.string.cant_find_place, new AddPlaceDialog.AddPlace() {
            @Override
            public void onPlaceAdded(String placeName) {
                if (currentLocation != null) {
                    place = new Place();
                    place.setName(placeName);
                    place.setLatitude(currentLocation.getLatitude());
                    place.setLongitude(currentLocation.getLongitude());
                    webServiceCall.addLocationWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), placeName, false);
                } else {
                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.no_location));
                }
            }
        }).show();

    }
}
