package com.checkedin.model;


import com.google.gson.annotations.SerializedName;

public class EducationInfo {


    public static final int EDUCATION_TYPE_SCHOOL = 1;
    public static final int EDUCATION_TYPE_UNIVERSITY = 2;


    @SerializedName("iEducationID")
    private String id;
    @SerializedName("iUserID")
    private String userId;
    @SerializedName("iMstEducationTypeID")
    private int typeId;
    @SerializedName("vName")
    private String name;
    @SerializedName("vFullAddress")
    private String fullAddress;
    @SerializedName("vLatitude")
    private String latitude;
    @SerializedName("vLongitude")
    private String longitude;
    @SerializedName("dFromDate")
    private String fromDate;
    @SerializedName("dToDate")
    private String toDate;
    @SerializedName("iIsCompleted")
    private int completed;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }


    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public int getCompleted() {
        return completed;
    }


    public String getTypeStr() {
        return typeId == EDUCATION_TYPE_SCHOOL ? "High School" : "University";
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
