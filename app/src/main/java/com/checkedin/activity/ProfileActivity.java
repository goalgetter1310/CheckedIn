package com.checkedin.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.fragment.ProfileEditBasicInfoFrg;
import com.checkedin.fragment.ProfileEditContactInfoFrg;
import com.checkedin.fragment.ProfileEditEduInfoFrg;
import com.checkedin.fragment.ProfileEditLivingPlaceInfoFrg;
import com.checkedin.fragment.ProfileEditWorkInfoFrg;
import com.checkedin.fragment.ProfileMainFrg;
import com.checkedin.model.UserDetail;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

public class ProfileActivity extends AppCompatActivity implements VolleyStringRequest.AfterResponse, View.OnClickListener {

    private Fragment[] fragments;
    private WebServiceCall webServiceCall;

    public static final int EDU_PLACE = 111;
    public static final int LIVING_PLACE = 112;
    private boolean isEditable;
    private ImageView ivDelete;
    private TextView tvTitle;

    public void setUserDetail(UserDetail userDetail) {
        this.userDetail = userDetail;
    }

    private UserDetail userDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
         tvTitle= (TextView) findViewById(R.id.tv_title);
        ivDelete = (ImageView) findViewById(R.id.iv_delete);

        webServiceCall = new WebServiceCall(this);

        fragments = new Fragment[6];
        fragments[0] = new ProfileMainFrg();
        fragments[1] = new ProfileEditBasicInfoFrg();
        fragments[2] = new ProfileEditContactInfoFrg();
        fragments[3] = new ProfileEditLivingPlaceInfoFrg();
        fragments[4] = new ProfileEditEduInfoFrg();
        fragments[5] = new ProfileEditWorkInfoFrg();

        Bundle extra = getIntent().getExtras();
        isEditable = extra.getBoolean("profile_editable");
        String userId = extra.getString("profile_userId", UserPreferences.fetchUserId(this));
        webServiceCall.userDetailsWsCall(this, userId);




        ivDelete.setOnClickListener(this);
        if (ivBack != null)
            ivBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
    }


    public UserDetail getUserDetail() {
        return userDetail;
    }


    public void setTitle(String title) {
        tvTitle.setText(title);
    }


    public void manageFragment(int position, Bundle bundle, boolean isAddToBackStack) {
        if (position == 0) {
            bundle = new Bundle();
            bundle.putBoolean("profile_editable", isEditable);
            fragments[position].setArguments(bundle);
        } else if (position == 4 || position == 5) {
            ivDelete.setTag(position);
        }

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(fragments[position].getClass().getSimpleName());

        if (bundle != null)
            fragments[position].setArguments(bundle);
        if (position > 0)
            fragmentTransaction.setCustomAnimations(R.anim.right2middle, R.anim.middle2left, R.anim.left2middle, R.anim.middle2right);
        fragmentTransaction.replace(R.id.fl_profile_container, fragments[position], fragments[position].getClass().getSimpleName());
        fragmentTransaction.commit();
    }

    @Override
    public void onResponseReceive(int requestCode) {
        UserDetailModel mUserDetail = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, UserDetailModel.class.getSimpleName());
        if (mUserDetail != null) {
            if (mUserDetail.getStatus() == 0) {
                userDetail = mUserDetail.getUserDetails();
                manageFragment(0, null, false);
            } else {
                Utility.showSnackBar(findViewById(android.R.id.content), mUserDetail.getMessage());
            }

        } else {
            Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
        }

    }


    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EDU_PLACE) {
            fragments[4].onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == LIVING_PLACE) {
            fragments[3].onActivityResult(requestCode, resultCode, data);
        } else if (resultCode == 100 || resultCode == 1) {
            SelectImage.getInstance().onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    public void onBackPressed() {
        Utility.hideKeyboard(this);
        int totalItemInStack = getSupportFragmentManager().getBackStackEntryCount();
        if (totalItemInStack > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        switch (position) {
            case 4:
                ((ProfileEditEduInfoFrg) fragments[4]).deleteEduInfo();
                break;
            case 5:
                ((ProfileEditWorkInfoFrg) fragments[5]).deleteWorkInfo();
                break;
        }
    }
}
