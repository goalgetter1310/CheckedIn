package com.checkedin.model;

import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class Comment {


    @SerializedName("id")
    private String id;
    @SerializedName("vFirstName")
    private String firstName;
    @SerializedName("vLastName")
    private String lastName;
    @SerializedName("vImage")
    private String profileImg;
    @SerializedName("tComment")
    private String text;
    @SerializedName("dCreatedDate")
    private String time;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProfileImg() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.THUMB_SIZE_PATH + profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTimeStr() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.SERVER_DATE_FORMAT, Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));
            cal.setTime(simpleDateFormat.parse(time));
            return Utility.timeAgo(cal.getTimeInMillis(), "dd MMM hh:mm a");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }
}
