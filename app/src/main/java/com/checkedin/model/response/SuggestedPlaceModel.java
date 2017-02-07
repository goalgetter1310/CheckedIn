package com.checkedin.model.response;

import com.checkedin.model.Place;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SuggestedPlaceModel extends BaseModel {

    @SerializedName("data")
    private List<Place> checkinLocationList;

    public List<Place> getCheckinLocationList() {
        return checkinLocationList;
    }

    public void setCheckinLocationList(List<Place> checkinLocationList) {
        this.checkinLocationList = checkinLocationList;
    }
}
