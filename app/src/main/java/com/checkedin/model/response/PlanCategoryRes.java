package com.checkedin.model.response;

import com.checkedin.model.PlanCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlanCategoryRes extends BaseModel {
    @SerializedName("data")
    private List<PlanCategory> planCategoryList;


    public List<PlanCategory> getPlanCategoryList() {
        return planCategoryList;
    }

    public void setPlanCategoryList(List<PlanCategory> planCategoryList) {
        this.planCategoryList = planCategoryList;
    }
}
