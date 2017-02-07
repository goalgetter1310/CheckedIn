package com.checkedin.model.response;

import com.checkedin.model.ChatList;

import java.util.List;

public class ChatListModel extends BaseModel {
    private List<ChatList> data;
    private int totRecords;

    public int getTotRecords() {
        return totRecords;
    }

    public void setTotRecords(int totRecords) {
        this.totRecords = totRecords;
    }

    public List<ChatList> getData() {
        return data;
    }

    public void setData(List<ChatList> data) {
        this.data = data;
    }
}
