package com.checkedin.model;

import android.content.Context;

import com.checkedin.utility.UserPreferences;

import java.util.ArrayList;
import java.util.List;

public class PostActivity {

    //	public static final String POST_TYPE_PHOTO = "1";
//	public static final String POST_TYPE_ALBUM = "2";
    public static final String POST_TYPE_PLANNING = "3";
    //	public static final String POST_TYPE_ACTIVITY = "4";
    public static final String POST_TYPE_CHECKIN = "5";
    public static final String POST_TYPE_ACTIVITY = "6";

    public static final String POST_PRIVACY_PUBLIC = "1";
    public static final String POST_PRIVACY_FRIEND = "2";
    public static final String POST_PRIVACY_ONLY_ME = "3";

    public static final int MAX_IMAGES_SELECT = 10;
    private List<String> tagFriendList;
    private List<Friend> tagFriends;
    private String id = "", url = "";
    private String type = POST_TYPE_CHECKIN;
    private String privacy = POST_PRIVACY_PUBLIC;
    private ArrayList<Photos> alTagPhoto;
    private List<String> removePhotoId;
    private boolean isEdit;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Photos> getAlTagPhoto() {
        if (alTagPhoto == null) {
            alTagPhoto = new ArrayList<>();
        }
        return alTagPhoto;
    }


    public String getUserId(Context context) {
        return UserPreferences.fetchUserId(context);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTagFriendList() {
        return tagFriendList;
    }

    public void setTagFriendIds(List<Friend> tagFriends) {
        this.tagFriendList = new ArrayList<>();
        for (Friend friend : tagFriends) {
            tagFriendList.add(friend.getId());
        }
    }


    public void setTagFriendList(List<String> tagFriendList) {
        this.tagFriendList = tagFriendList;
    }

    public void setAlTagPhoto(ArrayList<Photos> alTagPhoto) {
        this.alTagPhoto = alTagPhoto;
    }

    public List<String> getRemovePhotoId() {
        return removePhotoId;
    }

    public void setRemovePhotoId(List<String> removePhotoId) {
        this.removePhotoId = removePhotoId;
    }

    public boolean isEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        isEdit = edit;
    }

    public List<Friend> getTagFriends() {
        return tagFriends;
    }

    public void setTagFriends(List<Friend> tagFriends) {
        this.tagFriends = tagFriends;
    }
}
