package com.checkedin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.databinding.ActivityForgotPasswordBinding;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

public class ForgotPassDialog extends Dialog implements OnClickListener, OnEditorActionListener, VolleyStringRequest.AfterResponse {

    private final Context context;
    private String email;
    private WebServiceCall webServiceCall;
    private ActivityForgotPasswordBinding mBinding;

    public ForgotPassDialog(Context context) {
        super(context, R.style.AppTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.activity_forgot_password, null, false);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        setContentView(mBinding.getRoot());
        webServiceCall = new WebServiceCall(this);
        mBinding.btnSend.setBackgroundDrawable(roundRectShape(Utility.colorRes(context, R.color.colorPrimary)));

        addFocusListnerToEdittext();
        mBinding.etEmail.setOnEditorActionListener(this);
        mBinding.btnSend.setOnClickListener(this);
        mBinding.ivBack.setOnClickListener(this);
    }


    private Drawable roundRectShape(int color) {
        GradientDrawable gdRoundRect = new GradientDrawable();
        gdRoundRect.setColor(color);
        gdRoundRect.setCornerRadius(5);
        return gdRoundRect;
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.iv_back:
                    dismiss();
                    break;
                case R.id.btn_send:
                    if (validation()) {
                        webServiceCall.forgotPassWsCall(context, email);
                    }
                    break;
            }
        }
    }

    private boolean validation() {

        email = mBinding.etEmail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            mBinding.etEmail.setError("Enter email");
            return false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.etEmail.setError("Invalid email");
            return false;

        }

        return true;
    }

    private void addFocusListnerToEdittext() {

        mBinding.etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mBinding.etEmail.setError(null);
            }

        });

    }


    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel mForgotPassword = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, "Forgot Password");
        if (mForgotPassword != null) {
            if (mForgotPassword.getStatus() == BaseModel.STATUS_SUCCESS) {
                Toast.makeText(context, mForgotPassword.getMessage(), Toast.LENGTH_LONG).show();
                dismiss();
            } else {
                Utility.showSnackBar(findViewById(android.R.id.content), mForgotPassword.getMessage());
            }
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.server_connect_error));
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (validation()) {
            webServiceCall.forgotPassWsCall(context, email);
        }

        return false;
    }
}
