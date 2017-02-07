package com.checkedin.model;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class Place implements Parcelable {

    @SerializedName("iPlaceID")
    private String id;
    @SerializedName("vLocationName")
    private String name;
    @SerializedName("vStreetAddress1")
    private String streetAddress1 = "";
    @SerializedName("vStreetAddress2")
    private String streetAddress2;
    @SerializedName("vState")
    private String state;
    @SerializedName("vCity")
    private String city;
    @SerializedName("vCountry")
    private String country;
    @SerializedName("vLatitude")
    private double latitude;
    @SerializedName("vLongitude")
    private double longitude;
    private double distance;

    private boolean isGooglePlace;
    private boolean isActionEvent;


    public static final String GOOGLE_PLACE_API_RADIUS = "150"; //in  meters;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    public Place() {

    }

    protected Place(Parcel in) {
        name = in.readString();
        streetAddress1 = in.readString();
        streetAddress2 = in.readString();
        state = in.readString();
        city = in.readString();
        country = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        distance = in.readDouble();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    public double getDistance() {
        return distance;
    }

    public String getDistanceInKm() {
        return String.format(Locale.getDefault(), "%.2f", distance / 1000) + "km";
    }

    public void setDistance(Location location) {
        Location placeLoc = new Location("");
        placeLoc.setLatitude(latitude);
        placeLoc.setLongitude(longitude);

        distance = location.distanceTo(placeLoc);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(streetAddress1);
        dest.writeString(streetAddress2);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeDouble(distance);
    }

    public boolean isGooglePlace() {
        return isGooglePlace;
    }

    public void setGooglePlace(boolean googlePlace) {
        isGooglePlace = googlePlace;
    }

    public boolean isActionEvent() {
        return isActionEvent;
    }

    public void setActionEvent(boolean actionEvent) {
        isActionEvent = actionEvent;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}