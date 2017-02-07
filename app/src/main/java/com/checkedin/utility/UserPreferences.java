package com.checkedin.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.checkedin.model.CheckinActivity;
import com.checkedin.model.FutureActivity;
import com.checkedin.model.MyActivity;
import com.checkedin.model.Photos;
import com.checkedin.model.PostActivity;
import com.checkedin.model.UserDetail;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public final class UserPreferences {

    private static SharedPreferences checkinPref;

    private static void createPreference(Context context) {
        checkinPref = context.getSharedPreferences("checkin_pref", 0);
    }

//    public static boolean isFistInApp(Context context) {
//        createPreference(context);
//        boolean isFirstTime;
//        isFirstTime = checkinPref.getBoolean("first_time_in_app", true);
//        if (isFirstTime) {
//            SharedPreferences.Editor editPref = checkinPref.edit();
//            editPref.putBoolean("first_time_in_app", false);
//            editPref.apply();
//        }
//        return isFirstTime;
//    }

    public static void saveNotifyType(Context context, String notifyType) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putString("notify_type", notifyType);
        editPref.apply();
        checkinPref = null;
    }

    public static String fetchNotifyType(Context context) {
        if (context == null) return null;
        createPreference(context);
        String notifyType = checkinPref.getString("notify_type", "");
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("notify_type");
        editPref.apply();
        checkinPref = null;
        return notifyType;
    }

    public static void saveLoginPreference(Context context, String email, String password) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putString("login_email", email);
        editPref.putString("login_password", password);
        editPref.apply();
        checkinPref = null;
    }

    public static String[] fetchLoginPreference(Context context) {
        if (context == null) return null;
        createPreference(context);
        String[] loginPreference = {checkinPref.getString("login_email", ""), checkinPref.getString("login_password", "")};
        checkinPref = null;
        return loginPreference;
    }

