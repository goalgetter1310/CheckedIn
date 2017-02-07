package com.checkedin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yudiz on 30/08/16.
 */

public class ActivitySubCategory implements Parcelable {


    public static final String DEFAULT_SUB_CATEGORY = "1";
    public static final String CREATED_BY_USER_SUB_CATEGORY = "0";


    @SerializedName("iSubcategoryID")
    private int id;
    @SerializedName("iUserID")
    private int userId;
    @SerializedName("iCategoryID")
    private int categoryId;
    @SerializedName("vSubcategoryName")
    private String name;
    @SerializedName("ePredefined")
    private String predefined;
    @SerializedName("iDeleted")
    private String deleted;

    private boolean isActionEvent;

    protected ActivitySubCategory(Parcel in) {
        id = in.readInt();
        userId = in.readInt();
        categoryId = in.readInt();
        name = in.readString();
        predefined = in.readString();
        deleted = in.readString();
    }

    public ActivitySubCategory() {

    }

    public static final Creator<ActivitySubCategory> CREATOR = new Creator<ActivitySubCategory>() {
        @Override
        public ActivitySubCategory createFromParcel(Parcel in) {
            return new ActivitySubCategory(in);
        }

        @Override
        public ActivitySubCategory[] newArray(int size) {
            return new ActivitySubCategory[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPredefined() {
        return predefined;
    }

    public void setPredefined(String predefined) {
        this.predefined = predefined;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(userId);
        dest.writeInt(categoryId);
        dest.writeString(name);
        dest.writeString(predefined);
        dest.writeString(deleted);
    }

    public boolean isActionEvent() {
        return isActionEvent;
    }

    public void setActionEvent(boolean actionEvent) {
        isActionEvent = actionEvent;
    }
}
