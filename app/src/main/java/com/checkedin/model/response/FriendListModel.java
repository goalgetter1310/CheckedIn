package com.checkedin.model.response;

import android.content.Context;

import com.checkedin.R;
import com.checkedin.model.Friend;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FriendListModel extends BaseModel {
    private List<Friend> data;

    @SerializedName("totRecords")
    private int totalFriends;
    private List<Friend> suggestedFriend;

    public List<Friend> getData() {
        return data;
    }

    public void setData(List<Friend> data) {
        this.data = data;
    }

    public List<Friend> getSuggestedFriend() {
        return suggestedFriend;
    }

    public void setSuggestedFriend(List<Friend> suggestedFriend) {
        this.suggestedFriend = suggestedFriend;
    }

    public int getTotalFriends() {
        return totalFriends;
    }

    public String getTotalFriends(Context context) {
        String strTotalFriends = "";
        if (totalFriends == 1) {
            strTotalFriends = context.getString(R.string.total_friend, totalFriends);
        }else if (totalFriends > 1) {
            strTotalFriends =   context.getString(R.string.total_friends, totalFriends);
        }

        return strTotalFriends;
    }
    public String getTotalRequests(Context context) {
        String strTotalRequests = "";
        if (totalFriends == 1) {
            strTotalRequests = context.getString(R.string.total_request, totalFriends);
        }else if (totalFriends > 1) {
            strTotalRequests =  context.getString(R.string.total_requests, totalFriends);
        }

        return strTotalRequests;
    }

    public void setTotalFriends(int totalFriends) {
        this.totalFriends = totalFriends;
    }
}