//	public static boolean isFacebookLogin(Context context) {
//		createPreference(context);
//		return !TextUtils.isEmpty(checkinPref.getString("user_facebook_id", ""));
//	}


    public static void setGCMKey(Context context, String gcmKey) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putString("registration_id", gcmKey);
        editPref.apply();
        checkinPref = null;
    }

    public static String getGCMKey(Context context) {
        if (context == null) return null;
        createPreference(context);
        String regId = checkinPref.getString("registration_id", "");
        checkinPref = null;
        return regId;
    }

    public static void setGCMRegisterVersion(Context context, int version) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putInt("appVersion", version);
        editPref.apply();
        checkinPref = null;
    }

    public static int getGCMRegisterVersion(Context context) {
        createPreference(context);
        int appVersion = checkinPref.getInt("appVersion", Integer.MIN_VALUE);
        checkinPref = null;
        return appVersion;
    }


    public static void saveLastCheckinTime(Context context, long time) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putLong("last_checkin_time", time);
        editPref.apply();
        checkinPref = null;
    }

    public static long fetchLastCheckinTime(Context context) {
        createPreference(context);
        long time = checkinPref.getLong("last_checkin_time", -1);
        checkinPref = null;
        return time;
    }

    public static void removeLastCheckinTime(Context context) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("last_checkin_time");
        editPref.apply();
        checkinPref = null;
    }

    public static void saveFriendConfirmId(Context context, String friendId) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putString("friend_id", friendId);
        editPref.apply();
        checkinPref = null;
    }

    public static String fetchFriendConfirmId(Context context) {
        createPreference(context);
        String friendId = checkinPref.getString("friend_id", "");
        checkinPref = null;
        return friendId;
    }

    public static void saveUserDetails(Context context, UserDetail mUser) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putString("user_id", mUser.getId());
        editPref.putString("user_first_name", mUser.getFirstName());
        editPref.putString("user_last_name", mUser.getLastName());
        editPref.putString("user_email", mUser.getEmail());
        editPref.putString("user_image_url", mUser.getImageUrl());
        editPref.putString("user_username", mUser.getUsername());
        editPref.putString("user_dob", mUser.getDatOfBirth());
        editPref.putString("user_marital_status", mUser.getMaritalStatus());
        editPref.putString("user_state", mUser.getState());
        editPref.putString("user_city", mUser.getCity());
        editPref.putString("user_country", mUser.getCountry());
        editPref.putString("user_mobile_number", mUser.getMobileNo());
        editPref.putString("user_gender", mUser.getGender());
        editPref.putString("user_occupation", mUser.getOccupation());
        editPref.putString("user_edu_Inst", mUser.getEduInst());
        editPref.putString("user_company", mUser.getCompany());
        editPref.putString("user_field_of_interest", mUser.getFieldOfInterest());
        editPref.putString("user_facebook_id", mUser.getFacebookId());
        editPref.putString("user_twitter_id", mUser.getTwitterId());
        editPref.putString("user_mobile_privacy", mUser.getShowMobile());
        editPref.apply();
        checkinPref = null;
    }

    public static UserDetail fetchUserDetails(Context context) {
        createPreference(context);
        UserDetail mUser = new UserDetail();

        mUser.setId(checkinPref.getString("user_id", ""));
        mUser.setFirstName(checkinPref.getString("user_first_name", ""));
        mUser.setLastName(checkinPref.getString("user_last_name", ""));
        mUser.setEmail(checkinPref.getString("user_email", ""));
        mUser.setImageUrl((checkinPref.getString("user_image_url", "")));
        mUser.setUsername((checkinPref.getString("user_username", "")));
        mUser.setDatOfBirth((checkinPref.getString("user_dob", "")));
        mUser.setMaritalStatus((checkinPref.getString("user_marital_status", "")));
        mUser.setState((checkinPref.getString("user_state", "")));
        mUser.setCity((checkinPref.getString("user_city", "")));
        mUser.setCountry((checkinPref.getString("user_country", "")));
        mUser.setMobileNo((checkinPref.getString("user_mobile_number", "")));
        mUser.setGender((checkinPref.getString("user_gender", "")));
        mUser.setOccupation((checkinPref.getString("user_occupation", "")));
        mUser.setEduInst((checkinPref.getString("user_edu_Inst", "")));
        mUser.setCompany((checkinPref.getString("user_company", "")));
        mUser.setFieldOfInterest((checkinPref.getString("user_field_of_interest", "")));
        mUser.setFacebookId(checkinPref.getString("user_facebook_id", ""));
        mUser.setTwitterId(checkinPref.getString("user_twitter_id", ""));
        mUser.setShowMobile(checkinPref.getString("user_mobile_privacy", ""));
        checkinPref = null;
        return mUser;
    }

    public static String fetchUserFullName(Context context) {
        createPreference(context);
        String fullName = checkinPref.getString("user_first_name", null) + " " + checkinPref.getString("user_last_name", null);

        checkinPref = null;
        return fullName;
    }

    public static String fetchUserCity(Context context) {
        createPreference(context);
        String city = checkinPref.getString("user_city", null);

        checkinPref = null;
        return city;
    }

    public static String fetchProfileImageUrl(Context context) {
        createPreference(context);
        String imageUrl = checkinPref.getString("user_image_url", null);
        checkinPref = null;
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.ORIGINAL_SIZE_PATH + imageUrl;
    }

    public static String fetchUserId(Context context) {
        createPreference(context);
        String userId = checkinPref.getString("user_id", null);
        checkinPref = null;
        return userId;
    }

    public static void saveFirstTime(Context context) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putBoolean("first_time", true);
        editPref.apply();
    }

    public static boolean isFirstTime(Context context) {
        createPreference(context);
        boolean firstTime = checkinPref.getBoolean("first_time", false);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("first_time");
        editPref.apply();
        checkinPref = null;
        return firstTime;
    }

    public static void removeFirstTime(Context context) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("first_time");
        editPref.apply();
        checkinPref = null;
    }

    public static void savePostActivity(Context context, PostActivity postActivity) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        ArrayList<Photos> alTagPhoto = postActivity.getAlTagPhoto();
        if (postActivity instanceof CheckinActivity) {
            editPref.putString("location", ((CheckinActivity) postActivity).getLocation());
        } else if (postActivity instanceof FutureActivity) {
            editPref.putString("event_name", ((FutureActivity) postActivity).getEventName());
            editPref.putString("event_start", ((FutureActivity) postActivity).getEventStart());
            editPref.putString("desc", ((FutureActivity) postActivity).getDescription());
            editPref.putString("future_category_id", ((FutureActivity) postActivity).getiFpCategoryID());
            editPref.putString("future_sub_category_id", ((FutureActivity) postActivity).getiFpSubcategortID());
        } else {
            editPref.putString("category_id", ((MyActivity) postActivity).getCategoryId());
            editPref.putString("sub_category_id", ((MyActivity) postActivity).getSubCategoryId());
            editPref.putString("desc", ((MyActivity) postActivity).getDescription());
        }
        editPref.putInt("upload_index", alTagPhoto.size());

        if (postActivity.getTagFriendList() != null) {
            String friendIds = "";
            for (int counter = 0; counter < postActivity.getTagFriendList().size(); counter++) {

                friendIds += postActivity.getTagFriendList().get(counter) + ",";
                if (counter == postActivity.getTagFriendList().size() - 2) {
                    friendIds += ",";
                }
            }

            editPref.putString("tag_friend_ids", friendIds);
        }
        editPref.putString("post_id", postActivity.getId());
        editPref.putString("type", postActivity.getType());
        editPref.putString("privacy", postActivity.getPrivacy());
        editPref.putString("url", postActivity.getUrl());

        for (int counter = 0; counter < alTagPhoto.size(); counter++) {
            editPref.putString("tag_photo_" + counter, alTagPhoto.get(counter).getImageUrl());
            editPref.putString("caption_" + counter, alTagPhoto.get(counter).getCaption());
        }
        editPref.apply();
    }

    public static boolean isImageUploadCompleted(Context context) {
        createPreference(context);
        return checkinPref.getInt("upload_index", 0) == 0;
    }

    public static void removeRemeberMe(Context context) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("login_email");
        editPref.remove("login_password");
        editPref.apply();
        checkinPref = null;
    }

    public static void removePostActivityData(Context context, int index) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("tag_photo_" + index);
        editPref.remove("caption_" + index);
        if (index == 0) {
            editPref.remove("upload_index");
            editPref.remove("location");
            editPref.remove("tag_friend_ids");
            editPref.remove("post_id");
            editPref.remove("event_start");
            editPref.remove("event_name");
            editPref.remove("desc");
            editPref.remove("type");
            editPref.remove("privacy");
            editPref.remove("url");
            editPref.remove("category_id");
            editPref.remove("sub_category_id");
        }
        editPref.apply();
    }

    public static PostActivity fetchPostActivity(Context context) {
        if (context == null) return null;
        createPreference(context);
        int index = checkinPref.getInt("upload_index", 0);
        String type = checkinPref.getString("type", "");
        PostActivity postActivity;
        switch (type) {
            case PostActivity.POST_TYPE_CHECKIN:
                postActivity = new CheckinActivity();
                ((CheckinActivity) postActivity).setLocation(checkinPref.getString("location", ""));
                break;
            case PostActivity.POST_TYPE_PLANNING:
                postActivity = new FutureActivity();
                ((FutureActivity) postActivity).setEventStart(checkinPref.getString("event_start", ""));
                ((FutureActivity) postActivity).setDescription(checkinPref.getString("desc", ""));
                ((FutureActivity) postActivity).setEventName(checkinPref.getString("event_name", ""));
                ((FutureActivity) postActivity).setiFpCategoryID(checkinPref.getString("future_category_id", ""));
                ((FutureActivity) postActivity).setiFpSubcategortID(checkinPref.getString("future_sub_category_id", ""));
                break;
            default:
                postActivity = new MyActivity();
                ((MyActivity) postActivity).setCategoryId(checkinPref.getString("category_id", ""));
                ((MyActivity) postActivity).setDescription(checkinPref.getString("desc", ""));
                ((MyActivity) postActivity).setSubCategoryId(checkinPref.getString("sub_category_id", ""));
                break;
        }

        String friendIds = checkinPref.getString("tag_friend_ids", "");
        String[] friendStr;
        if (!friendIds.equals("")) {
            friendStr = friendIds.split(",");
            ArrayList<String> alFriendId = new ArrayList<>();
            for (String string : friendStr) {
                alFriendId.add(string.replace(",", ""));
            }
            postActivity.setTagFriendList(alFriendId);
        }
        postActivity.setId(checkinPref.getString("post_id", ""));
        postActivity.setType(type);
        postActivity.setPrivacy(checkinPref.getString("privacy", ""));
        postActivity.setUrl(checkinPref.getString("url", ""));

        ArrayList<Photos> alTagPhoto = postActivity.getAlTagPhoto();
        for (int counter = 0; counter < index; counter++) {
            Photos tagPhoto = new Photos();
            tagPhoto.setImageUrl(checkinPref.getString("tag_photo_" + counter, ""));
            tagPhoto.setCaption(checkinPref.getString("caption_" + counter, ""));
            alTagPhoto.add(tagPhoto);
        }
        return postActivity;
    }

    public static void saveChatFriendId(Context context, String[] friendDetials) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putBoolean("live_chat_friend", true);
        editPref.putString("live_chat_friend_id", friendDetials[0]);
        editPref.putString("live_chat_friend_name", friendDetials[1]);
        editPref.putString("live_chat_friend_img", friendDetials[2]);
        editPref.apply();
    }

    public static String[] fetchChatFriendId(Context context) {
        if (context == null) return null;
        createPreference(context);
        String friendDetails[] = null;
        if (checkinPref.getBoolean("live_chat_friend", false)) {
            friendDetails = new String[3];
            friendDetails[0] = checkinPref.getString("live_chat_friend_id", null);
            friendDetails[1] = checkinPref.getString("live_chat_friend_name", null);
            friendDetails[2] = checkinPref.getString("live_chat_friend_img", null);
            SharedPreferences.Editor editPref = checkinPref.edit();
            editPref.remove("live_chat_friend");
            editPref.remove("live_chat_friend_id");
            editPref.remove("live_chat_friend_name");
            editPref.remove("live_chat_friend_img");
            editPref.apply();
        }
        return friendDetails;
    }

    public static void saveLiveChatId(Context context, String liveChatId) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putString("live_chat_user_id", liveChatId);
        editPref.apply();
    }

    public static String fetchLiveChatId(Context context) {
        createPreference(context);
        return checkinPref.getString("live_chat_user_id", null);
    }

    public static void removeLiveChatId(Context context) {
        if (context == null) return;
        createPreference(context);

        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("live_chat_user_id");
        editPref.apply();
    }

    public static boolean isMsgServiceRunning(Context context) {
        createPreference(context);
        return checkinPref.getBoolean("msg_service", false);
    }

    public static void msgServiceRunning(Context context) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.putBoolean("msg_service", true);
        editPref.apply();
    }

    public static void stopMsgService(Context context) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("msg_service");
        editPref.apply();
    }

    public static void removeUserDetails(Context context) {
        if (context == null) return;
        createPreference(context);
        SharedPreferences.Editor editPref = checkinPref.edit();
        editPref.remove("user_id");
        editPref.remove("user_first_name");
        editPref.remove("user_last_name");
        editPref.remove("user_email");
        editPref.remove("user_password");
        editPref.remove("user_image_url");
        editPref.remove("user_username");
        editPref.remove("user_dob");
        editPref.remove("user_marital_status");
        editPref.remove("user_state");
        editPref.remove("user_city");
        editPref.remove("user_country");
        editPref.remove("user_mobile_number");
        editPref.remove("user_gender");
        editPref.remove("user_user_type");
        editPref.remove("user_status");
        editPref.remove("user_created_date");
        editPref.remove("user_updated_date");
        editPref.remove("user_facebook_id");
        editPref.remove("user_twitter_id");
        editPref.putBoolean("first_time_in_app", false);
        editPref.apply();
        checkinPref = null;
    }
}
