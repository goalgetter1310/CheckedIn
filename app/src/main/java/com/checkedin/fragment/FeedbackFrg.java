package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.databinding.FrgFeedbackBinding;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.widget.EditText;

public class FeedbackFrg extends Fragment implements View.OnClickListener, VolleyStringRequest.AfterResponse {

    private WebServiceCall webServiceCall;
    private String title;
    private String desecription;

    private FrgFeedbackBinding mBinding;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.frg_feedback, container, false);

        webServiceCall = new WebServiceCall(this);
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.feedback));


        mBinding.etTitle.addTextChangedListener(new TextChange(mBinding.etTitle));
        mBinding.etDesc.addTextChangedListener(new TextChange(mBinding.etDesc));
        mBinding.btnSubmit.setOnClickListener(this);
        return mBinding.getRoot();
    }


    private boolean validation() {

        title = mBinding.etTitle.getText().toString().trim();
        if (TextUtils.isEmpty(title)) {
            mBinding.etTitle.setError("Enter title");
            return false;
        }

        desecription = mBinding.etDesc.getText().toString().trim();
        if (TextUtils.isEmpty(desecription)) {
            mBinding.etDesc.setError("Enter description");
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        if (validation()) {
            Utility.hideKeyboard(getActivity());
            webServiceCall.feedbackWsCall(getContext(), title, desecription);
        }
    }

    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
        if (baseModel != null) {
            mBinding.etTitle.setText("");
            mBinding.etDesc.setText("");
            mBinding.etTitle.clearFocus();
            mBinding.etDesc.clearFocus();
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), baseModel.getMessage());
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
        }


    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webServiceCall != null && webServiceCall.volleyRequestInstatnce() != null) {
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_FEEDBACK);
        }
    }


    private class TextChange implements TextWatcher {

        private final EditText editText;

        private TextChange(EditText editText) {
            this.editText = editText;
        }


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            editText.setError(null);
        }
    }

}
