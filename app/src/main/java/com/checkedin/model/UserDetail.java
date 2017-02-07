package com.checkedin.model;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.checkedin.R;
import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.util.Comparator;
import java.util.List;

public class UserDetail implements Comparator<UserDetail> {


    @SerializedName("iUserID")
    protected String id = "";
    @SerializedName("vFirstName")
    protected String firstName = "";
    @SerializedName("vLastName")
    protected String lastName = "";
    @SerializedName("vEmail")
    protected String email = "";
    @SerializedName("vImage")
    protected String imageUrl = "";
    @SerializedName("vUserName")
    protected String username = "";
    @SerializedName("dDOB")
    protected String datOfBirth = "";
    @SerializedName("vMaritalStatus")
    protected String maritalStatus = "";
    @SerializedName("vState")
    protected String state = "";
    @SerializedName("vCity")
    protected String city = "";
    @SerializedName("vCountry")
    protected String country = "";
    @SerializedName("vMobileNumber")
    protected String mobileNo = "";
    @SerializedName("eGender")
    protected String gender = "";
    @SerializedName("vOccupation")
    protected String occupation = "";
    @SerializedName("vEducationInstitute")
    protected String eduInst = "";
    @SerializedName("vCompany")
    protected String company = "";
    @SerializedName("tFieldOfInterest")
    protected String fieldOfInterest = "";
    @SerializedName("vFacebookID")
    protected String facebookId = "";
    @SerializedName("vTwitterID")
    protected String twitterId = "";
    @SerializedName("iMobileNumberPrivacy")
    private String showMobile;


    @SerializedName("EducationInfo")
    private List<EducationInfo> educationInfoList;
    @SerializedName("WorkInfo")
    private List<WorkInfo> workInfoList;
    @SerializedName("LocationInfo")
    private List<LivingPlace> livingPlaceInfoList;

    public static final String USER_RELATIONSHIP_SINGLE = "Single";
    public static final String USER_RELATIONSHIP_MARRIED = "Married";
    public static final String USER_RELATIONSHIP_IN_A_RELATION = "In A Relationship";

    public static final String USER_GENDER_MALE = "Male";
    public static final String USER_GENDER_FEMALE = "Female";

    @BindingAdapter("userProfileImg")
    public static void profileImage(ImageView imageView, String url) {
        Utility.loadImageGlide(   imageView,                   url);
//                Glide.with(imageView.getContext()).load(url).error(R.drawable.ic_placeholder).placeholder(R.drawable.ic_placeholder).into(imageView);
    }

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
        return imageUrl;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEduInst() {
        return eduInst;
    }

    public void setEduInst(String eduInst) {
        this.eduInst = eduInst;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getFieldOfInterest() {
        return fieldOfInterest;
    }

    public void setFieldOfInterest(String fieldOfInterest) {
        this.fieldOfInterest = fieldOfInterest;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getTwitterId() {
        return twitterId;
    }

    public void setTwitterId(String twitterId) {
        this.twitterId = twitterId;
    }


    public String getFullImage() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.ORIGINAL_SIZE_PATH + imageUrl;
    }

    public String getThumbImage() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.ORIGINAL_SIZE_PATH + imageUrl;
    }


    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFrom() {
        String from = "";
        if (city != null && city.trim().length() != 0) {
            from = city + " ";
        }
        if (state != null && state.trim().length() != 0) {
            from = from + state + " ";
        }
        if (country != null && country.trim().length() != 0) {
            from = from + country;
        }
        return from;
    }

    @Override
    public int compare(UserDetail lhs, UserDetail rhs) {
        return lhs.getFirstName().compareTo(rhs.getFirstName());
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof UserDetail) {
            UserDetail userDetails = (UserDetail) o;
            return this.id.equals(userDetails.getId());
        }
        return super.equals(o);
    }


    public List<EducationInfo> getEducationInfoList() {
        return educationInfoList;
    }

    public void setEducationInfoList(List<EducationInfo> educationInfoList) {
        this.educationInfoList = educationInfoList;
    }

    public List<WorkInfo> getWorkInfoList() {
        return workInfoList;
    }

    public void setWorkInfoList(List<WorkInfo> workInfoList) {
        this.workInfoList = workInfoList;
    }

    public List<LivingPlace> getLivingPlaceInfoList() {
        return livingPlaceInfoList;
    }

    public void setLivingPlaceInfoList(List<LivingPlace> livingPlaceInfoList) {
        this.livingPlaceInfoList = livingPlaceInfoList;
    }

    public String getLivingPlaceInfo() {

        LivingPlaceRes livingPlaceRes = new LivingPlaceRes();

        LivingPlaceRes.LocationType vLocationType = livingPlaceRes.new LocationType();
        livingPlaceRes.setvLocationType(vLocationType);

        for (LivingPlace livingPlace : getLivingPlaceInfoList()) {
            if (livingPlace.getType().equals(LivingPlace.LIVING_PLACE_HOME)) {
                vLocationType.setHomeTown(vLocationType.new Town());
                vLocationType.getHomeTown().setvFullAddress(livingPlace.getFullAddress());
                vLocationType.getHomeTown().setvLatitude(livingPlace.getLatitude());
                vLocationType.getHomeTown().setvLongitude(livingPlace.getLongitude());
            } else {
                vLocationType.setCurrentTown(vLocationType.new Town());
                vLocationType.getCurrentTown().setvFullAddress(livingPlace.getFullAddress());
                vLocationType.getCurrentTown().setvLatitude(livingPlace.getLatitude());
                vLocationType.getCurrentTown().setvLongitude(livingPlace.getLongitude());
            }
        }
        return new GsonBuilder().create().toJson(livingPlaceRes);

    }

    public boolean isShowMobile() {
        return showMobile.equals("1");
    }

    public String getShowMobile() {
        return showMobile;
    }

    public void setShowMobile(String showMobile) {
        this.showMobile = showMobile;
    }
}
