package com.checkedin.activity;

import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.checkedin.R;
import com.checkedin.databinding.ActivityLoginBinding;
import com.checkedin.dialog.ForgotPassDialog;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

public class LoginActivity extends Activity implements OnClickListener, VolleyStringRequest.AfterResponse {

    private WebServiceCall webServiceCall;
    private String email, password;
    private ActivityLoginBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);

        webServiceCall = new WebServiceCall(this);
        mBinding.btnSignin.setBackgroundDrawable(roundRectShape(Utility.colorRes(this, R.color.colorPrimary)));
        getEmailPassword();

//        if (SplashActivity.isDeveloperPreview)
//            temp();

        addFocusListnerToEdittext();
        mBinding.btnSignin.setOnClickListener(this);
        mBinding.tvForgotPass.setOnClickListener(this);
    }


    private Drawable roundRectShape(int color) {
        GradientDrawable gdRoundRect = new GradientDrawable();
        gdRoundRect.setColor(color);
        gdRoundRect.setCornerRadius(5);
        return gdRoundRect;
    }

    private void temp() {// remove this
        mBinding.etEmail.setText("steve_smith@gmail.com");
        mBinding.etPassword.setText("12345678");
    }

    private void getEmailPassword() {
        String[] loginPreference = UserPreferences.fetchLoginPreference(this);
        mBinding.etEmail.setText(loginPreference[0]);
        mBinding.etPassword.setText(loginPreference[1]);
    }

    private boolean validation() {

        email = mBinding.etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mBinding.etEmail.setError("Enter email");
            return false;

        }

        password = mBinding.etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mBinding.etPassword.setError("Enter password");
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

        mBinding.etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mBinding.etPassword.setError(null);
            }

        });

    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.btn_signin:
                    if (validation()) {
                        webServiceCall.loginWsCall(this, email, password);
                        Utility.hideKeyboard(this);
                    }

                    break;
                case R.id.tv_forgot_pass:
                    new ForgotPassDialog(this).show();
                    break;
            }
        }
    }

//    private void lastLocationConfirm(String placeId, long time) {
//        UserPreferences.saveLastCheckinTime(this, time);
//        Intent iLastLocationConfirm = new Intent(this, CheckinConfirmReceiver.class);
//        iLastLocationConfirm.putExtra("last_checkin_place_id", placeId);
//
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, iLastLocationConfirm, PendingIntent.FLAG_UPDATE_CURRENT);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
//
//        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
//    }

    @Override
    public void onResponseReceive(int requestCode) {

        if (requestCode == WebServiceCall.REQUEST_CODE_LOGIN) {
            UserDetailModel mLogin = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, "Login Response");
            if (mLogin != null) {
                if (mLogin.getStatus() == BaseModel.STATUS_SUCCESS) {
                    if (mBinding.cbRememberPassword.isChecked()) {
                        UserPreferences.saveLoginPreference(this, email, password);
                    } else {
                        UserPreferences.removeRemeberMe(this);
                    }

                    UserPreferences.saveUserDetails(this, mLogin.getUserDetails());
                    webServiceCall.deviceTokenWsCall(this);
                } else {
                    Utility.showSnackBar(findViewById(android.R.id.content), mLogin.getMessage());
                }
            } else {
                Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
            }
        } else if (requestCode == WebServiceCall.REQUEST_CODE_REGISTER_DEVICE_TOKEN) {
//            webServiceCall.lastCheckinUserWsCall(this);
            BaseModel mBase = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
            Utility.logUtils(mBase != null ? mBase.getMessage() : getString(R.string.server_connect_error));
            setResult(RESULT_OK);
            finish();
        }

//        else {
//            LastCheckinModel mLastCheckin = (LastCheckinModel) webServiceCall.volleyRequestInstatnce().getModelObject(LastCheckinModel.class, LastCheckinModel.class.getSimpleName());
//            if (mLastCheckin != null) {
//                long time = System.currentTimeMillis() + (CheckinConfirmActivity.CHECKIN_CONFIRM_POPUP_INTERVAL * 3600 * 1000);
//                lastLocationConfirm(mLastCheckin.getLastCheckin().getPlaceId(), time);
//
//            }
//        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }

}
