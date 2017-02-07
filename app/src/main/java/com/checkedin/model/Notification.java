package com.checkedin.model;


import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Notification {
    public static final String NOTIFY_TYPE_TEXT_MSG = "text_message";
    public static final String NOTIFY_TYPE_IMG_MSG = "img_message";

    public static final String NOTIFY_TYPE_FRIEND_REQUEST = "friend_request";
    public static final String NOTIFY_TYPE_FRIEND_IN_LOUNGE = "server_friend";
    public static final String NOTIFY_TYPE_BIRTHDAY = "birthday";
    public static final String NOTIFY_TYPE_ACCEPT_FRIEND_REQUEST = "friend_req_accepted";
    public static final String NOTIFY_TYPE_CHECKIN_CONFIRM = "askSameCheckInLounge";

    public static final String NOTIFY_TYPE_TAG_ACTIVITY = "taggedin_activity";
    public static final String NOTIFY_TYPE_POST_COMMENT = "posted_comment";
    public static final String NOTIFY_TYPE_OTHER_POST_COMMENT = "other_user_posted_comment";
    public static final String NOTIFY_TYPE_OWNER_COMMENT = "owner_posted_comment";
    public static final String NOTIFY_TYPE_STAR_POST = "star_o_post";

    public static final String NOTIFY_TYPE_FAV_CHECKIN = "favorite_user_posted_checkin";
    public static final String NOTIFY_TYPE_FAV_ACTIVITY = "favorite_user_posted_activity";
    public static final String NOTIFY_TYPE_FAV_PLANNING = "favorite_user_posted_planning";

    private String vType, vMessage, dtCreatedDate, iPostID, vImage;

    public String getvType() {
        return vType;
    }

    public void setvType(String vType) {
        this.vType = vType;
    }

    public String getvMessage() {
        return vMessage;
    }

    public void setvMessage(String vMessage) {
        this.vMessage = vMessage;
    }

    public String getDtCreatedDate() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.SERVER_DATE_FORMAT, Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));
            cal.setTime(simpleDateFormat.parse(dtCreatedDate));
            return Utility.timeAgo(cal.getTimeInMillis(), "dd MMM hh:mm a");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dtCreatedDate;
    }

    public void setDtCreatedDate(String dtCreatedDate) {
        this.dtCreatedDate = dtCreatedDate;
    }

    public String getiPostID() {
        return iPostID;
    }

    public void setiPostID(String iPostID) {
        this.iPostID = iPostID;
    }

    public String getvImage() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.THUMB_SIZE_PATH + vImage;
    }

    public void setvImage(String vImage) {
        this.vImage = vImage;
    }
}
