package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.databinding.FrgContactInfoBinding;
import com.checkedin.model.UserDetail;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
public class ProfileEditContactInfoFrg extends Fragment implements VolleyStringRequest.AfterResponse {

    private FrgContactInfoBinding mBinding;

    private WebServiceCall webServiceCall;
    private String email, username, mobile;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_contact_info, container, false);

        webServiceCall = new WebServiceCall(this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBinding.setFragment(this);

        ((ProfileActivity) getActivity()).setTitle(getString(R.string.edit_profile));
        UserDetail userDetail = ((ProfileActivity) getActivity()).getUserDetail();

        mBinding.etEmail.setText(userDetail.getEmail());
        mBinding.etMobile.setText(userDetail.getMobileNo());
        mBinding.etUsername.setText(userDetail.getUsername());

    }

    private boolean isUserInputValid() {

        username = mBinding.etUsername.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            mBinding.etUsername.setError("Enter username");
            return false;
        }

        email = mBinding.etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mBinding.etEmail.setError("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.etEmail.setError("Invalid email");
            return false;

        }

        mobile = mBinding.etMobile.getText().toString().trim();
        if (TextUtils.isEmpty(mobile)) {
            mBinding.etMobile.setError("Enter mobile");
            return false;
        } else if (!Patterns.PHONE.matcher(mobile).matches()) {
            mBinding.etMobile.setError("Enter valid mobile no.");
            return false;
        } else if (mobile.length() < 8) {
            mBinding.etMobile.setError("Enter valid mobile no.");
            return false;

        }
        return true;

    }


    public void saveBtnClick(View v) {
        if (isUserInputValid())
            webServiceCall.editContactInfo(getContext(), UserPreferences.fetchUserId(getContext()), new String[]{email, username, mobile});
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
