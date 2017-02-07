package com.checkedin.model.response;


public class NotifyCountModel extends BaseModel {
    private Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public class Data {
        private int unreadNotification;

        public int getUnreadNotification() {
            return unreadNotification;
        }

        public void setUnreadNotification(int unreadNotification) {
            this.unreadNotification = unreadNotification;
        }
    }
}
