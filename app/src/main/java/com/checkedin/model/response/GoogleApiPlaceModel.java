package com.checkedin.model.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Yudiz on 04/08/16.
 */

public class GoogleApiPlaceModel {

    @SerializedName("next_page_token")
    private String nextPageToken;

    @SerializedName("results")
    private List<Result> alPlaceResult;

    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String nextPageToken) {
        this.nextPageToken = nextPageToken;
    }

    public List<Result> getAlPlaceResult() {
        return alPlaceResult;
    }

    public void setAlPlaceResult(List<Result> alPlaceResult) {
        this.alPlaceResult = alPlaceResult;
    }


    public class Result {

        private Geometry geometry;
        @SerializedName("name")
        private String placeName;
        @SerializedName("place_id")
        private String placeId;

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getPlaceId() {
            return placeId;
        }

        public void setPlaceId(String placeId) {
            this.placeId = placeId;
        }

        public class Geometry {
            private Loc location;

            public Loc getLocation() {
                return location;
            }

            public void setLocation(Loc location) {
                this.location = location;
            }

            public class Loc {
                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }


    }
}
