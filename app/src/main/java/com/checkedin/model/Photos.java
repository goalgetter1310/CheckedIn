package com.checkedin.model;

import com.checkedin.volley.WebServiceCall;
import com.google.gson.annotations.SerializedName;

public class Photos {


    public static final String PROFILE_PATH = "/profile";
    public static final String ACTIVITY_PATH = "/activity";
    public static final String MESSAGE_PATH = "/message";
    public static final String CATEGORY_PATH = "/category/";

    public static final String ORIGINAL_SIZE_PATH = "/original/";
    public static final String THUMB_SIZE_PATH = "/150_150/";

    @SerializedName("iPhotoID")
    private String id;
    @SerializedName("vImage")
    private String imageUrl;
    @SerializedName("vCaption")
    private String caption;
    private boolean isUploaded;


    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getOriginalSizePath() {
        return WebServiceCall.IMAGE_BASE_PATH + ACTIVITY_PATH + ORIGINAL_SIZE_PATH + imageUrl;
    }

    public String getThumbSizePath(String type, String imagePath) {
        return WebServiceCall.IMAGE_BASE_PATH + type + THUMB_SIZE_PATH + imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }
}
