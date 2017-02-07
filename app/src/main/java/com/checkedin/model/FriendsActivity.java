package com.checkedin.model;

import android.databinding.BindingAdapter;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Html;
import android.text.TextUtils;
import android.widget.ImageView;

import com.checkedin.R;
import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class FriendsActivity implements Parcelable {


    @SerializedName("iPostID")
    private String postId;
    @SerializedName("iPostType")
    private String postType;
    @SerializedName("iPostPrivacy")
    private String postPrivacy;
    @SerializedName("iUserID")
    private String userId;
    @SerializedName("vFirstName")
    private String firstName;
    @SerializedName("vLastName")
    private String lastName;
    @SerializedName("lastCheckin")
    private String lastCheckin;
    @SerializedName("vImage")
    private String profileImg;
    @SerializedName("tActivityDescription")
    private String postDescription;
    @SerializedName("dCreatedDate")
    private String postDate;
    @SerializedName("iPlaceID")
    private String locationId;
    @SerializedName("vLocationName")
    private String locationName;
    @SerializedName("vCity")
    private String city;
    @SerializedName("vCountry")
    private String country;
    @SerializedName("vLatitude")
    private String latitude;
    @SerializedName("vLongitude")
    private String longitude;
    @SerializedName("vCategoryName")
    private String categoryName;
    @SerializedName("vSubcategoryName")
    private String subcategoryName;
    @SerializedName("comments")
    private int commentCount;
    @SerializedName("tEventDescription")
    private String planningPostDescription;
    @SerializedName("dEventStart")
    private String planningPostDate;
    @SerializedName("taggedusers")
    private List<TagUser> tagUsers;
    @SerializedName("photos")
    private List<Photos> tagPhotos;
    @SerializedName("isStarOMeter")
    private int starByme;
    @SerializedName("iStarOMeterCount")
    private int starOMeterCount;

    private ArrayList<WordAction> alWordAction = new ArrayList<>();


    public FriendsActivity() {

    }

    protected FriendsActivity(Parcel in) {
        postId = in.readString();
        postType = in.readString();
        postPrivacy = in.readString();
        userId = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        lastCheckin = in.readString();
        profileImg = in.readString();
        postDescription = in.readString();
        postDate = in.readString();
        locationId = in.readString();
        locationName = in.readString();
        city = in.readString();
        country = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        categoryName = in.readString();
        subcategoryName = in.readString();
        commentCount = in.readInt();
        planningPostDescription = in.readString();
        planningPostDate = in.readString();
        starByme = in.readInt();
        starOMeterCount = in.readInt();
    }

    public static final Creator<FriendsActivity> CREATOR = new Creator<FriendsActivity>() {
        @Override
        public FriendsActivity createFromParcel(Parcel in) {
            return new FriendsActivity(in);
        }

        @Override
        public FriendsActivity[] newArray(int size) {
            return new FriendsActivity[size];
        }
    };

    @BindingAdapter({"bind:postImg"})
    public static void profileImage(ImageView imageView, String imgUrl) {
        Utility.loadImageGlide(imageView, imgUrl);
//        Glide.with(imageView.getContext()).load(imgUrl).error(R.drawable.ic_placeholder).into(imageView);
    }

    @BindingAdapter({"bind:starOMeterByMe"})
    public static void starOMeter(com.material.widget.TextView textView, boolean value) {
        textView.setCompoundDrawablesWithIntrinsicBounds(value ? R.drawable.ic_star_plus_orange_20dp : R.drawable.ic_star_plus_grey_20dp, 0, 0, 0);
    }

    public ArrayList<WordAction> getAlWordAction() {
        return alWordAction;
    }

    public boolean getStarByme() {
        return starByme != 0;
    }

    public void setStarByme(int starByme) {
        this.starByme = starByme;
    }


    public int getStarOMeterCount() {
        return starOMeterCount;
    }

    public String getStarOMeterCountStr() {
        return starOMeterCount + " star";
    }

    public void setStarOMeterCount(int starOMeterCount) {
        this.starOMeterCount = starOMeterCount;
    }


    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostType() {
        return postType;
    }

    public void setPostType(String postType) {
        this.postType = postType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getLastCheckin() {
        return lastCheckin != null && lastCheckin.equals("Yes");
    }

    public void setLastCheckin(String lastCheckin) {
        this.lastCheckin = lastCheckin;
    }

    public String getProfileImg() {
        return WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.THUMB_SIZE_PATH + profileImg;
    }

    public void setProfileImg(String profileImg) {
        this.profileImg = profileImg;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
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

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getSubcategoryName() {
        return subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }


    public String getPlanningPostDescription() {
        return planningPostDescription;
    }

    public void setPlanningPostDescription(String planningPostDescription) {
        this.planningPostDescription = planningPostDescription;
    }

    public String getPlanningPostDate() {
        return planningPostDate;
    }

    public void setPlanningPostDate(String planningPostDate) {
        this.planningPostDate = planningPostDate;
    }

    public List<TagUser> getTagUsers() {
        return tagUsers;
    }

    public void setTagUsers(List<TagUser> tagUsers) {
        this.tagUsers = tagUsers;
    }

    public List<Photos> getTagPhotos() {
        return tagPhotos;
    }

    public void setTagPhotos(List<Photos> tagPhotos) {
        this.tagPhotos = tagPhotos;
    }

    public String getCheckinPlace() {
        String location = "" + locationName;
        if (!TextUtils.isEmpty(city)) {
            return locationName + " " + city;
        }
        return location;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }


    public String getTitle() {
        String title = getFullName();
        alWordAction.add(new WordAction(0, title, userId));
        switch (postType) {
            case PostActivity.POST_TYPE_CHECKIN:
                if (getLastCheckin()) {
                    title += " is at " + getCheckinPlace();
                } else {
                    title += " was at " + getCheckinPlace();
                }
                alWordAction.add(new WordAction(1, getCheckinPlace(), ""));
                break;
            case PostActivity.POST_TYPE_ACTIVITY:
                switch (categoryName) {
                    case "Travelling":
                        title += " - " + categoryName;
                        break;
                    case "Foodspot":
                        title += " - " + "Eating" + " - " + subcategoryName;
                        break;
                    case "Celebrations":
                        title += " - " + "Celebrating" + " - " + subcategoryName;
                        break;
                    case "Groufie":
                        title+=" - "+categoryName;
                        break;
                    case "Other":
                        title += " - " + subcategoryName;
                        break;
                    default:
                        title += " - " + categoryName + " - " + subcategoryName;
                        break;
                }
                break;
            case PostActivity.POST_TYPE_PLANNING:
                if (categoryName.equals("Other")) {
                    title += " - Planning" + " - " + subcategoryName;
                } else {
                    title += " - Planning" + " - " + categoryName;
                }

                break;
        }
        title += postDescription();

        if (tagUsers != null && tagUsers.size() > 0) {
            int tagUserCount = tagUsers.size();
            switch (tagUserCount) {
                case 1:
                    title += " - with " + tagUsers.get(0).getFullName();
                    alWordAction.add(new WordAction(0, tagUsers.get(0).getFullName(), tagUsers.get(0).getiTaggedUserID()));
                    break;
                case 2:
                    title += " - with " + tagUsers.get(0).getFullName() + " and " + tagUsers.get(1).getFullName();
                    alWordAction.add(new WordAction(0, tagUsers.get(0).getFullName(), tagUsers.get(0).getiTaggedUserID()));
                    alWordAction.add(new WordAction(0, tagUsers.get(1).getFullName(), tagUsers.get(1).getiTaggedUserID()));
                    break;
                default:
                    title += " - with " + tagUsers.get(0).getFullName() + " and " + (tagUserCount - 1) + " Others";
                    alWordAction.add(new WordAction(0, tagUsers.get(0).getFullName(), tagUsers.get(0).getiTaggedUserID()));
                    alWordAction.add(new WordAction(2, (tagUserCount - 1) + " Others", ""));
                    break;
            }
        }
        return title;
    }


    private String getPostTime() {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.SERVER_DATE_FORMAT, Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));
            cal.setTime(simpleDateFormat.parse(postDate));


            return Utility.timeAgo(cal.getTimeInMillis(), "dd MMM hh:mm a");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return postDate;
    }

    private String getPlanningTime() {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat(Utility.SERVER_DATE_FORMAT, Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));
        try {
            cal.setTime(sdf.parse(planningPostDate));
            String outputDateFormat = "dd MMM h:mm a";
            outputDateFormat = outputDateFormat.replace("dd", "dd'" + Utility.dayOfMonthStr(cal.get(Calendar.DAY_OF_MONTH)) + "'");
            return new SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(cal.getTime());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return planningPostDate;
    }

    private String postDescription() {
        if (!TextUtils.isEmpty(postDescription)) {
            return " - " + postDescription;
        } else if (!TextUtils.isEmpty(planningPostDescription)) {
            return " - " + planningPostDescription;
        }
        return "";
    }

    public CharSequence getTime() {
        String planTime = "<font color='#000000'><b>Plan Time:-</b></font> ";
        return postType.equals(PostActivity.POST_TYPE_PLANNING) ? Html.fromHtml(planTime + getPlanningTime()) : getPostTime();
    }


    public int getCommentCount() {
        return commentCount;
    }


    public String getCommentCountStr() {
        return commentCount + " comment";
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public String getPostPrivacy() {
        return postPrivacy;
    }

    public void setPostPrivacy(String postPrivacy) {
        this.postPrivacy = postPrivacy;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postId);
        dest.writeString(postType);
        dest.writeString(postPrivacy);
        dest.writeString(userId);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(lastCheckin);
        dest.writeString(profileImg);
        dest.writeString(postDescription);
        dest.writeString(postDate);
        dest.writeString(locationId);
        dest.writeString(locationName);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(categoryName);
        dest.writeString(subcategoryName);
        dest.writeInt(commentCount);
        dest.writeString(planningPostDescription);
        dest.writeString(planningPostDate);
        dest.writeInt(starByme);
        dest.writeInt(starOMeterCount);
    }
}
