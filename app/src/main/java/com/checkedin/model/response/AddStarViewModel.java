package com.checkedin.model.response;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Yudiz on 22/07/16.
 */

public class AddStarViewModel extends BaseModel {
    @SerializedName("data")
    private AddStarView addStarView;

    public AddStarView getAddStarView() {
        return addStarView;
    }

    public void setAddStarView(AddStarView addStarView) {
        this.addStarView = addStarView;
    }

    public class AddStarView {
        @SerializedName("iStarOMeterCount")
        private int starOMeterCount;
//        @SerializedName("iViewOMeterCount")
//        private int viewOMeterCount;
        @SerializedName("isStarOMeter")
        private int starOMeter;
//        @SerializedName("isViewOMeter")
//        private int viewOMeter;

        public int getStarOMeterCount() {
            return starOMeterCount;
        }

        public void setStarOMeterCount(int starOMeterCount) {
            this.starOMeterCount = starOMeterCount;
        }

//        public int getViewOMeterCount() {
//            return viewOMeterCount;
//        }
//
//        public void setViewOMeterCount(int viewOMeterCount) {
//            this.viewOMeterCount = viewOMeterCount;
//        }

        public int getStarOMeter() {
            return starOMeter;
        }

        public void setStarOMeter(int starOMeter) {
            this.starOMeter = starOMeter;
        }

//        public int getViewOMeter() {
//            return viewOMeter;
//        }
//
//        public void setViewOMeter(int viewOMeter) {
//            this.viewOMeter = viewOMeter;
//        }
    }

}
