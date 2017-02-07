package com.checkedin.model;

import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ChatList {
    private String iMessageID;
    private String tMessage;
    private String dCreated;
    private String iUserID;
    private String vFirstName;
    private String vLastName;
    private String totalUnRead;
    private String vImage;
    private long createTimeMillis;

    public String getiMessageID() {
        return iMessageID;
    }

    public void setiMessageID(String iMessageID) {
        this.iMessageID = iMessageID;
    }

    public String gettMessage() {
        return tMessage;
    }

    public void settMessage(String tMessage) {

        this.tMessage = tMessage;
    }

    public String getdCreated() {

        return dCreated;
    }

    public String getFormatedCreateTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(createTimeMillis);
        return Utility.timeAgo(cal.getTimeInMillis(), "dd MMM hh:mm aaa", false);
    }


    public void setCreatedMills(){
        try {
            createTimeMillis = new SimpleDateFormat(Utility.SERVER_MESSAGE_DATE_FORMAT, Locale.getDefault()).parse(dCreated).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void setdCreated(String dCreated) {

        this.dCreated = dCreated;
    }

    public String getiUserID() {
        return iUserID;
    }

    public void setiUserID(String iUserID) {
        this.iUserID = iUserID;
    }

    public String getvFirstName() {
        return vFirstName;
    }

    public void setvFirstName(String vFirstName) {
        this.vFirstName = vFirstName;
    }

    public String getvLastName() {
        return vLastName;
    }

    public void setvLastName(String vLastName) {
        this.vLastName = vLastName;
    }

    public String getTotalUnRead() {
        return totalUnRead;
    }

    public void setTotalUnRead(String totalUnRead) {
        this.totalUnRead = totalUnRead;
    }

    public String getvImage() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.THUMB_SIZE_PATH + vImage;
    }

    public void setvImage(String vImage) {
        this.vImage = vImage;
    }

    public String getFullName() {
        return vFirstName + " " + vLastName;
    }
}