package com.checkedin.model.response;

import com.checkedin.model.FriendsActivity;

import java.util.List;

public class UserPlanningModel extends BaseModel {

    private Data data;

    public class Data {

        private List<FriendsActivity> records;
        private int totRecords;

        public List<FriendsActivity> getRecords() {
            return records;
        }

        public void setRecords(List<FriendsActivity> records) {
            this.records = records;
        }

        public int getTotRecords() {
            return totRecords;
        }

        public void setTotRecords(int totRecords) {
            this.totRecords = totRecords;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
