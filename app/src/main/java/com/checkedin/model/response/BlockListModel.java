package com.checkedin.model.response;

import com.checkedin.model.Friend;

import java.util.ArrayList;

public class BlockListModel extends BaseModel {
    private ArrayList<Friend> data;
    private int totRecords;

    public ArrayList<Friend> getData() {
        return data;
    }

    public void setData(ArrayList<Friend> data) {
        this.data = data;
    }

    public int getTotRecords() {
        return totRecords;
    }

    public void setTotRecords(int totRecords) {
        this.totRecords = totRecords;
    }
}
