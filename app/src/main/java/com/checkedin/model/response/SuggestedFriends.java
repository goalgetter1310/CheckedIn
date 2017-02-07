package com.checkedin.model.response;

import com.checkedin.model.User;
import com.checkedin.model.UserDetail;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestedFriends extends BaseModel {

    @SerializedName("data")
    private List<User> userDetails;


    public List<User> getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(List<User> userDetails) {
        this.userDetails = userDetails;
    }


}
