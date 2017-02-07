package com.checkedin.model.response;

import com.checkedin.model.Friend;
import com.checkedin.model.Place;

import java.util.ArrayList;

public class ServerFriendModel extends BaseModel {
    private Data data;

    public class Data {
        private Place placeData;
        private ArrayList<Friend> userData;
        private int totRecords;

        public Place getPlaceData() {
            return placeData;
        }

        public void setPlaceData(Place placeData) {
            this.placeData = placeData;
        }

        public ArrayList<Friend> getUserData() {
            return userData;
        }

        public void setUserData(ArrayList<Friend> userData) {
            this.userData = userData;
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
