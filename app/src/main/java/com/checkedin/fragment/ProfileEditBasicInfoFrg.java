package com.checkedin.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.databinding.FrgBasicInfoBinding;
import com.checkedin.dialog.ImageChooserDialog;
import com.checkedin.model.Photos;
import com.checkedin.model.UserDetail;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.app.DatePickerDialog;
import com.material.app.DialogFragment;
import com.material.widget.Spinner;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ProfileEditBasicInfoFrg extends Fragment implements SelectImage.ActivityResult, VolleyStringRequest.AfterResponse {

    private FrgBasicInfoBinding mBinding;
    private String imageUrl, firstName, lastName, gender, relationshipStatus, birthday;

    private DatePicker datePicker;
    private WebServiceCall webServiceCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_basic_info, container, false);
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
        mCalendar.set(Calendar.DATE, 1);
        mCalendar.set(Calendar.MONTH, 1);
        datePicker.dateRange(mCalendar.getTime().getTime(), new Date().getTime());
        datePicker.positiveAction(getString(android.R.string.ok)).negativeAction(getString(android.R.string.cancel));


        ((ProfileActivity) getActivity()).setTitle(getString(R.string.edit_profile));
        UserDetail userDetail = ((ProfileActivity) getActivity()).getUserDetail();
        String url = WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.THUMB_SIZE_PATH + userDetail.getImageUrl();
        Utility.loadImageGlide(mBinding.civProfileImg, url);

//        Glide.with(getContext()).load(url).into(mBinding.civProfileImg);

        mBinding.etFname.setText(userDetail.getFirstName());
        mBinding.etLname.setText(userDetail.getLastName());
        mBinding.etDob.setText(userDetail.getDatOfBirth());

        mBinding.spGender.setAdapter(new ArrayAdapter<>(getContext(), R.layout.simple_textview_layout, getContext().getResources().getStringArray(R.array.arr_gender)));
        mBinding.spRelationship.setAdapter(new ArrayAdapter<>(getContext(), R.layout.simple_textview_layout, getContext().getResources().getStringArray(R.array.arr_marital_status)));


        switch (userDetail.getMaritalStatus()) {
            default:
            case UserDetail.USER_GENDER_MALE:
                mBinding.spGender.setSelection(0);
                break;
            case UserDetail.USER_GENDER_FEMALE:
                mBinding.spGender.setSelection(1);
                break;
        }


        switch (userDetail.getMaritalStatus()) {
            default:
            case UserDetail.USER_RELATIONSHIP_SINGLE:
                mBinding.spGender.setSelection(0);
                break;
            case UserDetail.USER_RELATIONSHIP_MARRIED:
                mBinding.spGender.setSelection(1);
                break;
            case UserDetail.USER_RELATIONSHIP_IN_A_RELATION:
                mBinding.spGender.setSelection(2);
                break;

        }

        mBinding.spGender.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {
                ((TextView) mBinding.spGender.getSelectedView()).setText(position == 0 ? UserDetail.USER_GENDER_MALE : UserDetail.USER_GENDER_FEMALE);
                return false;
            }
        });

        mBinding.spRelationship.setOnItemClickListener(new Spinner.OnItemClickListener() {
            @Override
            public boolean onItemClick(Spinner parent, View view, int position, long id) {

                switch (position) {
                    case 0:
                        ((TextView) mBinding.spRelationship.getSelectedView()).setText(UserDetail.USER_RELATIONSHIP_SINGLE);
                        break;
                    case 1:
                        ((TextView) mBinding.spRelationship.getSelectedView()).setText(UserDetail.USER_RELATIONSHIP_MARRIED);
                        break;
                    case 2:
                        ((TextView) mBinding.spRelationship.getSelectedView()).setText(UserDetail.USER_RELATIONSHIP_IN_A_RELATION);
                        break;
                }

                return false;
            }
        });
    }

    private boolean isUserInputValid() {
        firstName = mBinding.etFname.getText().toString().trim();
        if (TextUtils.isEmpty(firstName)) {
            mBinding.etFname.setError("Enter First Name");
            return false;
        }
        lastName = mBinding.etLname.getText().toString().trim();
        if (TextUtils.isEmpty(lastName)) {
            mBinding.etLname.setError("Enter Last Name");
            return false;
        }

        gender = ((TextView) mBinding.spGender.getSelectedView()).getText().toString();
        relationshipStatus = ((TextView) mBinding.spRelationship.getSelectedView()).getText().toString();
        birthday = mBinding.etDob.getText().toString();
        return true;
    }


    public void profileImgClick(View v) {
        PermissionEverywhere.getPermission(getContext(), new String[]{Manifest.permission.CAMERA}, 101)
                .enqueue(new PermissionResultCallback() {
                    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                    @Override
                    public void onComplete(PermissionResponse permissionResponse) {
                        if (permissionResponse.isGranted()) {
                            PermissionEverywhere.getPermission(getContext(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101)
                                    .enqueue(new PermissionResultCallback() {
                                        @Override
                                        public void onComplete(PermissionResponse permissionResponse) {
                                            if (permissionResponse.isGranted()) {
                                                ImageChooserDialog selectImageDialog = new ImageChooserDialog(getActivity(), ProfileEditBasicInfoFrg.this);
                                                selectImageDialog.show();
                                            }
                                        }
                                    });
                        }
                    }
                });


    }

    public void birthdayClick(View v) {
        DialogFragment fragment = DialogFragment.newInstance(datePicker);
        fragment.show(getChildFragmentManager(), fragment.getClass().getSimpleName());
    }

    public void saveBtnClick(View v) {
        if (isUserInputValid())
            webServiceCall.editUserBasicInfo(getContext(), UserPreferences.fetchUserId(getContext()), new String[]{imageUrl, firstName, lastName, gender, relationshipStatus, birthday});
    }

    @Override
    public void onResult(Uri uriImagePath) {
        imageUrl = uriImagePath.getPath();
        mBinding.civProfileImg.setImageURI(uriImagePath);
    }

    @Override
    public void onResult(ArrayList<Uri> allUriImagePath) {

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
            try {
                DatePickerDialog dialog = (DatePickerDialog) fragment.getDialog();
                mBinding.etDob.setText(Utility.parseDate(dialog.getFormattedDate(SimpleDateFormat.getDateInstance()), "dd-MMM-yyyy", "yyyy-MM-dd"));
                super.onPositiveActionClicked(fragment);
            } catch (Exception ignored) {

            }
        }

        @Override
        public void onNegativeActionClicked(DialogFragment fragment) {
            super.onNegativeActionClicked(fragment);
        }
    }

}
