package com.checkedin.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CheckinActivity extends PostActivity implements Parcelable {
    private String location, lastCheckin, description;
    private Place place;

    protected CheckinActivity(Parcel in) {
        location = in.readString();
        lastCheckin = in.readString();
        description = in.readString();
        place = in.readParcelable(Place.class.getClassLoader());
    }

    public CheckinActivity() {

    }

    public static final Creator<CheckinActivity> CREATOR = new Creator<CheckinActivity>() {
        @Override
        public CheckinActivity createFromParcel(Parcel in) {
            return new CheckinActivity(in);
        }

        @Override
        public CheckinActivity[] newArray(int size) {
            return new CheckinActivity[size];
        }
    };

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setLocation(Place tipPlace) {
        JSONArray jsonArray = new JSONArray();
        if (tipPlace != null) {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("vLocationName", tipPlace.getName());
                jsonObject.put("vStreetAddress1", tipPlace.getStreetAddress1());
                jsonObject.put("vStreetAddress2", tipPlace.getStreetAddress2());
                jsonObject.put("vState", tipPlace.getState());
                jsonObject.put("vCity", tipPlace.getCity());
                jsonObject.put("vCountry", tipPlace.getCountry());
                jsonObject.put("vLatitude", tipPlace.getLatitude());
                jsonObject.put("vLongitude", tipPlace.getLongitude());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        this.location = jsonArray.toString();
    }

    public String getLastCheckin() {
        return lastCheckin;
    }

    public void setLastCheckin(String lastCheckin) {
        this.lastCheckin = lastCheckin;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(lastCheckin);
        dest.writeString(description);
        dest.writeParcelable(place, flags);
    }
}
