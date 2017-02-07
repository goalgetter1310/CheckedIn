package com.checkedin.model;

import com.checkedin.volley.WebServiceCall;
import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("iUserID")
    private String id = "";
    @SerializedName("vFirstName")
    private String firstName = "";
    @SerializedName("vLastName")
    private String lastName = "";
    @SerializedName("vEmail")
    private String email = "";
    @SerializedName("vImage")
    private String imageUrl = "";
    @SerializedName("vUserName")
    private String username = "";
    @SerializedName("dDOB")
    private String datOfBirth = "";
    @SerializedName("vMaritalStatus")
    private String maritalStatus = "";
    @SerializedName("vState")
    private String state = "";
    @SerializedName("vCity")
    private String city = "";
    @SerializedName("vCountry")
    private String country = "";
    @SerializedName("vMobileNumber")
    private String mobileNo = "";
    @SerializedName("eGender")
    private String gender = "";
    @SerializedName("lastCheckin")
    private String vLastCheckin = "";
    @SerializedName("lastCheckinLocation")
    private String vLastCheckinLocation = "";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageUrl() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.ORIGINAL_SIZE_PATH+imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDatOfBirth() {
        return datOfBirth;
    }

    public void setDatOfBirth(String datOfBirth) {
        this.datOfBirth = datOfBirth;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getvLastCheckin() {
        return vLastCheckin;
    }

    public void setvLastCheckin(String vLastCheckin) {
        this.vLastCheckin = vLastCheckin;
    }

    public String getvLastCheckinLocation() {
        return vLastCheckinLocation;
    }

    public void setvLastCheckinLocation(String vLastCheckinLocation) {
        this.vLastCheckinLocation = vLastCheckinLocation;
    }
}