package com.checkedin.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.checkedin.volley.WebServiceCall;

public class TagUser implements Parcelable {
    private String iTaggedUserID, vFirstName, vLastName, vImage;

    protected TagUser(Parcel in) {
        iTaggedUserID = in.readString();
        vFirstName = in.readString();
        vLastName = in.readString();
        vImage = in.readString();
    }

    public static final Creator<TagUser> CREATOR = new Creator<TagUser>() {
        @Override
        public TagUser createFromParcel(Parcel in) {
            return new TagUser(in);
        }

        @Override
        public TagUser[] newArray(int size) {
            return new TagUser[size];
        }
    };

    public TagUser() {
    }

    public String getiTaggedUserID() {
        return iTaggedUserID;
    }

    public void setiTaggedUserID(String iTaggedUserID) {
        this.iTaggedUserID = iTaggedUserID;
    }

    public String getvFirstName() {
        return vFirstName;
    }

    public void setvFirstName(String vFirstName) {
        this.vFirstName = vFirstName;
    }

    public String getvLastName() {
        return vLastName;
    }

    public String getFullName() {
        return vFirstName + " " + vLastName;
    }

    public void setvLastName(String vLastName) {
        this.vLastName = vLastName;
    }

    public String getvImage() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.THUMB_SIZE_PATH + vImage;
    }

    public void setvImage(String vImage) {
        this.vImage = vImage;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(iTaggedUserID);
        dest.writeString(vFirstName);
        dest.writeString(vLastName);
        dest.writeString(vImage);
    }
}
