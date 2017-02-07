package com.checkedin.model;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.ImageView;

import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;
import com.google.gson.annotations.SerializedName;

public class ActivityCategory implements Parcelable {

    @SerializedName("iCategoryID")
    private int id;
    @SerializedName("vCategoryName")
    private String name;
    @SerializedName("vImage")
    private String image;
    @SerializedName("activityCount")
    private int activityCount;
    @SerializedName("isSubCategoryFound")
    private boolean isContainSubCategory;


    protected ActivityCategory(Parcel in) {
        id = in.readInt();
        name = in.readString();
        image = in.readString();
        activityCount = in.readInt();
        isContainSubCategory = in.readByte() != 0;
    }

    public ActivityCategory() {

    }

    public static final Creator<ActivityCategory> CREATOR = new Creator<ActivityCategory>() {
        @Override
        public ActivityCategory createFromParcel(Parcel in) {
            return new ActivityCategory(in);
        }

        @Override
        public ActivityCategory[] newArray(int size) {
            return new ActivityCategory[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeInt(activityCount);
        dest.writeByte((byte) (isContainSubCategory ? 1 : 0));
    }

    @BindingAdapter({"bind:categoryImage"})
    public static void categoryImage(ImageView view, String imageUrl) {
        String image = WebServiceCall.IMAGE_BASE_PATH + Photos.CATEGORY_PATH + imageUrl;
        Utility.loadImageGlide(view, image);
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        switch (name) {
            case "Foodspot":
                return "Eating";
            case "Celebrations":
                return "Celebrating";
            case "Other":
                return "";
            default:
                return name;
        }
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

    public int getActivityCount() {
        return activityCount;
    }

    public String getActivityCountStr() {
        return String.valueOf(activityCount);
    }

    public boolean isActivity() {
        return activityCount > 0;
    }

    public void setActivityCount(int activityCount) {
        this.activityCount = activityCount;
    }

    public boolean isContainSubCategory() {
        return isContainSubCategory;
    }

    public void setContainSubCategory(boolean containSubCategory) {
        isContainSubCategory = containSubCategory;
    }

}
