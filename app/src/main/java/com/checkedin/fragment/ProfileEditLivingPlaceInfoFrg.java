package com.checkedin.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.databinding.FrgLivingPlaceBinding;
import com.checkedin.model.LivingPlace;
import com.checkedin.model.UserDetail;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class ProfileEditLivingPlaceInfoFrg extends Fragment implements VolleyStringRequest.AfterResponse {
    private FrgLivingPlaceBinding mBinding;

    private WebServiceCall webServiceCall;
    private boolean isCurrentCity;

    private LivingPlace currentCity, homeTown;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_living_place, container, false);
        webServiceCall = new WebServiceCall(this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mBinding.setFragment(this);
        UserDetail userDetail = ((ProfileActivity) getActivity()).getUserDetail();

        try {
            for (LivingPlace livingPlace : userDetail.getLivingPlaceInfoList()) {
                if (livingPlace.getType().equals(LivingPlace.LIVING_PLACE_HOME)) {
                    homeTown = livingPlace.clone();
                    mBinding.etHomeTown.setText(homeTown.getFullAddress());
                } else {
                    currentCity = livingPlace.clone();
                    mBinding.etCurrentCity.setText(currentCity.getFullAddress());
                }
            }
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        if (currentCity == null) {
            currentCity = new LivingPlace();

        }
        if (homeTown == null) {
            homeTown = new LivingPlace();
        }


        currentCity.setType(LivingPlace.LIVING_PLACE_CURRENT);
        homeTown.setType(LivingPlace.LIVING_PLACE_HOME);

    }

    public void saveBtnClick(View v) {
        UserDetail userDetail = ((ProfileActivity) getActivity()).getUserDetail();
        ArrayList<LivingPlace> alLivingPlace = new ArrayList<>();
        alLivingPlace.add(currentCity);
        alLivingPlace.add(homeTown);
        userDetail.setLivingPlaceInfoList(alLivingPlace);

        webServiceCall.editLivingPlaceInfo(getContext(), UserPreferences.fetchUserId(getContext()), userDetail.getLivingPlaceInfo());
    }


    public void currentCityClick(View v) {
        isCurrentCity = true;
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
            startActivityForResult(intent, ProfileActivity.LIVING_PLACE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void homeTownClick(View v) {
        isCurrentCity = false;
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
            startActivityForResult(intent, ProfileActivity.LIVING_PLACE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProfileActivity.LIVING_PLACE) {
            if (resultCode == Activity.RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                LatLng latLng = place.getLatLng();

                if (isCurrentCity) {
                    mBinding.etCurrentCity.setText(place.getAddress());
                    currentCity.setFullAddress(place.getAddress().toString());
                    currentCity.setLatitude(String.valueOf(latLng.latitude));
                    currentCity.setLongitude(String.valueOf(latLng.longitude));
                } else {
                    mBinding.etHomeTown.setText(place.getAddress());
                    homeTown.setFullAddress(place.getAddress().toString());
                    homeTown.setLatitude(String.valueOf(latLng.latitude));
                    homeTown.setLongitude(String.valueOf(latLng.longitude));
                }


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(getContext(), data);
                Utility.logUtils(status.getStatusMessage());
            }
        }


    }

    @Override
    public void onResponseReceive(int requestCode) {
        UserDetailModel mUserDetail = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, UserDetailModel.class.getSimpleName());
        if (mUserDetail != null) {
            if (mUserDetail.getStatus() == BaseModel.STATUS_SUCCESS) {
                ((ProfileActivity) getActivity()).setUserDetail(mUserDetail.getUserDetails());
                UserPreferences.saveUserDetails(getContext(), mUserDetail.getUserDetails());
                getActivity().onBackPressed();
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mUserDetail.getMessage());
            }
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }
}
