package com.checkedin.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.dialog.AlertPermissionDialog;
import com.checkedin.utility.Utility;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookAuthorizationException;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.checkedin.interfaces.SocialLoginListener;
import com.checkedin.utility.MyPrefs;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BaseActivity extends AppCompatActivity {

    public MyPrefs prefs;
    public View coordinatorLayout;
    public boolean hasFriendRequest, hasActivityAction;
//    public OnNotificationListener notificationListener;
    private MobileDataStateChangedReceiver mobileDataReceiver;
    private PushMessageReceiver pushMessageReceiver;
    private SocialLoginListener listener;
    //    private int ll_container;

//    private PushReceiver pushReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertPermissionDialog.set(this);
        super.setTitle("");
        prefs = MyPrefs.getInstance(this);
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (Build.VERSION.SDK_INT >= 21) {
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
//            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
//        } else {
//            setTheme(R.style.AppTheme);
//        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("tag","BaseActivity.onActivityResult " + requestCode + " " + resultCode + " " + data);
        Utility.logUtils("BaseCalled");
        try {

//            if (requestCode == CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()) {
//            if (resultCode == RESULT_OK) {
                try {
//                    callbackManager.onActivityResult(requestCode, resultCode, data);
                } catch (Exception e) {
                    e.printStackTrace();

                }
//            }
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideSoftKeyboard() {
        hideSoftKeyboard(this.getCurrentFocus());
    }

    public void hideSoftKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // =============================================================================================================
    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter pushFilter = new IntentFilter();
        pushFilter.addAction("com.manup.push");
        pushMessageReceiver = new PushMessageReceiver(this);
        registerReceiver(pushMessageReceiver, pushFilter);

        // register activity to handle push notification receive and click
//        if (getClass() == HomeActivity.class) {
//        try {
//            pushReceiver = new PushReceiver(this);
//            registerReceiver(pushReceiver, PushReceiver.getPushIntentFilter());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        }

        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        mobileDataReceiver = new MobileDataStateChangedReceiver();
        registerReceiver(mobileDataReceiver, filter);

        AlertPermissionDialog.set(this);

        try {
//            if (getIntent().hasExtra("pushmodel")) {
//                ResponsePush push = (ResponsePush) getIntent().getExtras().getSerializable("pushmodel");
//                sendBroadcast(PushReceiver.getPushReceiveIntent(push));
//                sendBroadcast(PushReceiver.getPushClickIntent(push));
//                getIntent().removeExtra("pushmodel");
//                getIntent().getExtras().clear();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (mobileDataReceiver != null) {
                unregisterReceiver(mobileDataReceiver);
            }
        } catch (Exception e) {

        }
        try {
            if (pushMessageReceiver != null) {
                unregisterReceiver(pushMessageReceiver);
            }
        } catch (Exception e) {

        }
        // unregister activity for push notification - receive and click receiver
        try {
//            unregisterReceiver(pushReceiver);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * login using facebook and callback fb information to listener
     *
     * @param listener callback user information
     */
//    public void loginWithFacebook(SocialLoginListener listener) {
//        initFacebook(new FacebookCallBack(listener));
//        this.listener=listener;
//    }


//    private void initFacebook(FacebookCallBack callBack) {
//////        FacebookSdk.sdkInitialize(this);
////        callbackManager = CallbackManager.Factory.create();
//        callbackManager = CallbackManager.Factory.create();
//        ArrayList<String> alPermission = new ArrayList<String>();
//        alPermission.add("email");
////        alPermission.add("user_birthday");
//        alPermission.add("public_profile");
//        alPermission.add("user_friends");
//        LoginManager.getInstance().logInWithReadPermissions(this, alPermission);
//        LoginManager.getInstance().registerCallback(callbackManager, callBack);
//    }


//    public void onPushClick(final ResponsePush pushmodel) {
//        try {
//            if (pushmodel.type.equals(NotificationState.FriendRequestDeclined.value) ||
//                    pushmodel.type.equals(NotificationState.FriendRequestAccepted.value) ||
//                    pushmodel.type.equals(NotificationState.FriendRequestReceived.value)) {
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public void onPushReceive(final ResponsePush pushmodel) {
//        try {
//            String type = pushmodel.type;
//            if (type.equals(NotificationState.FriendRequestReceived.value)) {
//                hasFriendRequest = true;
//                notifiDrawer();
//            } else if (type.equals(NotificationState.ChallengeRequestAccepted.value)) {
//                hasActivityAction = true;
//                notifiDrawer();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    protected interface OnNotificationListener {
//        public void onReceive(ResponsePush pushmodel, View bar);
//
//        public void onClick(ResponsePush pushmodel);
//    }

    /**
     * check that internet connection is connect or disconnect
     */
    public static class NetworkUtil {
        static final int TYPE_WIFI = 1;
        static final int TYPE_MOBILE = 2;
        static final int TYPE_NOT_CONNECTED = 0;

        public static int getConnectivityStatus(Context context) {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            if (null != activeNetwork) {
                if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) return TYPE_WIFI;

                if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) return TYPE_MOBILE;
            }
            return TYPE_NOT_CONNECTED;
        }
    }

//    public class FacebookCallBack implements FacebookCallback<LoginResult> {
//        private SocialLoginListener loginListener;
//
//        public FacebookCallBack(SocialLoginListener listener) {
//            this.loginListener = listener;
//        }
//
//        @Override
//        public void onSuccess(final LoginResult result) {
//            Utility.logUtils("Base Success Called");
//            try {
//                if (loginListener != null) {
//                    if (result.getAccessToken().getUserId() != null) {
////                        loginListener.onSocialLogin(result.getAccessToken().getUserId(), "", "", "", "", "", "");
//                    }
////                    else
//                    {
//                        AccessToken accessToken = AccessToken.getCurrentAccessToken();
//                        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
//                            @Override
//                            public void onCompleted(JSONObject object, GraphResponse response) {
//                                try {
//                                    if (object != null) {
////                                        loginListener.onSocialLogin("123123", "", "", "", "", "", "");
//                                        if (object.has("id")) {
//                                            String id = object.getString("id");
//                                            //                                String[] name = object.getString("name").split(" ");
//                                            String email = "";
//                                            if (object.has("email")) email = object.getString("email");
//
//                                            String dob = "";
////                                        if (object.has("birthday")) dob = object.getString("birthday");
//                                            //                                String image = "https://graph.facebook.com/" + post_id + "/picture?type=large";
//                                            LoginManager.getInstance().logOut();
//                                            if (loginListener != null) {
//                                                loginListener.onSocialLogin(
//                                                        id,
//                                                        "" + result.getAccessToken().getToken(),
//                                                        object.getString("first_name"),
//                                                        object.getString("last_name"),
//                                                        "" + email,
//                                                        "" + object.getString("name"),
//                                                        "" + dob);
//                                            }
//                                        }
//                                    }
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                } catch (Exception e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
//                        Bundle parameters = new Bundle();
//                        parameters.putString("fields", " name, email, gender,  first_name, last_name");
//                        request.setParameters(parameters);
//                        request.executeAsync();
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        @Override
//        public void onCancel() {
//            Utility.logUtils("Base cancel Called");
//            if (loginListener != null) {
//                loginListener.onSocialLogin("", "", "", "", "", "", "");
//            }
//        }
//
//        @Override
//        public void onError(FacebookException e) {
//            Utility.logUtils("Base error Called"+e.getMessage());
//            if (e instanceof FacebookAuthorizationException) {
//                if (AccessToken.getCurrentAccessToken() != null) {
//                    LoginManager.getInstance().logOut();
//                }
//            }
//
//            if (loginListener != null) {
//                loginListener.onSocialLogin("", "", "", "", "", "", "");
//            }
//        }
//    }

    private class MobileDataStateChangedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                int state = NetworkUtil.getConnectivityStatus(context);
                if (state == NetworkUtil.TYPE_WIFI || state == NetworkUtil.TYPE_MOBILE) {
                    AppController.HAS_CONNECTION = true;
                } else if (state == NetworkUtil.TYPE_NOT_CONNECTED) {
                    AppController.HAS_CONNECTION = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class PushMessageReceiver extends BroadcastReceiver {
        BaseActivity activity;

        PushMessageReceiver(BaseActivity activity) {
            this.activity = activity;
        }

        @Override
        public void onReceive(final Context context, Intent intent) {
            try {
//                final ResponsePush pushModel = (ResponsePush) intent.getExtras().getSerializable("pushmodel");


//                    PushBar bar = PushBar.make(findViewById(R.post_id.coordinatorLayout), pushModel, PushBar.LENGTH_LONG);
//                    try {
//                        // Notify Home Activity views
//                        sendBroadcast(PushReceiver.getPushReceiveIntent(pushModel));
//                        try {
//                            bar.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {//handlePush(pushmodel);
////                                act.onPushClick(pushmodel);
//                                    sendBroadcast(PushReceiver.getPushClickIntent(pushModel));
//                                }
//                            });
//                            bar.show();
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
