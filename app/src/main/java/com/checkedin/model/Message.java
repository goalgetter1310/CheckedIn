package com.checkedin.model;

import android.content.Context;
import android.support.annotation.NonNull;

import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Message implements Comparable<Message> {

    public static final String MESSAGE_TYPE_TEXT = "Text";
    public static final String MESSAGE_TYPE_IMAGE = "Image";

    private int msgId;
    private String senderId, receiverId, msgType, msgTextOrImage, createTime, friendImgUrl;
    private long createTimeMillis;

    private static String userId;

    public Message(Context context) {
        userId = UserPreferences.fetchUserId(context);
    }

    public long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMsgTextOrImage() {
        return msgTextOrImage;
    }

    public void setMsgTextOrImage(String msgTextOrImage) {
        this.msgTextOrImage = msgTextOrImage;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public int getMsgId() {
        return msgId;
    }

    public void setMsgId(int msgId) {
        this.msgId = msgId;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public boolean isSelf() {
        return senderId.equals(userId);
    }

    public String getCreateTime() {
        return createTime;
    }

    public String getFormatedCreateTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(createTimeMillis);
        return Utility.timeAgo(cal.getTimeInMillis(), "dd-MMM hh:mm a", false);
    }

    public void setCreateTime(String createTime) {
        try {
            createTimeMillis = new SimpleDateFormat(Utility.SERVER_MESSAGE_DATE_FORMAT, Locale.getDefault()).parse(createTime).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        this.createTime = createTime;
    }

    public String getFriendImgUrl() {
        return friendImgUrl;
    }

    public void setFriendImgUrl(String friendImgUrl) {
        this.friendImgUrl = friendImgUrl;
    }


    @Override
    public int compareTo(@NonNull Message another) {
        Long compareTime = createTimeMillis;
        return compareTime.compareTo(another.getCreateTimeMillis());
    }
}
