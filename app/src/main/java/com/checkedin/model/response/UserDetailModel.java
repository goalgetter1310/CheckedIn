package com.checkedin.model.response;

import com.checkedin.model.UserDetail;
import com.google.gson.annotations.SerializedName;

public class UserDetailModel extends BaseModel {

    @SerializedName("data")
    private UserDetail userDetails;


    public UserDetail getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetail userDetails) {
        this.userDetails = userDetails;
    }
}
