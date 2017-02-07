package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.databinding.FrgPrivacyBinding;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.widget.Switch;
public class PrivacyFrg extends Fragment implements VolleyStringRequest.AfterResponse {

    private FrgPrivacyBinding mBinding;

    private WebServiceCall webServiceCall;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_privacy, container, false);

        webServiceCall = new WebServiceCall(this);

        return mBinding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_privacy));


        mBinding.switchMobile.setChecked(UserPreferences.fetchUserDetails(getContext()).isShowMobile());
        mBinding.switchMobile.setOnCheckedChangeListener(new Switch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(Switch view, boolean checked) {
                webServiceCall.userPrivacyWsCall(getContext(), checked);
            }
        });


    }

    @Override
    public void onResponseReceive(int requestCode) {
        UserDetailModel mUserDetail = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, UserDetailModel.class.getSimpleName());
        if (mUserDetail != null) {
            if (mUserDetail.getStatus() == BaseModel.STATUS_SUCCESS) {
                UserPreferences.saveUserDetails(getContext(), mUserDetail.getUserDetails());
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mUserDetail.getMessage());
            }
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
        }

    }

    @Override
    public void onErrorReceive() {

        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getContext().getString(R.string.server_connect_error));
    }
}
