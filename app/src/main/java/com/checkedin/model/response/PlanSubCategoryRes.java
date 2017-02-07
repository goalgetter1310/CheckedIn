package com.checkedin.model.response;

import com.checkedin.model.PlanSubCategory;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yudiz on 30/08/16.
 */

public class PlanSubCategoryRes extends BaseModel {

    @SerializedName("data")
    private List<PlanSubCategory> planSubCategoryList;

    @SerializedName("totRecords")
    private int totRecords;

    public int getTotRecords() {
        return totRecords;
    }

    public void setTotRecords(int totRecords) {
        this.totRecords = totRecords;
    }

    public List<PlanSubCategory> getPlanSubCategoryList() {
        return planSubCategoryList;
    }

    public void setPlanSubCategoryList(List<PlanSubCategory> planSubCategoryList) {
        this.planSubCategoryList = planSubCategoryList;
    }
}
