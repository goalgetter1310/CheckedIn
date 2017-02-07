package com.checkedin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class PlanCategory implements Parcelable {

    @SerializedName("iFpCategoryID")
    private String id;
    @SerializedName("vCategoryName")
    private String name;
    @SerializedName("vImage")
    private String image;
    @SerializedName("isSubCategoryFound")
    private boolean isContainSubCategory;


    public PlanCategory() {

    }


    protected PlanCategory(Parcel in) {
        id = in.readString();
        name = in.readString();
        image = in.readString();
        isContainSubCategory = in.readByte() != 0;
    }

    public static final Creator<PlanCategory> CREATOR = new Creator<PlanCategory>() {
        @Override
        public PlanCategory createFromParcel(Parcel in) {
            return new PlanCategory(in);
        }

        @Override
        public PlanCategory[] newArray(int size) {
            return new PlanCategory[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }


    public boolean isContainSubCategory() {
        return isContainSubCategory;
    }

    public void setContainSubCategory(boolean containSubCategory) {
        isContainSubCategory = containSubCategory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeByte((byte) (isContainSubCategory ? 1 : 0));
    }
}
