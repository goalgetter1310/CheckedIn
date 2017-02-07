package com.checkedin.model.response;

import com.checkedin.model.UserDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yudiz on 22/07/16.
 */

public class StarViewUserModel extends BaseModel {

    @SerializedName("data")
    private List<UserDetail> starViewUser;

    @SerializedName("totRecords")
    private int totalRecords;

    public List<UserDetail> getStarViewUser() {
        return starViewUser;
    }

    public void setStarViewUser(List<UserDetail> starViewUser) {
        this.starViewUser = starViewUser;
    }

    public int getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
}
