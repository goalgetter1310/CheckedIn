package com.checkedin.model.response;

import com.checkedin.model.ActivitySubCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yudiz on 30/08/16.
 */

public class ActivitySubCategoryRes extends BaseModel {

    @SerializedName("data")
    private List<ActivitySubCategory> activitySubCategoryList;

    @SerializedName("totRecords")
    private int totRecords;

    public int getTotRecords() {
        return totRecords;
    }

    public void setTotRecords(int totRecords) {
        this.totRecords = totRecords;
    }
    public List<ActivitySubCategory> getActivitySubCategoryList() {
        return activitySubCategoryList;
    }

    public void setActivitySubCategoryList(List<ActivitySubCategory> activitySubCategoryList) {
        this.activitySubCategoryList = activitySubCategoryList;
    }
}
