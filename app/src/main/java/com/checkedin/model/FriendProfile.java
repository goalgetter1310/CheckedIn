package com.checkedin.model;

import com.checkedin.volley.WebServiceCall;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FriendProfile {

    @SerializedName("iUserID")
    private String userId;
    @SerializedName("vFirstName")
    private String firstName;
    @SerializedName("vLastName")
    private String lastName;
    @SerializedName("vImage")
    private String profileImg;
    @SerializedName("friendRelation")
    private String friendRelation;
    @SerializedName("isFavourite")
    private String isFavourite;
    @SerializedName("iRelID")
    private String relationId;
    @SerializedName("photos")
    private List<Photos> photosList;
    @SerializedName("iTotalPoints")
    private int checkedinPoint;
    @SerializedName("activityCount")
    private int activityCount;
    @SerializedName("totalCheckin")
    private int checkinCount;
    @SerializedName("futurePlanningCount")
    private int futurePlanningCount;
    @SerializedName("totalChekInPoints")
    private int totalCheckinPoints;
    @SerializedName("totalActivityPoints")
    private int totalActivityPoints;
    @SerializedName("totalPlanningPoints")
    private int totalPlanningPoints;
    @SerializedName("totalAppOpenPoints")
    private int totalAppOpenPoints;
    @SerializedName("totalStarOMeterPoints")
    private int totalPostPoints;

    public int getTotalCheckinPoints() {
        return totalCheckinPoints;
    }

    public void setTotalCheckinPoints(int totalCheckinPoints) {
        this.totalCheckinPoints = totalCheckinPoints;
    }

    public int getTotalActivityPoints() {
        return totalActivityPoints;
    }

    public void setTotalActivityPoints(int totalActivityPoints) {
        this.totalActivityPoints = totalActivityPoints;
    }

    public int getTotalPlanningPoints() {
        return totalPlanningPoints;
    }

    public void setTotalPlanningPoints(int totalPlanningPoints) {
        this.totalPlanningPoints = totalPlanningPoints;
    }

    public int getTotalAppOpenPoints() {
        return totalAppOpenPoints;
    }

    public void setTotalAppOpenPoints(int totalAppOpenPoints) {
        this.totalAppOpenPoints = totalAppOpenPoints;
    }

    public int getTotalPostPoints() {
        return totalPostPoints;
    }

    public void setTotalPostPoints(int totalPostPoints) {
        this.totalPostPoints = totalPostPoints;
    }

    private List<Friend> friendList = new ArrayList<>();


    public String getFullName() {
        return firstName + " " + lastName;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
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
        return profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getIsFavourite() {
        return isFavourite;
    }

    public void setIsFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }

    public String getRelationId() {
        return relationId;
    }

    public void setRelationId(String relationId) {
        this.relationId = relationId;
    }

    public List<Photos> getPhotosList() {
        return photosList;
    }

    public void setPhotosList(List<Photos> photosList) {
        this.photosList = photosList;
    }

    public int getCheckedinPoint() {
        return checkedinPoint;
    }

    public void setCheckedinPoint(int checkedinPoint) {
        this.checkedinPoint = checkedinPoint;
    }

    public int getCheckinCount() {
        return checkinCount;
    }

    public void setCheckinCount(int checkinCount) {
        this.checkinCount = checkinCount;
    }

    public String getvImage() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.ORIGINAL_SIZE_PATH + profileImg;
    }

    public String getFriendRelation() {
        return friendRelation;
    }

    public void setFriendRelation(String friendRelation) {
        this.friendRelation = friendRelation;
    }

    public boolean isFavourite() {
        return isFavourite.equals("Yes");
    }

    public void setFavourite(String isFavourite) {
        this.isFavourite = isFavourite;
    }


    public int getActivityCount() {
        return activityCount;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public int getFuturePlanningCount() {
        return futurePlanningCount;
    }

    public void setFuturePlanningCount(int futurePlanningCount) {
        this.futurePlanningCount = futurePlanningCount;
    }


    public List<Friend> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<Friend> friendList) {
        this.friendList = friendList;
    }
}
