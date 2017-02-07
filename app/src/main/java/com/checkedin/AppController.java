package com.checkedin;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.crashlytics.android.Crashlytics;
import com.facebook.FacebookSdk;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import io.fabric.sdk.android.Fabric;

public class AppController extends Application {

    public static final String STRING_RESP = "string_req";
    public static boolean HAS_CONNECTION = false;

    private static AppController mInstance;
    private RequestQueue mRequestQueue;
    private GoogleCloudMessaging gcm;
    private String regid;
    private String SENDER_ID = "308197681931";//129423564775


    @Override
    public void onCreate() {
        super.onCreate();

        getAppKeyHash();
//        if (!SplashActivity.isDeveloperPreview) {
//            Fabric.with(this, new Crashlytics());
//        } else {
//            Fabric.with(this);
//        }
        mInstance = this;
        AnalyticsTrackers.initialize(this);
        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP);
        if (checkPlayServices()) {
            gcm = GoogleCloudMessaging.getInstance(this.getApplicationContext());
            regid = getRegistrationId(this.getApplicationContext());
            if (regid.isEmpty()) {
                registerInBackground();
            } else {
                storeRegistrationId(regid);
            }
        }
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public synchronized Tracker getGoogleAnalyticsTracker() {
        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        t.setScreenName(screenName);
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }


    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    public void clearCache() {
        if (mRequestQueue != null) {
            mRequestQueue.getCache().clear();
        }
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        req.setTag(TextUtils.isEmpty(tag) ? AppController.class.getSimpleName() : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(AppController.class.getSimpleName());
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    /***********
     * GCM
     ***********/

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(AppController.this.getApplicationContext());
        return !(resultCode != ConnectionResult.SUCCESS && resultCode != ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED);
    }

    private String getRegistrationId(Context context) {
        String registrationId = UserPreferences.getGCMKey(context);
        if (registrationId.isEmpty()) {
            return "";
        }
        int registeredVersion = UserPreferences.getGCMRegisterVersion(context);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            return "";
        }

        return registrationId;
    }

    private int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (NameNotFoundException e) {
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
    private String getAppKeyHash() {
        try {
//            Log.d(TAG, "FirebaseInstanceId token: " + FirebaseInstanceId.getInstance().getToken());
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;

                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Tag", "Hash key >>>>> " + something);
                return something;
            }
        } catch (NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
        return null;
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(AppController.this.getApplicationContext());
                    }
                    gcm.unregister();
                    regid = gcm.register(SENDER_ID);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return null;
            }

            protected void onPostExecute(Void result) {
                storeRegistrationId(regid);
            }

            ;
        }.execute();
    }

    private void storeRegistrationId(String regId) {
        Utility.logUtils("Gcm Token" + regId);
        UserPreferences.setGCMKey(getApplicationContext(), regId);
        UserPreferences.setGCMRegisterVersion(getApplicationContext(), getAppVersion(this.getApplicationContext()));
    }

    /********* End GCM *********/
}
