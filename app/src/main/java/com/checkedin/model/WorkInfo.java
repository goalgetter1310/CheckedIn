package com.checkedin.model;


import android.text.TextUtils;

import com.checkedin.utility.Utility;
import com.google.gson.annotations.SerializedName;

public class WorkInfo {

    @SerializedName("iWorkID")
    private String id;
    @SerializedName("iUserID")
    private String userId;
    @SerializedName("vCompanyName")
    private String companyName;
    @SerializedName("vDesignation")
    private String designation;
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
    @SerializedName("iIsCurrentWorking")
    private int currentWorking;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
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

    public String getDuartion() {
        String toDate;
        if (this.toDate.equals("Present") || (!TextUtils.isEmpty(fromDate) && TextUtils.isEmpty(this.toDate))) {
            toDate = "Present";
        } else {
            toDate = Utility.parseDate(this.toDate, "yyyy-MM-dd", "dd MMM, yyyy");
        }

        return Utility.parseDate(fromDate, "yyyy-MM-dd", "dd MMM, yyyy") + " - " + toDate;
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

    public int getCurrentWorking() {
        return currentWorking;
    }

    public void setCurrentWorking(int currentWorking) {
        this.currentWorking = currentWorking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
