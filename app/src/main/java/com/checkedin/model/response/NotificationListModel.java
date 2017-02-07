package com.checkedin.model.response;

import com.checkedin.model.Notification;

import java.util.List;

public class NotificationListModel extends BaseModel {
    private Data data;

    public class Data {
        private List<Notification> notifications;
        private int totalRecords;


        public List<Notification> getNotifications() {
            return notifications;
        }

        public void setNotifications(List<Notification> notifications) {
            this.notifications = notifications;
        }

        public int getTotalRecords() {
            return totalRecords;
        }

        public void setTotalRecords(int totalRecords) {
            this.totalRecords = totalRecords;
        }
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
