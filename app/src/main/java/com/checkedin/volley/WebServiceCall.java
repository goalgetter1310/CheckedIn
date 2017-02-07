package com.checkedin.volley;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.model.CheckinActivity;
import com.checkedin.model.EducationInfo;
import com.checkedin.model.Friend;
import com.checkedin.model.FutureActivity;
import com.checkedin.model.Message;
import com.checkedin.model.MyActivity;
import com.checkedin.model.Photos;
import com.checkedin.model.Place;
import com.checkedin.model.PostActivity;
import com.checkedin.model.WorkInfo;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;

import org.apache.http.HttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WebServiceCall {

    //   http://ping2world.com/checkin/ws/api/
    //   http://ping2world.com/checkin/assets/front/images

    //   http://checkedin.net.in/checkedin-app/
    //   http://checkedin.net.in/assets/front/images


    //   http://52.36.34.178/index.php/ws/api/
    //   http://52.36.34.178/assets/front/images

    //   Google Api Key AIzaSyBxj0fGEjSFcqq8o-OxwfuOMwnXuL5nYjA
    private static final String WS_BASE_URL = "http://52.36.34.178/index.php/";
    private static final String WS_BASE_URL_HTML = "http://52.36.34.178/";
    public static final String IMAGE_BASE_PATH = "http://52.36.34.178/assets/front/images";
    public static final String WS_SIGNUP = "ws/api/registration";
    public static final String WS_LOGIN = "ws/api/login";
    public static final String WS_LOGIN_FB = "ws/api/social_connect";
    public static final String WS_LOGOUT = "ws/api/logout?iUserID=";
    public static final String WS_FORGOT_PASSWORD = "ws/api/forget_password";
    public static final String WS_SEARCH_FRIEND = "ws/api/searchFriend";
    public static final String WS_SEND_FRIEND_REQUEST = "ws/api/sendFriendRequest";
    public static final String WS_FRIEND_LIST = "ws/api/getFriendList";
    public static final String WS_USER_DETAILS = "ws/api/view_detail?iUserID=";
    public static final String WS_EDIT_PROFILE = "ws/api/edit_profile";
    public static final String WS_FRIEND_REQUEST_LIST = "ws/api/getFriendRequests?";
    public static final String WS_ACCEPT_REJECT_REQUEST = "ws/api/AcceptRejectFriendRequest";
    public static final String WS_POST_ACTIVITY = "ws/api/postActivity";
    public static final String WS_FRIEND_ACTIVITY = "ws/api/getFriendsActivity";
    public static final String WS_COMMENT_LIST = "ws/api/getCommentsDetails";
    //    public static final String WS_SOCIAL_CONNECT = "ws/api/social_connect";
    public static final String WS_POST_COMMENT = "ws/api/postComment";
    public static final String WS_REGISTER_DEVICE_TOKEN = "ws/api/registerDeviceToken";
    public static final String WS_CHAT_LIST = "ws/api/loadMessageList";
    public static final String WS_SEND_MESSAGE = "ws/api/sendMessage";
    public static final String WS_MESSAGES_LIST = "ws/api/getMessages";
    public static final String WS_POST_FUTURE_PLANNING = "ws/api/addFuturePlaninng";
    public static final String WS_ACTIVITY_CATEGORY_LIST = "ws/api/getCategories";
    public static final String WS_PLANNING_CATEGORY_LIST = "ws/api/getFuturePlanningCategories";
    public static final String WS_ADD_SUB_CATEGORY = "ws/api/addSubCategory";
    public static final String WS_USER_TIMELINE = "ws/api/viewUserProfile";
    public static final String WS_ADD_FAVOURITE = "ws/api/addFavourite";
    public static final String WS_REMOVE_FAVOURITE = "ws/api/removeFavourite";
    public static final String WS_FAVOURITE_LIST = "ws/api/getFavouriteList";
    public static final String WS_SERVER_USER_LIST = "ws/api/getLastCheckinData";
    public static final String WS_CHECKED_LOCATION_LIST = "ws/api/getSuggestedPlaces";
    public static final String WS_ADD_LOCATION = "ws/api/addLocation";
    public static final String WS_BLOCK_UNBLOCK_USER = "ws/api/blockUnblockUser";
    public static final String WS_NOTIFICATION_LIST = "ws/api/getNotificationList";
    //    public static final String WS_REMOVE_POST = "ws/api/removePost";
    public static final String WS_PERSONAL_LOCATION_LIST = "ws/api/getPredefinedPlaces";
    public static final String WS_TIMELINE_CHECKIN_LIST = "ws/api/getAllCheckInsOfUser";
    public static final String WS_TIMELINE_ACTIVITY_LIST = "ws/api/getUserActivitiesFiltered";
    public static final String WS_CHANGE_PASSWORD = "ws/api/change_password";
    public static final String WS_ADD_PLANNING_SUB_CATEGORY = "ws/api/addFuturePlanningSubCategory";
    public static final String WS_CLEAR_CHAT = "ws/api/clearConversation";
    public static final String WS_BLOCK_LIST = "ws/api/getBlockedUsersList?iUserID=";
    public static final String WS_PARTICULAR_POST_DETAILS = "ws/api/getPostDetailByPostID";
    public static final String WS_INVITE_BY_EMAIL = "ws/api/inviteByEmail";
    public static final String WS_FUTURE_PLANNING_LIST = "ws/api/getFuturePlanning";
    public static final String WS_NOTIFICATION_COUNT = "ws/api/getUnreadNotificationCount?iUserID=";
    public static final String WS_UNFRIEND = "ws/api/unfriendUser";
    public static final String WS_REPORT_POST = "ws/api/reportPost";
    public static final String WS_ABOUT_US = WS_BASE_URL_HTML + "html/about-us.html";
    public static final String WS_HELP = WS_BASE_URL_HTML + "html/help.html";
    public static final String WS_TC = WS_BASE_URL_HTML + "html/terms-conditions.html";
    public static final String WS_REPORT_PROBLEM = WS_BASE_URL_HTML + "html/report-a-problem.html";
    public static final String WS_APP_OPEN = "ws/api/addPointsForAppOpen";
    public static final String WS_ADD_STAR_VIEW_METER = "ws/api/addStarViewOMeter";
    public static final String WS_STAR_VIEW_USER = "ws/api/getStartOMeterUsers";
    public static final String WS_LAST_CHECKIN_USER = "ws/api/getLastCheckInOfUser";
    public static final String WS_REMOVE_LAST_CHECKIN_USER = "ws/api/removeLastCheckInOfUser";
    public static final String WS_FEEDBACK = "ws/api/addFeedBack";
    public static final String WS_DELETE_PROFILE_INFO = "ws/api/removeUserAdditionalInfo";
    public static final String WS_ACTIVITY_SUB_CATEGORY = "ws/api/getAcitivitySubcategory";
    public static final String WS_EDIT_ACTIVITY_SUB_CATEGORY = "ws/api/editUserDefinedActivitySubcategory";
    public static final String WS_DELETE_ACTIVITY_SUB_CATEGORY = "ws/api/deleteUserDefinedActivitySubcategory";
    public static final String WS_PLAN_SUB_CATEGORY_LIST = "ws/api/getPlanningSubcategory";
    public static final String WS_EDIT_PLAN_SUB_CATEGORY = "ws/api/editUserDefinedPlanningSubcategory";
    public static final String WS_DELETE_PLAN_SUB_CATEGORY = "ws/api/deleteUserDefinedPlanningSubcategory";
    public static final String WS_EDIT_PLACE = "ws/api/editUserDefinedPlace";
    public static final String WS_DELETE_PLACE = "ws/api/deleteUserDefinedPlace";
    public static final String WS_EDIT_POST = "ws/api/editPost";
    public static final String WS_DELETE_POST = "ws/api/deletePost";
    public static final String TUTORIAL_LINK = "https://www.youtube.com/embed/EERkfeD5mEY?rel=0"; //https://www.youtube.com/embed/EERkfeD5mEY https://www.youtube.com/watch?v=EERkfeD5mEY
    public static final String SUGGESTED_FRIENDS="ws/api/suggestFriend";

    public static final String WS_GOOGLE_PLACE_API = "https://maps.googleapis.com/maps/api/place/search/json?key=AIzaSyBxj0fGEjSFcqq8o-OxwfuOMwnXuL5nYjA&rankBy=distance&radius=" + Place.GOOGLE_PLACE_API_RADIUS + "&location=";

    //https://maps.googleapis.com/maps/api/place/search/json?location=23.014395,72.5027664&radius=500&key=AIzaSyBCddOMI-B1GyRJeggZg9-1LB3vXBZt5E8&pagetoken=
    public static final int REQUEST_CODE_SIGNUP = 1;
    public static final int REQUEST_CODE_LOGIN = 2;
    public static final int REQUEST_CODE_LOGOUT = 3;
    public static final int REQUEST_CODE_FORGOT_PASSWORD = 4;
    public static final int REQUEST_CODE_SEARCH_FRIEND = 5;
    public static final int REQUEST_CODE_SEND_FRIEND_REQUEST = 6;
    public static final int REQUEST_CODE_FRIEND_LIST = 7;
    public static final int REQUEST_CODE_USER_DETAILS = 8;
    public static final int REQUEST_CODE_EDIT_PROFILE = 9;
    public static final int REQUEST_CODE_FRIEND_REQUEST_LIST = 10;
    public static final int REQUEST_CODE_ACCEPT_REJECT_REQUEST = 11;
    public static final int REQUEST_CODE_POST_ACTIVITY = 12;
    public static final int REQUEST_CODE_FRIEND_ACTIVITY = 13;
    public static final int REQUEST_CODE_COMMENT_LIST = 14;
    //    public static final int REQUEST_CODE_SOCIAL_CONNECT = 15;
    public static final int REQUEST_CODE_POST_COMMENT = 16;
    public static final int REQUEST_CODE_REGISTER_DEVICE_TOKEN = 17;
    public static final int REQUEST_CODE_CHAT_LIST = 18;
    public static final int REQUEST_CODE_SEND_MESSAGE = 19;
    public static final int REQUEST_CODE_MESSAGES_LIST = 20;
    public static final int REQUEST_CODE_POST_FUTURE_PLANNING = 21;
    public static final int REQUEST_CODE_ACTIVITY_CATEGORY_LIST = 22;
    public static final int REQUEST_CODE_PLANNING_CATEGORY_LIST = 23;
    public static final int REQUEST_CODE_ADD_SUB_CATEGORY = 24;
    public static final int REQUEST_CODE_USER_TIMELINE = 25;
    public static final int REQUEST_CODE_ADD_FAVOURITE = 26;
    public static final int REQUEST_CODE_REMOVE_FAVOURITE = 27;
    public static final int REQUEST_CODE_SERVER_USER_LIST = 28;
    public static final int REQUEST_CODE_CHECKED_LOCATION_LIST = 29;
    public static final int REQUEST_CODE_ADD_LOCATION = 30;
    public static final int REQUEST_CODE_BLOCK_UNBLOCK_USER = 31;
    public static final int REQUEST_CODE_NOTIFICATION_LIST = 32;
    //    public static final int REQUEST_CODE_REMOVE_POST = 33;
    public static final int REQUEST_CODE_PERSONAL_LOCATION_LIST = 34;
    public static final int REQUEST_CODE_TIMELINE_CHECKIN_LIST = 35;
    public static final int REQUEST_CODE_TIMELINE_ACTIVITY_LIST = 36;
    public static final int REQUEST_CODE_CHANGE_PASSWORD = 37;
    public static final int REQUEST_CODE_ADD_PLANNING_SUB_CATEGORY = 38;
    public static final int REQUEST_CODE_CLEAR_CHAT = 39;
    public static final int REQUEST_CODE_BLOCK_LIST = 40;
    public static final int REQUEST_CODE_PARTICULAR_POST_DETAILS = 41;
    public static final int REQUEST_CODE_INVITE_BY_EMAIL = 42;
    public static final int REQUEST_CODE_FUTURE_PLANNING_LIST = 43;
    public static final int REQUEST_CODE_NOTIFICATION_COUNT = 44;
    public static final int REQUEST_CODE_UNFRIEND = 45;
    public static final int REQUEST_CODE_REPORT_POST = 46;
    public static final int REQUEST_CODE_APP_OPEN = 47;
    public static final int REQUEST_CODE_ADD_STAR_VIEW_METER = 48;
    public static final int REQUEST_CODE_STAR_VIEW_USER = 49;
    public static final int REQUEST_CODE_LAST_CHECKIN_USER = 50;
    public static final int REQUEST_CODE_REMOVE_LAST_CHECKIN_USER = 51;
    public static final int REQUEST_CODE_FEEDBACK = 52;
    public static final int REQUEST_CODE_FAVOURITE_LIST = 53;
    public static final int REQUEST_CODE_GOOGLE_PLACE_API = 54;
    public static final int REQUEST_CODE_DELETE_PROFILE_INFO = 55;
    public static final int REQUEST_CODE_ACTIVITY_SUB_CATEGORY = 56;
    public static final int REQUEST_CODE_EDIT_ACTIVITY_SUB_CATEGORY = 57;
    public static final int REQUEST_CODE_DELETE_ACTIVITY_SUB_CATEGORY = 58;
    public static final int REQUEST_CODE_PLAN_SUB_CATEGORY_LIST = 59;
    public static final int REQUEST_CODE_EDIT_PLAN_SUB_CATEGORY = 60;
    public static final int REQUEST_CODE_DELETE_PLAN_SUB_CATEGORY = 61;
    public static final int REQUEST_CODE_EDIT_PLACE = 62;
    public static final int REQUEST_CODE_DELETE_PLACE = 63;
    public static final int REQUEST_CODE_EDIT_POST = 64;
    public static final int REQUEST_CODE_DELETE_POST = 65;
    public static final int REQUEST_CODE_SUGGESTED_FRIENDS=66;
    private VolleyStringRequest volleyStringRequest;

    public VolleyStringRequest volleyRequestInstatnce() {
        return volleyStringRequest;
    }

    public WebServiceCall(VolleyStringRequest.AfterResponse afterResponse) {
        volleyStringRequest = new VolleyStringRequest(afterResponse);
    }

    public void loginWsCall(Context context, String email, String password) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("vEmailOrUsername", email);
            params.put("vPassword", password);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_LOGIN, params, REQUEST_CODE_LOGIN);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }
    public void loginFbWsCall(Context context, String fbId, String token) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("vSocialID", fbId);
            params.put("tDeviceToken", token);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_LOGIN_FB, params, REQUEST_CODE_LOGIN);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void forgotPassWsCall(Context context, String email) {

        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("vEmail", email);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_FORGOT_PASSWORD, params, REQUEST_CODE_FORGOT_PASSWORD);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }

    }

    public void signupWsCall(Context context, String[] user) {

        if (Utility.checkInternetConnectivity(context)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (!TextUtils.isEmpty(user[0])) {
                FileBody fileBody = new FileBody(new File(user[0]));
                builder.addPart("vImage", fileBody);
            }

            builder.addPart("vFirstName", new StringBody(user[1], ContentType.TEXT_PLAIN));
            builder.addPart("vLastName", new StringBody(user[2], ContentType.TEXT_PLAIN));
            builder.addPart("vEmail", new StringBody(user[3], ContentType.TEXT_PLAIN));
            builder.addPart("vPassword", new StringBody(user[4], ContentType.TEXT_PLAIN));

            HttpEntity entity = builder.build();

            volleyStringRequest.volleyRequest(context, WS_BASE_URL + WS_SIGNUP, entity, REQUEST_CODE_SIGNUP);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }

    }

    public void signUpWithSocial(Context context,String image,String id,String firstname,String lastname) {
        if (Utility.checkInternetConnectivity(context)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (!TextUtils.isEmpty(image)) {
                FileBody fileBody = new FileBody(new File(image));
                builder.addPart("vImage", fileBody);
            }

            builder.addPart("vFirstName", new StringBody(firstname, ContentType.TEXT_PLAIN));
            builder.addPart("vLastName", new StringBody(lastname, ContentType.TEXT_PLAIN));
            builder.addPart("vFacebookID", new StringBody(id, ContentType.TEXT_PLAIN));

            HttpEntity entity = builder.build();

            volleyStringRequest.volleyRequest(context, WS_BASE_URL + WS_SIGNUP, entity, REQUEST_CODE_SIGNUP);

        }
    }

    public void deviceTokenWsCall(Context context) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();

            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("tDeviceToken", UserPreferences.getGCMKey(context));

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_REGISTER_DEVICE_TOKEN, params, REQUEST_CODE_REGISTER_DEVICE_TOKEN);
        }
    }

    public void logoutWsCall(Context context) {
        if (Utility.checkInternetConnectivity(context)) {
            String url = WS_BASE_URL + WS_LOGOUT + UserPreferences.fetchUserId(context) + "&ePlatform=Android";
            volleyStringRequest.volleyRequest(context, Request.Method.GET, url, null, REQUEST_CODE_LOGOUT);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }

    }

    public void appOpenWsCall(Context context) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_APP_OPEN, params, REQUEST_CODE_APP_OPEN);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }

    }

    public boolean timelineWsCall(Context context, String friendId) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            if (!UserPreferences.fetchUserId(context).equals(friendId)) {
                params.put("iProfileID", friendId);
            }
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_USER_TIMELINE, params, REQUEST_CODE_USER_TIMELINE);
            return true;
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean sendFriendRequestWsCall(Context context, String friendId) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iRequestFrom", UserPreferences.fetchUserId(context));
            params.put("iRequestTo", friendId);
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_SEND_FRIEND_REQUEST, params, REQUEST_CODE_SEND_FRIEND_REQUEST);
            return true;

        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean addFavouriteWsCall(Context context, String friendId) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iProfileID", friendId);
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_ADD_FAVOURITE, params, REQUEST_CODE_ADD_FAVOURITE);
            return true;

        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public boolean removeFavouriteWsCall(Context context, String friendId) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iProfileID", friendId);
            AppController.getInstance().clearCache();
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_REMOVE_FAVOURITE, params, REQUEST_CODE_REMOVE_FAVOURITE);
            return true;
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public boolean unFriendWsCall(Context context, String relId) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iRelID", relId);
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_UNFRIEND, params, REQUEST_CODE_UNFRIEND);
            return true;
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return false;
        }

    }

    public boolean blockUserWsCall(Context context, String friendId, boolean isBlock) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iFriendID", friendId);
            params.put("eRelationStatus", isBlock ? Friend.RELATION_STATUS_BLOCK : Friend.RELATION_STATUS_UNBLOCK);
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_BLOCK_UNBLOCK_USER, params, REQUEST_CODE_BLOCK_UNBLOCK_USER);
            return true;

        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void addStarViewOMeterWsCall(Context context, String postId, boolean isStar) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iPostID", postId);
            params.put("type", isStar ? "star" : "view");

            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_ADD_STAR_VIEW_METER, params, REQUEST_CODE_ADD_STAR_VIEW_METER);
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }
    }

    public void starViewUserWsCall(Context context, String postId, boolean isStar, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iPostID", postId);
            params.put("offset", String.valueOf(page));
            params.put("type", isStar ? "star" : "view");

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_STAR_VIEW_USER, params, REQUEST_CODE_STAR_VIEW_USER);
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }
    }

    public void lastCheckinUserWsCall(Context context) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));

            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_LAST_CHECKIN_USER, params, REQUEST_CODE_LAST_CHECKIN_USER);
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }
    }

    public void removeLastCheckinUserWsCall(Context context, String placeId) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iPlaceID", placeId);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_REMOVE_LAST_CHECKIN_USER, params, REQUEST_CODE_REMOVE_LAST_CHECKIN_USER);
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }
    }

    public void postDetailsWsCall(Context context, String postId) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iPostID", postId);
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_PARTICULAR_POST_DETAILS, params, REQUEST_CODE_PARTICULAR_POST_DETAILS);

        } else {
            Toast.makeText(context, R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    public void feedbackWsCall(Context context, String title, String description) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("vTitle", title);
            params.put("tDescription", description);
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_FEEDBACK, params, REQUEST_CODE_FEEDBACK);

        } else {
            Toast.makeText(context, R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    public boolean friendListWsCall(Context context, String userId, int page, boolean isNeedSuggestedFriend) {
        return friendListWsCall(context, userId, page, isNeedSuggestedFriend, true);
    }

    public boolean friendListWsCall(Context context, String userId, int page, boolean isNeedSuggestedFriend, boolean showProgress) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("offset", String.valueOf(page));
            params.put("getSuggestedFriend", isNeedSuggestedFriend ? "TRUE" : "FALSE");

            volleyStringRequest.volleyRequest(showProgress ? context : null, Request.Method.POST, WS_BASE_URL + WS_FRIEND_LIST, params, REQUEST_CODE_FRIEND_LIST);
            return true;
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return true;
        }

    }

    public void suggestedFriendsCall(Context context, String contacList,boolean showProgress) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("contactJson",contacList);
            volleyStringRequest.volleyRequest(showProgress ? context : null, Request.Method.POST, WS_BASE_URL + SUGGESTED_FRIENDS,params, REQUEST_CODE_SUGGESTED_FRIENDS);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }

    }
        public void chatListWsCall(Context context, int page, boolean showProgress) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("offset", String.valueOf(page));
            volleyStringRequest.volleyRequest(showProgress ? context : null, Request.Method.POST, WS_BASE_URL + WS_CHAT_LIST, params, REQUEST_CODE_CHAT_LIST);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void getChatMsgWsCall(final Context context, int page, String friendId) {
        getChatMsgWsCall(context, page, friendId, REQUEST_CODE_MESSAGES_LIST, true);
    }

    public void getChatMsgWsCall(final Context context, int page, String friendId, final int requestCode,final boolean showProgress) {
        if (Utility.checkInternetConnectivity(context)) {
            final Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iFriendID", friendId);
            params.put("offset", String.valueOf(page));
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    volleyStringRequest.volleyRequest(showProgress ? context : null, Request.Method.POST, WS_BASE_URL + WS_MESSAGES_LIST, params, requestCode);
                }
            }, 1000);

        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void clearChat(Context context, String friendId) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iFriendID", friendId);
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_CLEAR_CHAT, params, REQUEST_CODE_CLEAR_CHAT);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void favListWsCall(Context context, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("offset", String.valueOf(page));
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_FAVOURITE_LIST, params, REQUEST_CODE_FAVOURITE_LIST);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void friendRequestWsCall(Context context, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            String url = WS_BASE_URL + WS_FRIEND_REQUEST_LIST + "iUserID=" + UserPreferences.fetchUserId(context) + "&offset=" + page;
            volleyStringRequest.volleyRequest(context, Request.Method.GET, url, null, REQUEST_CODE_FRIEND_REQUEST_LIST);

        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }

    }

    public void searchFriendWsCall(Context context, String searchText) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("seachParam", searchText);
            params.put("currentLoggedInUserID", UserPreferences.fetchUserId(context));
            params.put("limit", "20");

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_SEARCH_FRIEND, params, REQUEST_CODE_SEARCH_FRIEND);
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }
    }

    public void checkinWsCall(Context context, CheckinActivity checkinPostActivity, int index, boolean isShowDialog) {
        if (Utility.checkInternetConnectivity(context)) {
            checkinPostActivity.setUrl(WS_BASE_URL + WS_POST_ACTIVITY);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            ArrayList<Photos> alTagPhoto = checkinPostActivity.getAlTagPhoto();
            if (alTagPhoto.size() > 0) {
                FileBody fileBody = new FileBody(new File(alTagPhoto.get(index).getImageUrl()));
                builder.addPart("vImage", fileBody);
                builder.addPart("vCaption", new StringBody(checkinPostActivity.getAlTagPhoto().get(index).getCaption(), ContentType.TEXT_PLAIN));
            }

            if (checkinPostActivity.getTagFriendList() != null && checkinPostActivity.getTagFriendList().size() > 0) {
                for (int counter = 0; counter < checkinPostActivity.getTagFriendList().size(); counter++) {
                    builder.addPart("taggedUsersArray[" + counter + "]", new StringBody(checkinPostActivity.getTagFriendList().get(counter), ContentType.TEXT_PLAIN));
                }
            }
            if (!TextUtils.isEmpty(checkinPostActivity.getId()))
                builder.addPart("iPostID", new StringBody(checkinPostActivity.getId(), ContentType.TEXT_PLAIN));

            String desc = checkinPostActivity.getDescription();
            if (!TextUtils.isEmpty(desc))
                builder.addPart("tActivityDescription", new StringBody(desc, ContentType.TEXT_PLAIN));
            builder.addPart("locationDetail", new StringBody(checkinPostActivity.getLocation(), ContentType.TEXT_PLAIN));
            builder.addPart("iPostType", new StringBody(checkinPostActivity.getType(), ContentType.TEXT_PLAIN));
            builder.addPart("iActivityPrivacy", new StringBody(checkinPostActivity.getPrivacy(), ContentType.TEXT_PLAIN));
            builder.addPart("iUserID", new StringBody(checkinPostActivity.getUserId(context), ContentType.TEXT_PLAIN));
            builder.addPart("iPostPrivacy", new StringBody(checkinPostActivity.getPrivacy(), ContentType.TEXT_PLAIN));

            HttpEntity entity = builder.build();

            volleyStringRequest.volleyRequest(isShowDialog ? context : null, WS_BASE_URL + WS_POST_ACTIVITY, entity, REQUEST_CODE_POST_ACTIVITY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void activityWsCall(Context context, MyActivity myActivity, int index, boolean isShowDialog) {
        if (Utility.checkInternetConnectivity(context)) {
            myActivity.setUrl(WS_BASE_URL + WS_POST_ACTIVITY);
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            ArrayList<Photos> alTagPhoto = myActivity.getAlTagPhoto();
            if (alTagPhoto.size() > 0) {
                FileBody fileBody = new FileBody(new File(alTagPhoto.get(index).getImageUrl()));
                builder.addPart("vImage", fileBody);
                builder.addPart("vCaption", new StringBody(myActivity.getAlTagPhoto().get(index).getCaption(), ContentType.TEXT_PLAIN));
            }
            if (myActivity.getTagFriendList() != null && myActivity.getTagFriendList().size() > 0) {
                for (int counter = 0; counter < myActivity.getTagFriendList().size(); counter++) {
                    builder.addPart("taggedUsersArray[" + counter + "]", new StringBody(myActivity.getTagFriendList().get(counter), ContentType.TEXT_PLAIN));
                }
            }

            String desc = myActivity.getDescription();
            if (!TextUtils.isEmpty(desc))
                builder.addPart("tActivityDescription", new StringBody(desc, ContentType.TEXT_PLAIN));
            if (!TextUtils.isEmpty(myActivity.getId()))
                builder.addPart("iPostID", new StringBody(myActivity.getId(), ContentType.TEXT_PLAIN));
            String subCategoryId = myActivity.getSubCategoryId();
            if (!TextUtils.isEmpty(subCategoryId))
                builder.addPart("iSubcategoryID", new StringBody(subCategoryId, ContentType.TEXT_PLAIN));
            builder.addPart("iCategoryID", new StringBody(myActivity.getCategoryId(), ContentType.TEXT_PLAIN));
            builder.addPart("iPostType", new StringBody(myActivity.getType(), ContentType.TEXT_PLAIN));
            builder.addPart("iActivityPrivacy", new StringBody(myActivity.getPrivacy(), ContentType.TEXT_PLAIN));
            builder.addPart("iUserID", new StringBody(myActivity.getUserId(context), ContentType.TEXT_PLAIN));
            builder.addPart("iPostPrivacy", new StringBody(myActivity.getPrivacy(), ContentType.TEXT_PLAIN));

            HttpEntity entity = builder.build();

            volleyStringRequest.volleyRequest(isShowDialog ? context : null, WS_BASE_URL + WS_POST_ACTIVITY, entity, REQUEST_CODE_POST_ACTIVITY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void planingWsCall(Context context, FutureActivity futureActivity, int index, boolean isShowDialog) {
        if (Utility.checkInternetConnectivity(context)) {

            ArrayList<Photos> alTagPhoto = futureActivity.getAlTagPhoto();

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            if (alTagPhoto.size() > 0) {
                FileBody fileBody = new FileBody(new File(alTagPhoto.get(index).getImageUrl()));
                builder.addPart("vImage", fileBody);

                builder.addPart("vCaption", new StringBody(futureActivity.getAlTagPhoto().get(index).getCaption(), ContentType.TEXT_PLAIN));
            }

            if (TextUtils.isEmpty(futureActivity.getDescription())) {
                Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), "Please write something");
                return;
            }

            builder.addPart("tEventDescription", new StringBody(futureActivity.getDescription(), ContentType.TEXT_PLAIN));
            if (TextUtils.isEmpty(futureActivity.getEventStart())) {
                Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), "Please select date and time");
                return;
            }

            if (futureActivity.getTagFriendList() != null && futureActivity.getTagFriendList().size() > 0) {
                for (int counter = 0; counter < futureActivity.getTagFriendList().size(); counter++) {
                    builder.addPart("taggedUsersArray[" + counter + "]", new StringBody(futureActivity.getTagFriendList().get(counter), ContentType.TEXT_PLAIN));
                }
            }

            if (!TextUtils.isEmpty(futureActivity.getId()))
                builder.addPart("iPostID", new StringBody(futureActivity.getId(), ContentType.TEXT_PLAIN));

            builder.addPart("dEventStart", new StringBody(futureActivity.getEventStart(), ContentType.TEXT_PLAIN));
            builder.addPart("dEventEnd", new StringBody(futureActivity.getEventStart(), ContentType.TEXT_PLAIN));
            builder.addPart("vEventName", new StringBody(futureActivity.getEventName(), ContentType.TEXT_PLAIN));
            builder.addPart("iPostType", new StringBody(futureActivity.getType(), ContentType.TEXT_PLAIN));
            builder.addPart("iActivityPrivacy", new StringBody(futureActivity.getPrivacy(), ContentType.TEXT_PLAIN));
            builder.addPart("iUserID", new StringBody(futureActivity.getUserId(context), ContentType.TEXT_PLAIN));
            builder.addPart("iPostPrivacy", new StringBody(futureActivity.getPrivacy(), ContentType.TEXT_PLAIN));
            builder.addPart("iFpCategoryID", new StringBody(futureActivity.getiFpCategoryID(), ContentType.TEXT_PLAIN));
            if (!TextUtils.isEmpty(futureActivity.getiFpSubcategortID()))
                builder.addPart("iFpSubcategoryID", new StringBody(futureActivity.getiFpSubcategortID(), ContentType.TEXT_PLAIN));

            HttpEntity entity = builder.build();
            volleyStringRequest.volleyRequest(isShowDialog ? context : null, WS_BASE_URL + WS_POST_FUTURE_PLANNING, entity, REQUEST_CODE_POST_FUTURE_PLANNING);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void addLocationWsCall(Context context, String lat, String log, String locationName, boolean isPersonalCheckin) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("vLocationName", locationName);
            params.put("vLatitude", lat);
            params.put("vLongitude", log);
            if (isPersonalCheckin) {
                params.put("iPredefinedBy", UserPreferences.fetchUserId(context));
            }
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_ADD_LOCATION, params, REQUEST_CODE_ADD_LOCATION);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void activityAddSubCategoryWsCall(Context context, String subCategoryName, String categoryId) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();

            params.put("vSubcategoryName", subCategoryName);
            params.put("iCategoryID", categoryId);
            params.put("iUserID", UserPreferences.fetchUserId(context));

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_ADD_SUB_CATEGORY, params, REQUEST_CODE_ADD_SUB_CATEGORY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void planAddSubCategoryWsCall(Context context, String subCategoryName, String categoryId) {

        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("vSubcategoryName", subCategoryName);
            params.put("iFpCategoryID", categoryId);
            params.put("iUserID", UserPreferences.fetchUserId(context));
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_ADD_PLANNING_SUB_CATEGORY, params, REQUEST_CODE_ADD_PLANNING_SUB_CATEGORY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public boolean googlePlaceApiWsCall(Context context, String lat, String lng, String nextPageToekn, boolean isShowProgress) {
        if (Utility.checkInternetConnectivity(context)) {
            String url = TextUtils.isEmpty(nextPageToekn) ? WS_GOOGLE_PLACE_API + lat + "," + lng : WS_GOOGLE_PLACE_API + lat + "," + lng + "&pagetoken=" + nextPageToekn;
            Utility.logUtils("Place Url= " + url);
            volleyStringRequest.volleyRequest(isShowProgress ? context : null, Request.Method.GET, url, null, REQUEST_CODE_GOOGLE_PLACE_API);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
        return false;
    }

    public boolean personalCheckinWsCall(Context context, CheckinActivity checkinPostActivity) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("locationDetail", checkinPostActivity.getLocation());
            params.put("iPostType", PostActivity.POST_TYPE_CHECKIN);
            params.put("iActivityPrivacy", PostActivity.POST_PRIVACY_ONLY_ME);
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iPostPrivacy", PostActivity.POST_PRIVACY_ONLY_ME);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_POST_ACTIVITY, params, REQUEST_CODE_POST_ACTIVITY);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
            return false;
        }
    }

    public boolean checkinPlaceWsCall(Context context, String latitude, String longitude, boolean isShowProgress) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("vLatitude", latitude);
            params.put("vLongitude", longitude);
            params.put("iUserID", UserPreferences.fetchUserId(context));
            volleyStringRequest.volleyRequest(isShowProgress ? context : null, Request.Method.POST, WS_BASE_URL + WS_CHECKED_LOCATION_LIST, params, REQUEST_CODE_CHECKED_LOCATION_LIST);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
            return false;
        }
    }

    public boolean personalPlaceWsCall(Context context, boolean isShowProgress) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            volleyStringRequest.volleyRequest(isShowProgress ? context : null, Request.Method.POST, WS_BASE_URL + WS_PERSONAL_LOCATION_LIST, params, REQUEST_CODE_PERSONAL_LOCATION_LIST);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
            return false;
        }
    }

    public void userDetailsWsCall(Context context, String userId) {
        if (Utility.checkInternetConnectivity(context)) {
            String url = WS_BASE_URL + WS_USER_DETAILS + userId;
            volleyStringRequest.volleyRequest(context, Request.Method.GET, url, null, REQUEST_CODE_USER_DETAILS);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void editUserBasicInfo(Context context, String userId, String[] basicInfo) {
        if (Utility.checkInternetConnectivity(context)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            if (!TextUtils.isEmpty(basicInfo[0])) {
                FileBody fileBody = new FileBody(new File(basicInfo[0]));
                builder.addPart("vImage", fileBody);
            }

            builder.addPart("iUserID", new StringBody(userId, ContentType.TEXT_PLAIN));
            builder.addPart("vProfileSectionType", new StringBody("BasicInfo", ContentType.TEXT_PLAIN));
            builder.addPart("vFirstName", new StringBody(basicInfo[1], ContentType.TEXT_PLAIN));
            builder.addPart("vLastName", new StringBody(basicInfo[2], ContentType.TEXT_PLAIN));
            builder.addPart("eGender", new StringBody(basicInfo[3], ContentType.TEXT_PLAIN));
            builder.addPart("vMaritalStatus", new StringBody(basicInfo[4], ContentType.TEXT_PLAIN));
            builder.addPart("dDOB", new StringBody(basicInfo[5], ContentType.TEXT_PLAIN));
            HttpEntity entity = builder.build();

            volleyStringRequest.volleyRequest(context, WS_BASE_URL + WS_EDIT_PROFILE, entity, REQUEST_CODE_EDIT_PROFILE);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void editProfileImg(Context context, String imageUrl) {
        if (Utility.checkInternetConnectivity(context)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            FileBody fileBody = new FileBody(new File(imageUrl));

            builder.addPart("iUserID", new StringBody(UserPreferences.fetchUserId(context), ContentType.TEXT_PLAIN));
            builder.addPart("vImage", fileBody);
            builder.addPart("vProfileSectionType", new StringBody("BasicInfo", ContentType.TEXT_PLAIN));

            HttpEntity entity = builder.build();
            volleyStringRequest.volleyRequest(context, WS_BASE_URL + WS_EDIT_PROFILE, entity, REQUEST_CODE_EDIT_PROFILE);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void editContactInfo(Context context, String userId, String[] contactInfo) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("vProfileSectionType", "BasicInfo");
            params.put("vEmail", contactInfo[0]);
            params.put("vUserName", contactInfo[1]);
            params.put("vMobileNumber", contactInfo[2]);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_PROFILE, params, REQUEST_CODE_EDIT_PROFILE);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void editLivingPlaceInfo(Context context, String userId, String livingPlace) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("vProfileSectionType", "LocationInfo");
            params.put("locationDetail", livingPlace);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_PROFILE, params, REQUEST_CODE_EDIT_PROFILE);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void editEduInfo(Context context, String userId, EducationInfo educationInfo) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("vProfileSectionType", "EducationInfo");
            if (!TextUtils.isEmpty(educationInfo.getId()))
                params.put("iEducationID", educationInfo.getId());
            params.put("iMstEducationTypeID", String.valueOf(educationInfo.getTypeId()));
            params.put("vName", educationInfo.getName());
            params.put("vFullAddress", educationInfo.getFullAddress());
            params.put("dFromDate", educationInfo.getFromDate());
            params.put("dToDate", educationInfo.getToDate());
            params.put("vLatitude", educationInfo.getLatitude());
            params.put("vLongitude", educationInfo.getLongitude());
            params.put("iIsCompleted", String.valueOf(educationInfo.getCompleted()));

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_PROFILE, params, REQUEST_CODE_EDIT_PROFILE);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void editWorkInfo(Context context, String userId, WorkInfo workInfo) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("vProfileSectionType", "WorkInfo");
            if (!TextUtils.isEmpty(workInfo.getId()))
                params.put("iWorkID", workInfo.getId());
            params.put("vCompanyName", workInfo.getCompanyName());
            params.put("vDesignation", workInfo.getDesignation());
            params.put("vFullAddress", workInfo.getFullAddress());
            params.put("dFromDate", workInfo.getFromDate());
            params.put("vLatitude", workInfo.getLatitude());
            params.put("vLongitude", workInfo.getLongitude());
            params.put("dToDate", workInfo.getToDate());
            params.put("iIsCurrentWorking", String.valueOf(workInfo.getCurrentWorking()));

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_PROFILE, params, REQUEST_CODE_EDIT_PROFILE);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void deleteEduInfo(Context context, String userId, String eduId) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("vProfileSectionType", "EducationInfo");
            params.put("iEducationID", eduId);


            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_DELETE_PROFILE_INFO, params, REQUEST_CODE_DELETE_PROFILE_INFO);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void deleteWorkInfo(Context context, String userId, String workId) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("vProfileSectionType", "WorkInfo");
            params.put("iWorkID", workId);


            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_DELETE_PROFILE_INFO, params, REQUEST_CODE_DELETE_PROFILE_INFO);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void activityCategoryWsCall(Context context, String userId, String friendId) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            if (friendId != null && !friendId.equals(userId))
                params.put("friendID", friendId);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_ACTIVITY_CATEGORY_LIST, params, REQUEST_CODE_ACTIVITY_CATEGORY_LIST);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public boolean activitySubCategoryWSCall(Context context, String userId, String categoryId, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", userId);
            params.put("iCategoryID", categoryId);
            params.put("offset", String.valueOf(page));

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_ACTIVITY_SUB_CATEGORY, params, REQUEST_CODE_ACTIVITY_SUB_CATEGORY);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
            return false;
        }
    }

    public void editActivitySubCategoryWsCall(Context context, String subCategoryId, String subCategoryName) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iSubcategoryID", subCategoryId);
            params.put("vSubcategoryName", subCategoryName);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_ACTIVITY_SUB_CATEGORY, params, REQUEST_CODE_EDIT_ACTIVITY_SUB_CATEGORY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void deleteActivitySubCategoryWsCall(Context context, String subCategoryId) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iSubcategoryID", subCategoryId);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_DELETE_ACTIVITY_SUB_CATEGORY, params, REQUEST_CODE_DELETE_ACTIVITY_SUB_CATEGORY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void planningCategoryWsCall(Context context) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_PLANNING_CATEGORY_LIST, params, REQUEST_CODE_PLANNING_CATEGORY_LIST);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public boolean planSubCategoryWSCall(Context context, String categoryId, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iFpCategoryID", categoryId);
            params.put("offset", String.valueOf(page));

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_PLAN_SUB_CATEGORY_LIST, params, REQUEST_CODE_PLAN_SUB_CATEGORY_LIST);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
            return false;
        }
    }

    public void editPlanSubCategoryWsCall(Context context, String subCategoryId, String subCategoryName) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iFpSubcategoryID", subCategoryId);
            params.put("vSubcategoryName", subCategoryName);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_PLAN_SUB_CATEGORY, params, REQUEST_CODE_EDIT_PLAN_SUB_CATEGORY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void deletePlanSubCategoryWsCall(Context context, String subCategoryId) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iFpSubcategoryID", subCategoryId);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_DELETE_PLAN_SUB_CATEGORY, params, REQUEST_CODE_DELETE_PLAN_SUB_CATEGORY);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void editPlaceWsCall(Context context, String placeId, String placeName) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iPlaceID", placeId);
            params.put("vLocationName", placeName);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_PLACE, params, REQUEST_CODE_EDIT_PLACE);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void deletePlaceWSCall(Context context, String placeId) {
        if (context != null) {
            if (Utility.checkInternetConnectivity(context)) {
                Map<String, String> params = new HashMap<>();
                params.put("iUserID", UserPreferences.fetchUserId(context));
                params.put("iPlaceID", placeId);

                volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_DELETE_PLACE, params, REQUEST_CODE_DELETE_PLACE);
            } else {
                Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
            }
        }
    }

    public void blockListWsCall(Context context, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            String url = WS_BASE_URL + WS_BLOCK_LIST + UserPreferences.fetchUserId(context) + "&offset=" + page;
            volleyStringRequest.volleyRequest(context, Request.Method.GET, url, null, REQUEST_CODE_BLOCK_LIST);

        } else {
            Utility.showSnackBar(((ProfileActivity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void changePassWsCall(Context context, String oldPass, String newPass) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("vOldPassword", oldPass);
            params.put("vNewPassword", newPass);
            AppController.getInstance().clearCache();
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_CHANGE_PASSWORD, params, REQUEST_CODE_CHANGE_PASSWORD);

        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void editPostWsCall(Context context, PostActivity postActivity, boolean isShowDialog) {
        if (Utility.checkInternetConnectivity(context)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            ArrayList<Photos> alPhotos = postActivity.getAlTagPhoto();
            if (alPhotos != null && alPhotos.size() > 0 && TextUtils.isEmpty(alPhotos.get(0).getId())) {
                FileBody fileBody = new FileBody(new File(alPhotos.get(0).getImageUrl()));
                builder.addPart("vImage", fileBody);
            }

            builder.addPart("iPostID", new StringBody(postActivity.getId(), ContentType.TEXT_PLAIN));
            builder.addPart("iUserID", new StringBody(UserPreferences.fetchUserId(context), ContentType.TEXT_PLAIN));
            builder.addPart("iPostType", new StringBody(postActivity.getType(), ContentType.TEXT_PLAIN));
            builder.addPart("iPostPrivacy", new StringBody(postActivity.getPrivacy(), ContentType.TEXT_PLAIN));

            if (postActivity.getTagFriendList() != null && postActivity.getTagFriendList().size() > 0) {
                for (int counter = 0; counter < postActivity.getTagFriendList().size(); counter++) {
                    builder.addPart("taggedUsersArray[" + counter + "]", new StringBody(postActivity.getTagFriendList().get(counter), ContentType.TEXT_PLAIN));
                }
            }

            if (postActivity.getRemovePhotoId() != null && postActivity.getRemovePhotoId().size() > 0) {
                for (int counter = 0; counter < postActivity.getRemovePhotoId().size(); counter++) {
                    builder.addPart("removedPhotoIds[" + counter + "]", new StringBody(postActivity.getRemovePhotoId().get(counter), ContentType.TEXT_PLAIN));
                }
            }

            if (postActivity instanceof CheckinActivity) {
                CheckinActivity checkinActivity = (CheckinActivity) postActivity;
                builder.addPart("checkInText", new StringBody(checkinActivity.getDescription(), ContentType.TEXT_PLAIN));
            } else if (postActivity instanceof MyActivity) {
                MyActivity myActivity = (MyActivity) postActivity;
                builder.addPart("tActivityDescription", new StringBody(myActivity.getDescription(), ContentType.TEXT_PLAIN));
            } else {
                FutureActivity futureActivity = (FutureActivity) postActivity;
                builder.addPart("vEventName", new StringBody(futureActivity.getEventName(), ContentType.TEXT_PLAIN));
                builder.addPart("dEventStart", new StringBody(futureActivity.getEventStart(), ContentType.TEXT_PLAIN));
                builder.addPart("dEventEnd", new StringBody(futureActivity.getEventStart(), ContentType.TEXT_PLAIN));
                builder.addPart("tEventDescription", new StringBody(futureActivity.getDescription(), ContentType.TEXT_PLAIN));
            }


            HttpEntity entity = builder.build();

            volleyStringRequest.volleyRequest(isShowDialog ? context : null, WS_BASE_URL + WS_EDIT_POST, entity, REQUEST_CODE_EDIT_POST);

        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void deletePostWsCall(Context context, String postId, String postType) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iPostID", postId);
            params.put("iPostType", postType);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_DELETE_POST, params, REQUEST_CODE_DELETE_POST);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public void sendValueToServer(Context context, String relationId, String relationStatus) {
        Map<String, String> params = new HashMap<>();
        params.put("iRelID", relationId);
        params.put("eRelationStatus", relationStatus);
        volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_ACCEPT_REJECT_REQUEST, params, REQUEST_CODE_ACCEPT_REJECT_REQUEST);
    }

    public void userPrivacyWsCall(Context context, boolean showMobile) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iMobileNumberPrivacy", showMobile ? "1" : "0");

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_EDIT_PROFILE, params, REQUEST_CODE_DELETE_POST);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }


    public void commentListWsCall(Context context, String postId, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iPostID", postId);
            params.put("offset", String.valueOf(page));
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_COMMENT_LIST, params, REQUEST_CODE_COMMENT_LIST);

        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }
    }

    public void postCommentWsCall(Context context, String postId, String comment) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iPostID", postId);
            params.put("tComment", comment);
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_POST_COMMENT, params, REQUEST_CODE_POST_COMMENT);

        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
        }
    }

    public boolean friendActivityWsCall(Context context, int page) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("offset", String.valueOf(page));

            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_FRIEND_ACTIVITY, params, REQUEST_CODE_FRIEND_ACTIVITY);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
            return false;
        }
    }

    public void notifyCountWsCall(Context context) {
        if (Utility.checkInternetConnectivity(context)) {
            String url = WS_BASE_URL + WS_NOTIFICATION_COUNT + UserPreferences.fetchUserId(context);
            volleyStringRequest.volleyRequest(null, Request.Method.GET, url, null, REQUEST_CODE_NOTIFICATION_COUNT);
        }
    }


    public void sendInvitationByEmailWsCall(Context context, String email) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("vEmail", email);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_INVITE_BY_EMAIL, params, REQUEST_CODE_INVITE_BY_EMAIL);
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

    public boolean notifyListWsCall(Context context, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("offset", String.valueOf(page));
            AppController.getInstance().clearCache();
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_NOTIFICATION_LIST, params, REQUEST_CODE_NOTIFICATION_LIST);
            return true;
        } else {
            Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void reportWsCall(Context context, String postId, int type, String comment) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iPostID", postId);
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iReportType", String.valueOf(type));
            params.put("tComment", comment);

            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_REPORT_POST, params, REQUEST_CODE_REPORT_POST);
        } else {
            Toast.makeText(context, context.getResources().getString(R.string.no_internet_connect), Toast.LENGTH_LONG).show();
        }
    }

    public boolean chatMsgWsCall(Context context, Message message) {
        if (Utility.checkInternetConnectivity(context)) {
            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

            builder.addPart("iMsgFrom", new StringBody(UserPreferences.fetchUserId(context), ContentType.TEXT_PLAIN));
            builder.addPart("iMsgTo", new StringBody(message.getReceiverId(), ContentType.TEXT_PLAIN));
            builder.addPart("tMessage", new StringBody(message.getMsgTextOrImage(), ContentType.TEXT_PLAIN));
            builder.addPart("eMessageType", new StringBody(message.getMsgType(), ContentType.TEXT_PLAIN));
            HttpEntity entity = builder.build();
            AppController.getInstance().clearCache();

            volleyStringRequest.volleyRequest(null, WS_BASE_URL + WS_SEND_MESSAGE, entity, REQUEST_CODE_SEND_MESSAGE);
            return true;
        }
        return false;
    }

    public boolean userActivityWsCall(Context context, String categoryId, String friendId, int page) {
        if (Utility.checkInternetConnectivity(context)) {

            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("iCategoryID", categoryId);
            params.put("offset", String.valueOf(page));
            if (!UserPreferences.fetchUserId(context).equals(friendId)) {
                params.put("friendID", friendId);
            }

            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_TIMELINE_ACTIVITY_LIST, params, REQUEST_CODE_TIMELINE_ACTIVITY_LIST);
            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));

        }
        return false;
    }


    public boolean userCheckinWsCall(Context context, String friendId, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("offset", String.valueOf(page));

            if (!UserPreferences.fetchUserId(context).equals(friendId)) {
                params.put("friendID", friendId);
            }
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_TIMELINE_CHECKIN_LIST, params, REQUEST_CODE_TIMELINE_CHECKIN_LIST);

            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));

        }
        return false;
    }

    public boolean futurePlanningWsCall(Context context, String friendId, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("offset", String.valueOf(page));
            if (!UserPreferences.fetchUserId(context).equals(friendId)) {
                params.put("friendID", friendId);
            }
            volleyStringRequest.volleyRequest(null, Request.Method.POST, WS_BASE_URL + WS_FUTURE_PLANNING_LIST, params, REQUEST_CODE_FUTURE_PLANNING_LIST);

            return true;
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));

        }
        return false;
    }

    public void checkinLoungeFriendWsCall(Context context, String searchParams, int page) {
        if (Utility.checkInternetConnectivity(context)) {
            Map<String, String> params = new HashMap<>();
            params.put("iUserID", UserPreferences.fetchUserId(context));
            params.put("offset", String.valueOf(page));
            if (!TextUtils.isEmpty(searchParams))
                params.put("searchParam", searchParams);
            volleyStringRequest.volleyRequest(context, Request.Method.POST, WS_BASE_URL + WS_SERVER_USER_LIST, params, REQUEST_CODE_SERVER_USER_LIST);

        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.no_internet_connect));
        }
    }

}