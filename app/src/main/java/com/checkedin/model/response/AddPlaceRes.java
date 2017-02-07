package com.checkedin.model.response;


import com.google.gson.annotations.SerializedName;

public class AddPlaceRes extends BaseModel {

    @SerializedName("data")
    private PlaceRes placeRes;

    public PlaceRes getPlaceRes() {
        return placeRes;
    }

    public void setPlaceRes(PlaceRes placeRes) {
        this.placeRes = placeRes;
    }

    public class PlaceRes {
        @SerializedName("iPlaceID")
        private String placeId;

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }
    }

}
