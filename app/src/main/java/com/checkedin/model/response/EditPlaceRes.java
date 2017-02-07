package com.checkedin.model.response;


import com.checkedin.model.Place;
import com.google.gson.annotations.SerializedName;

public class EditPlaceRes extends BaseModel {

    @SerializedName("data")
    private Place place;


    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
