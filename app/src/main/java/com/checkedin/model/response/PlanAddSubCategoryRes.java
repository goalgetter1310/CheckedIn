package com.checkedin.model.response;

import com.checkedin.model.PlanSubCategory;
import com.google.gson.annotations.SerializedName;

public class PlanAddSubCategoryRes extends BaseModel {

    @SerializedName("data")
    private PlanSubCategory planSubCategory;


    public PlanSubCategory getPlanSubCategory() {
        return planSubCategory;
    }

    public void setPlanSubCategory(PlanSubCategory planSubCategory) {
        this.planSubCategory = planSubCategory;
    }
}
