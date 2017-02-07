package com.checkedin.utility;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.checkedin.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class MyPrefs {

    private static MyPrefs mInstance = null;
    // -----------------------------------------------------------------
    private SharedPreferences myPrefs;
    private SharedPreferences.Editor prefEditor;

    // ------------------------------------------------------------------costructor
    private MyPrefs(Context context) {
//        this.context = context;
        myPrefs = context.getSharedPreferences(context.getString(R.string.app_name), 0);
        prefEditor = myPrefs.edit();
    }

    public static MyPrefs getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new MyPrefs(context);
        }
        return mInstance;
    }

    /**
     * Set a String value in the preferences editor, to be written back.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can
     * chain put calls together.
     */
    public void set(String key, String value) {
        Log.d("tag", "Adding Prefs : " + key + " > " + value);
        prefEditor.putString(key, value);
        prefEditor.commit();
    }

    /**
     * Set a Integer value in the preferences editor, to be written back.
     *
     * @param key   The name of the preference to modify.
     * @param value The new value for the preference.
     * @return Returns a reference to the same Editor object, so you can
     * chain put calls together.
     */
    public void set(String key, int value) {
        Log.d("tag", "Adding Prefs : " + key + " > " + value);
        prefEditor.putInt(key, value);
        prefEditor.commit();
    }

    public void set(keys key, String value) {
        set(key.name(), value);
    }

    public void set(keys key, int value) {
        set(key.name(), value);
    }

    public String get(String key) {
        return myPrefs.getString(key, "");
    }

    public int getInt(String key) {
        return myPrefs.getInt(key, 0);
    }

    public String get(keys key) {
        return get(key.name());
    }

    public int getInt(keys key) {
        return getInt(key.name());
    }


    public void reset() {
        set(keys.USER_ID, "");
        set(keys.CHALLENGEINTRO, "");
        set(keys.ACCESS_TOKEN, "");
        set(keys.FIRSTNAME, "");
        set(keys.LASTNAME, "");
        set(keys.USERNAME, "");
        set(keys.AVATAR, "");
    }

//    public void set(ResponseUserProfile profile) {
//        if (profile != null) {
//            set(keys.FIRSTNAME, "" + profile.firstname);
//            set(keys.LASTNAME, "" + profile.lastname);
//            set(keys.USERNAME, "" + profile.displayName);
//            set(keys.AVATAR, "" + profile.avatarUrl);
//            set(keys.USER_ID, "" + profile.userId);
//        }
//        set(keys.USER, profile);
//    }

    /**
     * return pojo class from preference
     *
     * @param key      The keys enum name or STRING of the preference to modify.
     * @param classOfT The new class reference for the preference.
     * @param <T>      class reference for which type of class we have to return
     * @return class from preference
     */
    public <T> T get(keys key, Class<T> classOfT) {
        return get(key.name(), classOfT);
    }

    public <T> T get(String key, Class<T> classOfT) {
        return new Gson().fromJson(get(key), classOfT);
    }

    /**
     * set pojo class to preference
     *
     * @param key      The keys enum name or STRING of the preference to modify.
     * @param classOfT class reference to store
     */
    public void set(keys key, Object classOfT) {
        set(key.name(), classOfT);
    }

    public void set(String key, Object classOfT) {
        set(key, new Gson().toJson(classOfT));
    }

    public enum keys {
        GCMVERSION, GCMKEY, FEED, LNG, CHALLENGEINTRO, ACCESS_TOKEN, USER_ID, FIRSTNAME, LASTNAME, USERNAME, AVATAR, USER, REGISTRATIONID, TWITTERFRIEND, DISTANCE, REMINDER
//        , EMAIL, PASSWORD, PASSWORDCONFIRM, USERSTATUS, EMPLOYEEID, DOB
    }

    //------------------ Search Challenge and Friend query ----------------------//

    public void addSearchChellange(String value) {
        Set<String> mSearchList = getSearchChellange();
        if (mSearchList.contains(value))
            mSearchList.remove(value);
        mSearchList.add(value);
        prefEditor.putStringSet("SearchChellange", mSearchList);
        prefEditor.commit();
    }

    public Set<String> getSearchChellange() {
        return myPrefs.getStringSet("SearchChellange", new HashSet<String>());
    }

    public void addSearchFriend(String value) {
        Set<String> mSearchList = getSearchFriend();
        Set<String> mSearchListTemp = getSearchFriend();
        if (mSearchList.contains(value))
            mSearchList.remove(value);
        mSearchListTemp.addAll(mSearchList);
        mSearchList.clear();
        mSearchList.add(value);
        mSearchList.addAll(mSearchListTemp);
        prefEditor.putStringSet("SearchFriend", mSearchList);
        prefEditor.commit();
    }

    public Set<String> getSearchFriend() {
        return myPrefs.getStringSet("SearchFriend", new HashSet<String>());
    }


    public void clearSearch() {

        prefEditor.remove("SearchChellange");
        prefEditor.remove("SearchFriend");
        prefEditor.commit();
    }
    //------------------ End : Search Challenge and Friend query ----------------------//
}
