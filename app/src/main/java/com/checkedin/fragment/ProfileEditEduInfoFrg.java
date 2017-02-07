package com.checkedin.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.databinding.FrgEduInfoBinding;
import com.checkedin.model.EducationInfo;
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
import com.material.app.DatePickerDialog;
import com.material.app.DialogFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
public class ProfileEditEduInfoFrg extends Fragment implements VolleyStringRequest.AfterResponse {

    private FrgEduInfoBinding mBinding;
    private EducationInfo educationInfo;
    private WebServiceCall webServiceCall;


    private boolean isGraduate;
    private boolean isFromDate;
    private DatePicker datePicker;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_edu_info, container, false);
        datePicker = new DatePicker();
        webServiceCall = new WebServiceCall(this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);

        Calendar mCalendar = Calendar.getInstance(Locale.getDefault());
        mCalendar.set(Calendar.YEAR, 1900);
        datePicker.positiveAction(getString(android.R.string.ok)).negativeAction(getString(android.R.string.cancel));

        UserDetail userDetail = ((ProfileActivity) getActivity()).getUserDetail();
        Bundle argument = getArguments();
        int position = argument.getInt("edit_user_edu_info", -1);
        int eduTypeId = argument.getInt("edu_type");
        if (position > -1) {
            educationInfo = userDetail.getEducationInfoList().get(position);
            mBinding.etName.setText(educationInfo.getName());
            mBinding.etCityTown.setText(educationInfo.getFullAddress());
            mBinding.etFromYear.setText(educationInfo.getFromDate());
            mBinding.etToYear.setText(educationInfo.getToDate());

            if (educationInfo.getCompleted() == 1) {
                isGraduate = true;
                mBinding.etGraduate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_tick_orange_20dp, 0);
                educationInfo.setCompleted(1);
            } else {
                isGraduate = false;
                mBinding.etGraduate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_orange_20dp, 0);
                educationInfo.setCompleted(0);
            }

        } else {
            educationInfo = new EducationInfo();
            educationInfo.setTypeId(eduTypeId);
        }
        mBinding.etName.setHint(educationInfo.getTypeId() == EducationInfo.EDUCATION_TYPE_SCHOOL ? "High School" : "University");
    }


    private boolean isUserInputValid() {
        String name, fullAddress, fromDate, toDate;

        name = mBinding.etName.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            mBinding.etName.setError("Enter " + (educationInfo.getTypeId() == EducationInfo.EDUCATION_TYPE_SCHOOL ? "High School" : "University"));
            return false;
        }

        fullAddress = mBinding.etCityTown.getText().toString();
        if (TextUtils.isEmpty(fullAddress)) {
            mBinding.etCityTown.setError("Enter City/Town");
            return false;
        }

        fromDate = mBinding.etFromYear.getText().toString();
        if (TextUtils.isEmpty(fromDate)) {
            mBinding.etFromYear.setError("Enter From date");
            return false;
        }

        toDate = mBinding.etToYear.getText().toString();
        if (isGraduate && TextUtils.isEmpty(toDate)) {
            mBinding.etToYear.setError("Enter To date");
            return false;
        }

        educationInfo.setName(name);
        educationInfo.setFullAddress(fullAddress);
        educationInfo.setFromDate(fromDate);
        educationInfo.setToDate(toDate);
        educationInfo.setCompleted(isGraduate ? 1 : 0);

        return true;
    }


    public void saveBTnClick(View v) {
        if (isUserInputValid()) {
            webServiceCall.editEduInfo(getContext(), UserPreferences.fetchUserId(getContext()), educationInfo);
        }
    }

    public void fromDateClick(View v) {
        isFromDate = true;

        String toDate = mBinding.etToYear.getText().toString();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, 1900);
        if (!TextUtils.isEmpty(toDate)) {
            Calendar mCal = Calendar.getInstance();
            mCalendar.set(Calendar.YEAR, 1900);
            try {
                mCal.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(toDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            datePicker.dateRange(mCalendar.getTimeInMillis(), mCal.getTimeInMillis());
        } else {
            datePicker.dateRange(mCalendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
        }

        DialogFragment fragment = DialogFragment.newInstance(datePicker);
        fragment.show(getChildFragmentManager(), fragment.getClass().getSimpleName());
    }

    public void toDateClick(View v) {
        isFromDate = false;

        String fromDate = mBinding.etFromYear.getText().toString();
        if (!TextUtils.isEmpty(fromDate)) {
            Calendar mCalendar = Calendar.getInstance();
            try {
                mCalendar.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(fromDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            datePicker.dateRange(mCalendar.getTimeInMillis(), Calendar.getInstance().getTimeInMillis());
        }


        DialogFragment fragment = DialogFragment.newInstance(datePicker);
        fragment.show(getChildFragmentManager(), fragment.getClass().getSimpleName());
    }


    public void graduateToggle(View v) {
        isGraduate = !isGraduate;
        if (isGraduate) {
            mBinding.etGraduate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_tick_orange_20dp, 0);
            educationInfo.setCompleted(1);
        } else {
            mBinding.etGraduate.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_circle_orange_20dp, 0);
            educationInfo.setCompleted(0);
        }
    }


    public void placeSelect(View v) {
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(getActivity());
            startActivityForResult(intent, ProfileActivity.EDU_PLACE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    public void deleteEduInfo() {
        webServiceCall.deleteEduInfo(getContext(), UserPreferences.fetchUserId(getContext()), educationInfo.getId());
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ProfileActivity.EDU_PLACE) {
            if (resultCode == Activity.RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(getContext(), data);
                mBinding.etCityTown.setText(place.getAddress());

                LatLng latLng = place.getLatLng();
                educationInfo.setLatitude(String.valueOf(latLng.latitude));
                educationInfo.setLongitude(String.valueOf(latLng.longitude));


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


    @SuppressLint("ParcelCreator")
    private class DatePicker extends DatePickerDialog.Builder {

        DatePicker() {
            super(R.style.Material_App_Dialog_DatePicker_Light);
        }

        @Override
        public void onPositiveActionClicked(DialogFragment fragment) {
            DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
            if (isFromDate)
                mBinding.etFromYear.setText(Utility.parseDate(dialog.getFormattedDate(SimpleDateFormat.getDateInstance()), "dd-MMM-yyyy", "yyyy-MM-dd"));
            else
                mBinding.etToYear.setText(Utility.parseDate(dialog.getFormattedDate(SimpleDateFormat.getDateInstance()), "dd-MMM-yyyy", "yyyy-MM-dd"));
            super.onPositiveActionClicked(fragment);
        }

        @Override
        public void onNegativeActionClicked(DialogFragment fragment) {
            super.onNegativeActionClicked(fragment);
        }
    }
}
