package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.ActionEvent;
import com.checkedin.CurrentLocation;
import com.checkedin.ManageActionEvent;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.PersonalLocAdapter;
import com.checkedin.databinding.FrgCheckinLocListBinding;
import com.checkedin.dialog.AddPlaceDialog;
import com.checkedin.model.Place;
import com.checkedin.model.response.AddPlaceRes;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.EditPlaceRes;
import com.checkedin.model.response.SuggestedPlaceModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;

public class PersonalLocationFrg extends CheckinPlacesFrg.Fragment implements OnRefreshListener, VolleyStringRequest.AfterResponse, View.OnClickListener, ActionEvent {
    private PersonalLocAdapter adptPersonal;
    private ArrayList<Place> alPersonalPlace, alAllPersonalPlace;
    private WebServiceCall webServiceCall;
    private LinearLayoutManager mLayoutManager;

    private Location currentLocation;
    private FrgCheckinLocListBinding mBinding;
    private ManageActionEvent manageActionEvent;
    private int actionSelectedPosition;
    private Place place;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_checkin_loc_list, container, false);
        mLayoutManager = new LinearLayoutManager(getContext());
        webServiceCall = new WebServiceCall(this);
        alPersonalPlace = new ArrayList<>();
        alAllPersonalPlace = new ArrayList<>();
        adptPersonal = new PersonalLocAdapter(getContext(), alPersonalPlace, this);
        mLayoutManager = new LinearLayoutManager(getContext());
        currentLocation = new CurrentLocation(getContext()).getCurrentLocation();
        manageActionEvent = new ManageActionEvent(getActivity(), this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.rvSearchPlace.setLayoutManager(mLayoutManager);
        mBinding.rvSearchPlace.setAdapter(adptPersonal);
        mBinding.srlSearchPlace.setOnRefreshListener(this);
        mBinding.srlSearchPlace.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        mBinding.fabAdd.setVisibility(View.VISIBLE);

        mBinding.fabAdd.setOnClickListener(this);
        mBinding.srlSearchPlace.setOnRefreshListener(this);
    }

    @Override
    public void cancelSearchPlace() {
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (alPersonalPlace != null && alPersonalPlace.size() <= 0) {
                if (currentLocation != null) {
                    mBinding.srlSearchPlace.setRefreshing(webServiceCall.personalPlaceWsCall(getContext(), false));
                } else {
                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.location_not_available));
                }
            }
        }
    }


    public void showActionEventMenu(boolean isActionEventMenu, int position) {
        this.actionSelectedPosition = position;
        manageActionEvent.showActionEventMenu(isActionEventMenu);
    }


    @Override
    public void onClick(View v) {
        ((CheckinPlacesFrg) getParentFragment()).closeSearch();
        new AddPlaceDialog(getContext(), R.string.cant_find_place, new AddPlaceDialog.AddPlace() {
            @Override
            public void onPlaceAdded(String placeName) {
                try {
                    place = new Place();
                    place.setName(placeName);
                    place.setLatitude(currentLocation.getLatitude());
                    place.setLongitude(currentLocation.getLongitude());
                    webServiceCall.addLocationWsCall(getContext(), String.valueOf(currentLocation.getLatitude()), String.valueOf(currentLocation.getLongitude()), placeName, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).show();
    }

    @Override
    public void onEdit() {
        adptPersonal.closeActionEventView();
        new AddPlaceDialog(getContext(), R.string.cant_find_place, alPersonalPlace.get(actionSelectedPosition).getName(), new AddPlaceDialog.AddPlace() {
            @Override
            public void onPlaceAdded(String placeName) {
                Place placeEdit = alPersonalPlace.get(actionSelectedPosition);
                placeEdit.setName(placeName);
                adptPersonal.notifyItemChanged(actionSelectedPosition);
                webServiceCall.editPlaceWsCall(getContext(), String.valueOf(placeEdit.getId()), placeName);
            }
        }).show();

    }

    @Override
    public void onDelete() {
        adptPersonal.closeActionEventView();
        webServiceCall.deletePlaceWSCall(getContext(), String.valueOf(alPersonalPlace.get(actionSelectedPosition).getId()));
        alPersonalPlace.remove(actionSelectedPosition);
        adptPersonal.notifyItemRemoved(actionSelectedPosition);
        adptPersonal.notifyItemRangeChanged(actionSelectedPosition, adptPersonal.getItemCount());
    }

    @Override
    public void onBack() {
        adptPersonal.closeActionEventView();
    }


    @Override
    public void onRefresh() {
        alPersonalPlace.clear();
        alAllPersonalPlace.clear();
        mBinding.srlSearchPlace.setRefreshing(webServiceCall.personalPlaceWsCall(getContext(), false));
    }

    @Override
    public void searchPlace(String query) {
        alPersonalPlace.clear();
        query = query.trim();
        if (TextUtils.isEmpty(query)) {
            alPersonalPlace.addAll(alAllPersonalPlace);
        } else {
            for (int counter = 0; counter < alAllPersonalPlace.size(); counter++) {
                if (alAllPersonalPlace.get(counter).getName().toLowerCase(Locale.getDefault()).contains(query))
                    alPersonalPlace.add(alAllPersonalPlace.get(counter));
            }
        }
        adptPersonal.notifyDataSetChanged();
    }


    @Override
    public void onResponseReceive(int requestCode) {
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_PERSONAL_LOCATION_LIST:
                mBinding.srlSearchPlace.setRefreshing(false);
                SuggestedPlaceModel mPersonalPlace = (SuggestedPlaceModel) webServiceCall.volleyRequestInstatnce().getModelObject(SuggestedPlaceModel.class, SuggestedPlaceModel.class.getSimpleName());
                if (mPersonalPlace != null) {
                    if (mPersonalPlace.getStatus() == BaseModel.STATUS_SUCCESS) {
                        for (int counter = 0; counter < mPersonalPlace.getCheckinLocationList().size(); counter++) {
                            Place place = new Place();
                            place.setId(mPersonalPlace.getCheckinLocationList().get(counter).getId());
                            place.setLatitude(mPersonalPlace.getCheckinLocationList().get(counter).getLatitude());
                            place.setLongitude(mPersonalPlace.getCheckinLocationList().get(counter).getLongitude());
                            place.setName(mPersonalPlace.getCheckinLocationList().get(counter).getName());
                            place.setDistance(currentLocation);
                            alPersonalPlace.add(place);
                            alAllPersonalPlace.add(place);
                        }
                        adptPersonal.notifyDataSetChanged();
                    } else {
                        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), mPersonalPlace.getMessage());
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
                        alPersonalPlace.add(place);
                        adptPersonal.notifyItemChanged(actionSelectedPosition);
                    } else {
                        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), addPlaceRes.getMessage());
                    }

                } else {

                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;

            case WebServiceCall.REQUEST_CODE_EDIT_PLACE:
                EditPlaceRes editPlaceRes = (EditPlaceRes) webServiceCall.volleyRequestInstatnce().getModelObject(EditPlaceRes.class, EditPlaceRes.class.getSimpleName());
                if (editPlaceRes != null) {
                    if (editPlaceRes.getStatus() == BaseModel.STATUS_SUCCESS) {
                        alPersonalPlace.set(actionSelectedPosition, editPlaceRes.getPlace());
                        adptPersonal.notifyItemChanged(actionSelectedPosition);
                    } else {
                        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), editPlaceRes.getMessage());
                    }

                } else {

                    Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;

            case WebServiceCall.REQUEST_CODE_DELETE_PLACE:
                BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (baseModel != null) {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), baseModel.getMessage());
                } else {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;
        }
    }

    @Override
    public void onErrorReceive() {
        mBinding.srlSearchPlace.setRefreshing(false);
        Utility.showSnackBar(((MainActivity) getContext()).findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }

    @Override
    public void onDestroyView() {
        manageActionEvent.closeActionView();
        super.onDestroyView();
    }
}
