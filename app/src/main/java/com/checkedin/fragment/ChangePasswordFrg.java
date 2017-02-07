package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.widget.Button;
import com.material.widget.EditText;

public class ChangePasswordFrg extends Fragment implements OnClickListener, VolleyStringRequest.AfterResponse {
    private EditText etOldPasss, etNewPass, etNewConfirmPass;
    private Button btnChange;
    private WebServiceCall webServiceCall;
    private String oldPass, newPass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_change_password, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        addFocusListnerToEdittext();
//        if (UserPreferences.isSocialLogin(getActivity())) {
//            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), "Cannot Change Password! You may have login with Facebook or Twitter");
//        }
        btnChange.setOnClickListener(this);
        return view;
    }

    private void initViews(View view) {
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_change_password));

        etOldPasss = (EditText) view.findViewById(R.id.et_change_pass_old);
        etNewPass = (EditText) view.findViewById(R.id.et_change_pass_new);
        etNewConfirmPass = (EditText) view.findViewById(R.id.et_change_pass_cnew);
        btnChange = (Button) view.findViewById(R.id.btn_change_pass_change);

        webServiceCall = new WebServiceCall(this);
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
//            if (UserPreferences.isSocialLogin(getActivity())) {
//                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), "Can't Change Password! You may have login with Facebook or Twitter");
//            } else {
            if (validation()) {
                webServiceCall.changePassWsCall(getContext(), oldPass, newPass);
            }
//            }
        }
    }

    private boolean validation() {

        oldPass = etOldPasss.getText().toString();
        if (TextUtils.isEmpty(oldPass)) {
            etOldPasss.setError("Enter Old password");
            return false;
        }
        newPass = etNewPass.getText().toString();
        if (TextUtils.isEmpty(newPass)) {
            etNewPass.setError("Enter New password");
            return false;
        } else if (newPass.length() < 8) {
            etNewPass.setError("Min. 8 Character.!");
            return false;
        } else if (oldPass.equals(newPass)) {
            etNewPass.setError("Old Password and New Password should be different");
            return false;
        }
        String conPassword = etNewConfirmPass.getText().toString();

        if (!newPass.equals(conPassword)) {
            etNewConfirmPass.setError("Password do not match");
            return false;
        }

        return true;
    }

    private void addFocusListnerToEdittext() {

        etOldPasss.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etOldPasss.setError(null);
            }

        });

        etNewPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etNewPass.setError(null);
            }

        });
        etNewConfirmPass.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                etNewConfirmPass.setError(null);
            }

        });

    }


    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel mChangePassword = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
        if (mChangePassword != null) {
            if (mChangePassword.getStatus() == BaseModel.STATUS_SUCCESS) {
                etOldPasss.setText("");
                etNewPass.setText("");
                etNewConfirmPass.setText("");
            }
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mChangePassword.getMessage());
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }
}
