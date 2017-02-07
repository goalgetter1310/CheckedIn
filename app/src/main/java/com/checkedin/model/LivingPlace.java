package com.checkedin.model;


import android.support.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

public class LivingPlace implements Cloneable, Comparable<LivingPlace> {

    public final static String LIVING_PLACE_HOME = "HomeTown";
    public final static String LIVING_PLACE_CURRENT = "CurrentTown";

    @SerializedName("iUserLocationID")
    private int id;
    @SerializedName("iUserID")
    private String userId;
    @SerializedName("vFullAddress")
    private String fullAddress;
    @SerializedName("vLatitude")
    private String latitude;
    @SerializedName("vLongitude")
    private String longitude;
    @SerializedName("vLocationType")
    private String type;
    @SerializedName("tUpdatedDate")
    private String updatedDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }


    public String getType() {
        return type;
    }

    public String getDisplayType() {
        return type.equals(LIVING_PLACE_CURRENT) ? "Current City" : "Home Town";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
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

    @Override
    public LivingPlace clone() throws CloneNotSupportedException {
        return (LivingPlace) super.clone();
    }

    @Override
    public int compareTo(@NonNull LivingPlace another) {
        return this.type.compareTo(another.type);
    }
}
