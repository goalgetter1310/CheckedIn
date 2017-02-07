package com.checkedin.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.databinding.ActivitySignupBinding;
import com.checkedin.dialog.ImageChooserDialog;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.util.ArrayList;

public class SignupActivity extends Activity implements OnClickListener, VolleyStringRequest.AfterResponse, SelectImage.ActivityResult {

    private String email, password, fname, lName, profileImg;
    private WebServiceCall webServiceCall;
    private ActivitySignupBinding mBinding;

    private ImageChooserDialog imgChooserDialog;
    private Uri uriImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_signup);

        webServiceCall = new WebServiceCall(this);
        imgChooserDialog = new ImageChooserDialog(this, this);

        mBinding.btnSignup.setBackgroundDrawable(roundRectShape(Utility.colorRes(this, R.color.colorPrimary)));

        addFocusListnerToEdittext();
        mBinding.civProfileImg.setOnClickListener(this);
        mBinding.btnSignup.setOnClickListener(this);
        mBinding.ivBack.setOnClickListener(this);
    }

    private Drawable roundRectShape(int color) {
        GradientDrawable gdRoundRect = new GradientDrawable();
        gdRoundRect.setColor(color);
        gdRoundRect.setCornerRadius(5);
        return gdRoundRect;
    }

    private boolean validation() {
        fname = mBinding.etFname.getText().toString().trim();
        if (TextUtils.isEmpty(fname)) {
            mBinding.etFname.setError("Enter First Name");
            return false;
        } else if (!Utility.isOnlyAlphabet(fname)) {
            mBinding.etFname.setError("Enter Proper Name");
            return false;
        }
        lName = mBinding.etLname.getText().toString().trim();
        if (TextUtils.isEmpty(lName)) {
            mBinding.etLname.setError("Enter Last Name");
            return false;
        } else if (!Utility.isOnlyAlphabet(lName)) {
            mBinding.etLname.setError("Enter Proper Name");
            return false;
        }

        email = mBinding.etEmail.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mBinding.etEmail.setError("Enter email");
            return false;

        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            mBinding.etEmail.setError("Invalid email");
            return false;

        }

        password = mBinding.etPassword.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mBinding.etPassword.setError("Enter password");
            return false;
        } else if (password.length() < 8) {
            mBinding.etPassword.setError("Min. 8 Character.!");
            return false;
        }

        String conPassword = mBinding.etConfirmPassword.getText().toString();

        if (!password.equals(conPassword)) {
            mBinding.etConfirmPassword.setError("Password do not match");
            return false;
        }

        if (uriImagePath == null) {
            Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.select_profile_img));
            return false;
        }


        return true;
    }

    private void addFocusListnerToEdittext() {
        mBinding.etFname.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mBinding.etFname.setError(null);
            }

        });
        mBinding.etLname.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mBinding.etLname.setError(null);
            }

        });


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

        mBinding.etConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                mBinding.etConfirmPassword.setError(null);
            }

        });

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.civ_profileImg:
                    PermissionEverywhere.getPermission(this, new String[]{Manifest.permission.CAMERA}, 101)
                            .enqueue(new PermissionResultCallback() {
                                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                                @Override
                                public void onComplete(PermissionResponse permissionResponse) {
                                    if (permissionResponse.isGranted()) {
                                        PermissionEverywhere.getPermission(SignupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101)
                                                .enqueue(new PermissionResultCallback() {
                                                    @Override
                                                    public void onComplete(PermissionResponse permissionResponse) {
                                                        if (permissionResponse.isGranted()) {
                                                            imgChooserDialog.show();
                                                        }
                                                    }
                                                });
                                    }
                                }
                            });


                    break;
                case R.id.iv_back:
                    finish();
                    break;
                case R.id.btn_signup:
                    if (validation()) {
                        webServiceCall.signupWsCall(this, new String[]{profileImg, fname, lName, email, password});
                    }
                    break;

            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SelectImage.getInstance().onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onResponseReceive(int requestCode) {
        if (requestCode == WebServiceCall.REQUEST_CODE_SIGNUP) {
            UserDetailModel mSignUp = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, "Signup Response");
            if (mSignUp != null) {
                if (mSignUp.getStatus() == BaseModel.STATUS_SUCCESS) {
                    Toast.makeText(this, mSignUp.getMessage(), Toast.LENGTH_LONG).show();
                    UserPreferences.saveFirstTime(this);
                    UserPreferences.saveUserDetails(this, mSignUp.getUserDetails());
                    webServiceCall.deviceTokenWsCall(this);
                } else {
                    Utility.showSnackBar(findViewById(android.R.id.content), mSignUp.getMessage());
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
    public void onErrorReceive() {
        Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }

    @Override
    public void onResult(Uri uriImagePath) {
        this.uriImagePath = uriImagePath;
        mBinding.civProfileImg.setImageURI(uriImagePath);
        profileImg = uriImagePath.getPath();
    }

    @Override
    public void onResult(ArrayList<Uri> allUriImagePath) {

    }
}
