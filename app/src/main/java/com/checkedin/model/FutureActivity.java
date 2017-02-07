package com.checkedin.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Calendar;

public class FutureActivity extends PostActivity implements Parcelable {
    private String eventName = "future Post", eventStart, description = "-", iFpCategoryID = "", iFpSubcategortID = "", categoryName, subCategoryName;

    private FutureActivity(Parcel in) {
        eventName = in.readString();
        eventStart = in.readString();
        description = in.readString();
        iFpCategoryID = in.readString();
        iFpSubcategortID = in.readString();
        categoryName = in.readString();
        subCategoryName = in.readString();
    }

    public FutureActivity() {

    }

    public static final Creator<FutureActivity> CREATOR = new Creator<FutureActivity>() {
        @Override
        public FutureActivity createFromParcel(Parcel in) {
            return new FutureActivity(in);
        }

        @Override
        public FutureActivity[] newArray(int size) {
            return new FutureActivity[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventStart() {
        return eventStart;
    }

    public void setEventStart(String eventStart) {
        this.eventStart = eventStart;
    }

    public void setEventStart(Calendar eventStart) {
        this.eventStart = eventStart.get(Calendar.YEAR) + "-" + (eventStart.get(Calendar.MONTH) + 1) + "-" + eventStart.get(Calendar.DAY_OF_MONTH) + " " + eventStart.get(Calendar.HOUR_OF_DAY) + ":" + eventStart.get(Calendar.MINUTE) + ":"
                + eventStart.get(Calendar.SECOND);
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setiFpCategoryID(String iFpCategoryID) {
        this.iFpCategoryID = iFpCategoryID;
    }

    public String getiFpCategoryID() {
        return iFpCategoryID;
    }

    public String getiFpSubcategortID() {
        return iFpSubcategortID;
    }

    public void setiFpSubcategortID(String iFpSubcategortID) {
        this.iFpSubcategortID = iFpSubcategortID;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubCategoryName() {
        return subCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        this.subCategoryName = subCategoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(eventName);
        dest.writeString(eventStart);
        dest.writeString(description);
        dest.writeString(iFpCategoryID);
        dest.writeString(iFpSubcategortID);
        dest.writeString(categoryName);
        dest.writeString(subCategoryName);
    }
}
