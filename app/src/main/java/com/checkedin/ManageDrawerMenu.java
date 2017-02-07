package com.checkedin;


import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.checkedin.activity.MainActivity;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.activity.WelcomeActivity;
import com.checkedin.container.AboutUsFrgContainer;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.container.FavouriteFrgContainer;
import com.checkedin.container.HomeFrgContainer;
import com.checkedin.container.InviteFriendContainer;
import com.checkedin.container.NotificationsContainer;
import com.checkedin.container.SettingFrgContainer;
import com.checkedin.container.TutorialFrgContainer;
import com.checkedin.dialog.ConfirmDialog;
import com.checkedin.dialog.ImageChooserDialog;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.UserDetailModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.util.ArrayList;

public class ManageDrawerMenu implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener, VolleyStringRequest.AfterResponse, SelectImage.ActivityResult {


    private final Context context;
    private boolean isNavigationIconAnimate = true;

    private int navigationState;

    private CircleImageView civProfilePic;
    private TextView tvUsername, tvCity;
    private DrawerLayout dlMain;
    private ManagerDrawer mDrawerToggle;
    private LinearLayout llDrawer;
    private NavigationView nvDrawer;

    private WebServiceCall webServiceCall;
    private int pos=0;

    public ManageDrawerMenu(Context context) {
        this.context = context;
    }

