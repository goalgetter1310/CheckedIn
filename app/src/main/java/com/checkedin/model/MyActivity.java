package com.checkedin.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MyActivity extends PostActivity implements Parcelable, Cloneable {

    private String categoryId, subCategoryId, categoryName, subCategoryName, description = "-";

    protected MyActivity(Parcel in) {
        categoryId = in.readString();
        subCategoryId = in.readString();
        description = in.readString();
    }

    public MyActivity() {

    }

    public static final Creator<MyActivity> CREATOR = new Creator<MyActivity>() {
        @Override
        public MyActivity createFromParcel(Parcel in) {
            return new MyActivity(in);
        }

        @Override
        public MyActivity[] newArray(int size) {
            return new MyActivity[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getSubCategoryId() {
        return subCategoryId;
    }

    public void setSubCategoryId(String subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(categoryId);
        dest.writeString(subCategoryId);
        dest.writeString(description);
    }

    public String getCategoryDisplayName() {
        switch (categoryName) {
            case "Foodspot":
                return "Eating";
            case "Celebrations":
                return "Celebrating";
            case "Other":
                return "";
            default:
                return categoryName;
        }
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
