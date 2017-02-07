package com.checkedin.model;

import android.util.Log;

import com.checkedin.utility.Utility;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MessageChatRoom implements Comparable<MessageChatRoom> {

    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;

    private int mType;
    @SerializedName("iMessageID")
    @Expose
    private Integer iMessageID;
    @SerializedName("vSocketID")
    @Expose
    private String vSocketID;
    @SerializedName("tMessage")
    @Expose
    private String tMessage;
    @SerializedName("dCreatedDate")
    @Expose
    private String tUpdateDate;
    @SerializedName("iRoomID")
    @Expose
    private Integer iRoomID;
    @SerializedName("iUserID")
    @Expose
    private Integer iUserID;
    @SerializedName("vFirstName")
    @Expose
    private String vFirstName;
    @SerializedName("vLastName")
    @Expose
    private String vLastName;
    private String createTime;
    private long createTimeMillis;

    public MessageChatRoom() {
    }

    public Long getCreateTimeMillis() {
        return createTimeMillis;
    }

    public boolean isSelf(Integer userId) {
        return iUserID.equals(userId);
    }

    public int getType() {
        return mType;
    }

    public Integer getIMessageID() {
        return iMessageID;
    }

    public void setIMessageID(Integer iMessageID) {
        this.iMessageID = iMessageID;
    }

    public String getVSocketID() {
        return vSocketID;
    }

    public void setVSocketID(String vSocketID) {
        this.vSocketID = vSocketID;
    }

    public String getTMessage() {
        return tMessage;
    }

    public void setTMessage(String tMessage) {
        this.tMessage = tMessage;
    }

    public String getTUpdateDate() {
        return tUpdateDate;
    }

    public void setTUpdateDate(String tUpdateDate) {
        this.tUpdateDate = tUpdateDate;
    }

    public Integer getIRoomID() {
        return iRoomID;
    }

    public void setIRoomID(Integer iRoomID) {
        this.iRoomID = iRoomID;
    }

    public Integer getIUserID() {
        return iUserID;
    }

    public void setIUserID(Integer iUserID) {
        this.iUserID = iUserID;
    }

    public String getVFirstName() {
        return vFirstName;
    }

    public void setVFirstName(String vFirstName) {
        this.vFirstName = vFirstName;
    }

    public String getVLastName() {
        return vLastName;
    }

    public void setVLastName(String vLastName) {
        this.vLastName = vLastName;
    }

    public String getFormatedCreateTime() {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(createTimeMillis);
        return Utility.timeAgo(cal.getTimeInMillis(), "dd-MMM hh:mm a", false);
    }


    public String setCreateTime(String givenDateString) {
        long timeInMilliseconds = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date mDate = sdf.parse(givenDateString);
            timeInMilliseconds = mDate.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String updatedDate = "";
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM,yyyy hh:mm a");
        sdf.setTimeZone(TimeZone.getDefault());
        updatedDate = sdf.format(new Date(timeInMilliseconds));
        return updatedDate;
    }

//    public void setCreateTime(String createTime) {
//        try {
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss.SSS");
//            df.setTimeZone(TimeZone.getTimeZone("UTC"));
//            Date date = df.parse(createTime);
//            df.setTimeZone(TimeZone.getDefault());
//            String formattedDate = df.format(date);
//            createTimeMillis = new SimpleDateFormat(Utility.SERVER_MESSAGE_DATE_FORMAT, Locale.getDefault()).parse(formattedDate).getTime();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        this.createTime = createTime;
//    }

    @Override
    public int compareTo(MessageChatRoom another) {
        Long compareTime = createTimeMillis;
        return another.getCreateTimeMillis().compareTo(createTimeMillis);
    }

//    public static class Builder {
//        private final int mType;
//        private Integer iMessageID;
//        private String vSocketID;
//        private String tMessage;
//        private String tUpdateDate;
//        private Integer iRoomID;
//        private Integer iUserID;
//        private String vFirstName;
//        private String vLastName;
//
//
//        public Builder(int type) {
//            mType = type;
//        }
//
//        public Builder iMessageID(int iMessageID) {
//            iMessageID = iMessageID;
//            return this;
//        }
//
//        public Builder iRoomID(int iRoomID) {
//            iRoomID = iRoomID;
//            return this;
//        }
//
//        public Builder iUserID(int iUserID) {
//            iUserID = iUserID;
//            return this;
//        }
//
//        public Builder vSocketID(String vSocketID) {
//            vSocketID = vSocketID;
//            return this;
//        }
//
//        public Builder tMessage(String tMessage) {
//            tMessage = tMessage;
//            return this;
//        }
//
//        public Builder tUpdateDate(String tUpdateDate) {
//            tUpdateDate = tUpdateDate;
//            return this;
//        }
//
//        public Builder vFirstName(String vFirstName) {
//            vFirstName = vFirstName;
//            return this;
//        }
//
//        public Builder vLastName(String vLastName) {
//            vLastName = vLastName;
//            return this;
//        }
//
//        public Message build() {
//
//            Message message = new Message();
//            message.mType = mType;
//            message.iMessageID = iMessageID;
//            message.vSocketID = vSocketID;
//            message.tMessage = tMessage;
//            message.tUpdateDate = tUpdateDate;
//            message.iRoomID = iRoomID;
//            message.iUserID = iUserID;
//            message.vFirstName = vFirstName;
//            message.vLastName = vLastName;
//            return message;
//        }
//    }
}
