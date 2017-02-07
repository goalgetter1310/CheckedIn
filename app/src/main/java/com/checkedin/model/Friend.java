package com.checkedin.model;

import com.google.gson.annotations.SerializedName;

public class Friend extends UserDetail {

    public static final String RELATION_STATUS_ADD_FRIEND = "Add Friend";
    public static final String RELATION_STATUS_FRIEND = "Friend";
    public static final String RELATION_STATUS_DECLINED = "Declined";
    public static final String RELATION_STATUS_BLOCK = "Blocked";
    public static final String RELATION_STATUS_REUEST_SENT = "Pending";
    public static final String RELATION_STATUS_NO_FRIEND = "No Friends";
    public static final String RELATION_STATUS_UNBLOCK = "Unblock";

    public static final String REQUEST_ACCEPTED = "Friend";
    public static final String REQUEST_REJECTED = RELATION_STATUS_DECLINED;

    @SerializedName("iRelID")
    private String relationId;
    @SerializedName("eRelationStatus")
    private String relationStatus;
    @SerializedName("lastCheckin")
    private String lastCheckinTime;
    @SerializedName("isFriend")
    private String isFriend;
    @SerializedName("lastCheckinLocation")
    private String lastCheckinLoc;

    private boolean isSelected;


    public String getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(String relationStatus) {
        this.relationStatus = relationStatus;
    }

    public String getLastCheckinTime() {
        return lastCheckinTime;
    }

    public void setLastCheckinTime(String lastCheckinTime) {
        this.lastCheckinTime = lastCheckinTime;
    }

    public String getLastCheckinLoc() {
        return lastCheckinLoc;
    }

    public void setLastCheckinLoc(String lastCheckinLoc) {
        this.lastCheckinLoc = lastCheckinLoc;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getIsFriend() {
        return isFriend;
    }

    public void setIsFriend(String isFriend) {
        this.isFriend = isFriend;
    }


    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }


}