    public void init(Toolbar tbMain, DrawerLayout dlMain) {
        Activity activity = (Activity) context;
        this.dlMain = dlMain;

        nvDrawer = (NavigationView) activity.findViewById(R.id.nv_drawer);
        View headerView = nvDrawer.inflateHeaderView(R.layout.drawer_header);
        ImageView ivEditProfile = (ImageView) headerView.findViewById(R.id.iv_drawer_profileEdit);
        llDrawer = (LinearLayout) activity.findViewById(R.id.ll_drawer);

        civProfilePic = (CircleImageView) headerView.findViewById(R.id.civ_drawer_image);
        ImageView ivEditProfileImg = (ImageView) headerView.findViewById(R.id.iv_edit_profileImg);
        tvUsername = (TextView) headerView.findViewById(R.id.tv_drawer_user_name);
        tvCity = (TextView) headerView.findViewById(R.id.tv_drawer_user_city);


        mDrawerToggle = new ManagerDrawer((Activity) context, dlMain, tbMain, R.string.app_name, R.string.app_name);
        webServiceCall = new WebServiceCall(this);
        mDrawerToggle.syncState();
        ivEditProfile.setColorFilter(Utility.colorRes(context, R.color.colorPrimary));
        disableNavigationViewScrollbars();

        tbMain.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    if (navigationState == 1)
                        ManageDrawerMenu.this.dlMain.openDrawer(llDrawer);
                    else
                        ((Activity) context).onBackPressed();
                }
            }
        });
        ivEditProfile.setOnClickListener(this);
        ivEditProfileImg.setOnClickListener(this);
        civProfilePic.setOnClickListener(this);
        dlMain.addDrawerListener(mDrawerToggle);
        nvDrawer.setNavigationItemSelectedListener(this);
    }


    @SuppressWarnings({"ConstantConditions", "deprecation"})
    public void setDrawerHeader() {
        Utility.loadImageGlide(civProfilePic, UserPreferences.fetchProfileImageUrl(context));
//        Picasso.with(context).load(UserPreferences.fetchProfileImageUrl(context)).error(context.getResources().getDrawable(R.drawable.ic_placeholder)).into(civProfilePic);
        tvUsername.setText(UserPreferences.fetchUserFullName(context));
        tvCity.setText(UserPreferences.fetchUserCity(context));
    }


    public void toggleActionBarIcon(int state, boolean animate) {
        navigationState = state;
        isNavigationIconAnimate = true;
        if (animate) {
            float start = state == 0 ? 0f : 1.0f;
            final float end = Math.abs(start - 1);
            ValueAnimator offsetAnimator = ValueAnimator.ofFloat(start, end);
            offsetAnimator.setDuration(300);
            offsetAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
            offsetAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float offset = (Float) animation.getAnimatedValue();
                    mDrawerToggle.onDrawerSlide(null, offset);
                    if (end == offset) {
                        isNavigationIconAnimate = false;
                    }
                }
            });
            offsetAnimator.start();
        }

    }

    private void disableNavigationViewScrollbars() {
        if (nvDrawer != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) nvDrawer.getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_edit_profileImg:
                PermissionEverywhere.getPermission(context, new String[]{Manifest.permission.CAMERA}, 101)
                        .enqueue(new PermissionResultCallback() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void onComplete(PermissionResponse permissionResponse) {
                                if (permissionResponse.isGranted()) {
                                    PermissionEverywhere.getPermission(context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101)
                                            .enqueue(new PermissionResultCallback() {
                                                @Override
                                                public void onComplete(PermissionResponse permissionResponse) {
                                                    if (permissionResponse.isGranted()) {
                                                        ImageChooserDialog selectImageDialog = new ImageChooserDialog((Activity) context, ManageDrawerMenu.this);
                                                        selectImageDialog.show();
                                                    }
                                                }
                                            });
                                }
                            }
                        });

                break;
            case R.id.iv_drawer_profileEdit:

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent iProfileActivity = new Intent(context, ProfileActivity.class);
                        iProfileActivity.putExtra("profile_userId", UserPreferences.fetchUserId(context));
                        iProfileActivity.putExtra("profile_editable", true);
                        context.startActivity(iProfileActivity);
                    }
                }, 300);
                ManageDrawerMenu.this.dlMain.closeDrawer(GravityCompat.START);

                break;

            case R.id.civ_drawer_image:
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                            Utility.doubleTapTime = System.currentTimeMillis();
                            try {
                                TimelineFrg friendProfile = new TimelineFrg();
                                Bundle argument = new Bundle();
                                argument.putString("friend_id", UserPreferences.fetchUserId(context));
                                friendProfile.setArguments(argument);
                                DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                                dialogFrgContainer.init(friendProfile);
                                dialogFrgContainer.show(((AppCompatActivity) context).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, 300);
                ManageDrawerMenu.this.dlMain.closeDrawer(GravityCompat.START);

                break;
        }
    }

    public void openHome() {
        manageNavigation(nvDrawer.getMenu().getItem(0));
        pos=0;
    }


    public void openNotification() {
        manageNavigation(nvDrawer.getMenu().getItem(3));
    }

    private void manageNavigation(MenuItem item) {

        if (item.getItemId() != R.id.itm_nav_menu_logout) {
            for (int menuCounter = 0; menuCounter < nvDrawer.getMenu().size(); menuCounter++) {
                nvDrawer.getMenu().getItem(menuCounter).setChecked(false);
            }

            item.setChecked(true);
        }
        dlMain.closeDrawers();
        Fragment fContainer = null;

        switch (item.getItemId()) {
            case R.id.itm_nav_menu_home:
                Utility.currentFragment = context.getString(R.string.title_home);
                fContainer = new HomeFrgContainer();
                pos=0;
                break;
            case R.id.itm_nav_menu_friend_fav:
                Utility.currentFragment = context.getString(R.string.title_favourites);
                fContainer = new FavouriteFrgContainer();
                break;

            case R.id.itm_nav_menu_setting:
                Utility.currentFragment = context.getString(R.string.title_setting);
                fContainer = new SettingFrgContainer();
                break;
//            case R.id.itm_nav_menu_invite:
//                Utility.currentFragment = context.getString(R.string.title_invite);
//                fContainer = new InviteFriendContainer();
//                break;

            case R.id.itm_nav_menu_about:
                Utility.currentFragment = context.getString(R.string.title_about_us);
                fContainer = new AboutUsFrgContainer();
                break;

            case R.id.itm_nav_menu_notification:
                Utility.currentFragment = context.getString(R.string.title_notifications);
                fContainer = new NotificationsContainer();
                break;
            case R.id.itm_nav_menu_tutorial:
                Utility.currentFragment = context.getString(R.string.title_tutorial);
                fContainer = new TutorialFrgContainer();
                break;

            case R.id.itm_nav_menu_logout:
                alertBeforeLogout();
                break;
        }
        if (fContainer != null) {
            FragmentTransaction fTrans = ((AppCompatActivity) context).getSupportFragmentManager().beginTransaction();
            fTrans.replace(R.id.fl_container, fContainer, Utility.currentFragment);
            fTrans.commit();
        }
    }

    private void alertBeforeLogout() {

        new ConfirmDialog(context, R.string.logout_confirm, new ConfirmDialog.OnConfirmYes() {
            @Override
            public void confirmYes() {
                webServiceCall.logoutWsCall(context);
                UserPreferences.removeUserDetails(context);
            }
        }).show();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        manageNavigation(item);
        return false;
    }

    @Override
    public void onResponseReceive(int requestCode) {
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_LOGOUT:
                Intent iLogin = new Intent(context, WelcomeActivity.class);
                context.startActivity(iLogin);
                ((Activity) context).finish();
                break;
            case WebServiceCall.REQUEST_CODE_EDIT_PROFILE:
                UserDetailModel mUserDetail = (UserDetailModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserDetailModel.class, UserDetailModel.class.getSimpleName());
                if (mUserDetail != null) {
                    if (mUserDetail.getStatus() == BaseModel.STATUS_SUCCESS) {
                        UserPreferences.saveUserDetails(context, mUserDetail.getUserDetails());
                        setDrawerHeader();
                    }
                }
                break;
        }

    }

    @Override
    public void onErrorReceive() {

    }

    public boolean closeDrawer() {
        if (dlMain.isDrawerOpen(GravityCompat.START)) {
            dlMain.closeDrawer(llDrawer);
            return true;
        }
        return false;
    }

    public boolean isStackEmpty() {
        MainActivity activity = (MainActivity) context;

        Utility.hideKeyboard(activity);

        boolean isPopFragment = false;

        if (Utility.currentFragment.equals(activity.getString(R.string.title_home))) {
            isPopFragment = ((BaseContainerFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getString(R.string.title_home))).popFragment();
        }
        if (Utility.currentFragment.equals(activity.getString(R.string.title_notifications))) {
            isPopFragment = ((BaseContainerFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getString(R.string.title_notifications))).popFragment();
        }
        else if (Utility.currentFragment.equals(activity.getString(R.string.title_favourites))) {
            activity.setToolbarTitle(activity.getString(R.string.title_favourites));
            isPopFragment = ((BaseContainerFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getString(R.string.title_favourites))).popFragment();
        } else if (Utility.currentFragment.equals(activity.getString(R.string.title_setting))) {
            activity.setToolbarTitle(activity.getString(R.string.title_setting));
            isPopFragment = ((BaseContainerFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getString(R.string.title_setting))).popFragment();
        } else if (Utility.currentFragment.equals(activity.getString(R.string.title_invite))) {
            activity.setToolbarTitle(activity.getString(R.string.title_invite));
            isPopFragment = ((BaseContainerFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getString(R.string.title_invite))).popFragment();
        } else if (Utility.currentFragment.equals(activity.getString(R.string.title_about_us))) {
            activity.setToolbarTitle(activity.getString(R.string.title_about_us));
            isPopFragment = ((BaseContainerFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getString(R.string.title_about_us))).popFragment();
        } else if (Utility.currentFragment.equals(activity.getString(R.string.title_tutorial))) {
            activity.setToolbarTitle(activity.getString(R.string.title_tutorial));
            isPopFragment = ((BaseContainerFragment) activity.getSupportFragmentManager().findFragmentByTag(activity.getString(R.string.title_tutorial))).popFragment();
        }
        return isPopFragment;
    }

    @Override
    public void onResult(Uri uriImagePath) {
        webServiceCall.editProfileImg(context, uriImagePath.getPath());
    }

    @Override
    public void onResult(ArrayList<Uri> allUriImagePath) {

    }


    private class ManagerDrawer extends ActionBarDrawerToggle {

        ManagerDrawer(Activity activity, DrawerLayout drawerLayout, Toolbar toolbar, int openDrawerContentDescRes, int closeDrawerContentDescRes) {
            super(activity, drawerLayout, toolbar, openDrawerContentDescRes, closeDrawerContentDescRes);
        }

        @Override
        public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
            Utility.hideKeyboard(((Activity) context));
        }

        @Override
        public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
            super.onDrawerSlide(drawerView, navigationState == 0 ? 1 : 0);
        }

        @Override
        public void onDrawerSlide(View drawerView, float slideOffset) {
            super.onDrawerSlide(drawerView, slideOffset);
            if (!isNavigationIconAnimate) {
                super.onDrawerSlide(drawerView, navigationState == 0 ? 1 : 0);
            }
        }
    }


}
