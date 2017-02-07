package com.checkedin.model.response;

import com.checkedin.model.ActivityCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CategoryModel extends BaseModel {

    @SerializedName("data")
    private List<ActivityCategory> activityCategoryList;

    public List<ActivityCategory> getCategoryList() {
        return activityCategoryList;
    }

    public void setCategoryList(List<ActivityCategory> activityCategoryList) {
        this.activityCategoryList = activityCategoryList;
    }
}
