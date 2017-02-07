package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
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

public class InviteByEmailFrg extends Fragment implements VolleyStringRequest.AfterResponse {
    private WebServiceCall webServiceCall;
    private EditText etEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_invite_email, container, false);
        etEmail = (EditText) view.findViewById(R.id.et_inviteByEmail_email);
        Button btnInvite = (Button) view.findViewById(R.id.btn_inviteByEmail_invite);
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_invite));
        webServiceCall = new WebServiceCall(this);

        btnInvite.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString().trim();
                if (TextUtils.isEmpty(email)) {
                    etEmail.setError("Enter Email");
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    etEmail.setError("Enter Proper Email");
                } else {
                    webServiceCall.sendInvitationByEmailWsCall(getContext(), email);
                }
            }
        });
        return view;
    }

    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
        if (baseModel != null) {
            if (baseModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                etEmail.setText("");
                Utility.hideKeyboard(getActivity());
            }
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), baseModel.getMessage());
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }
}
