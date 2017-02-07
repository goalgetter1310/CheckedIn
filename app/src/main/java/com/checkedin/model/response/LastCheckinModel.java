package com.checkedin.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yudiz on 23/07/16.
 */

public class LastCheckinModel extends BaseModel {

    @SerializedName("data")
    private LastCheckin lastCheckin;

    public LastCheckin getLastCheckin() {
        return lastCheckin;
    }

    public void setLastCheckin(LastCheckin lastCheckin) {
        this.lastCheckin = lastCheckin;
    }

    public class LastCheckin {
        @SerializedName("iPlaceID")
        private String placeId;
        @SerializedName("dUpdatedDate")
        private String createdTime;

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public String getCreatedTime() {
            return createdTime;
        }

        public void setCreatedTime(String createdTime) {
            this.createdTime = createdTime;
        }
    }


}
