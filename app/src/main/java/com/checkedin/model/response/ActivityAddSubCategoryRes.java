package com.checkedin.model.response;

import com.checkedin.model.ActivitySubCategory;
import com.google.gson.annotations.SerializedName;

public class ActivityAddSubCategoryRes extends BaseModel {

    @SerializedName("data")
    private ActivitySubCategory activitySubCategory;


    public ActivitySubCategory getActivitySubCategory() {
        return activitySubCategory;
    }

    public void setActivitySubCategory(ActivitySubCategory activitySubCategory) {
        this.activitySubCategory = activitySubCategory;
    }
}
