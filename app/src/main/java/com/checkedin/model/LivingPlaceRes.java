package com.checkedin.model;

/**
 * Created by Yudiz on 24/08/16.
 */

public class LivingPlaceRes {
    private LocationType vLocationType;

    public LocationType getvLocationType() {
        return vLocationType;
    }

    public void setvLocationType(LocationType vLocationType) {
        this.vLocationType = vLocationType;
    }

    public class LocationType {

        private Town HomeTown;
        private Town CurrentTown;

        public Town getHomeTown() {
            return HomeTown;
        }

        public void setHomeTown(Town homeTown) {
            HomeTown = homeTown;
        }

        public Town getCurrentTown() {
            return CurrentTown;
        }

        public void setCurrentTown(Town currentTown) {
            CurrentTown = currentTown;
        }

        public class Town {
            private String vFullAddress;
            private String vLatitude;
            private String vLongitude;

            public String getvFullAddress() {
                return vFullAddress;
            }

            public void setvFullAddress(String vFullAddress) {
                this.vFullAddress = vFullAddress;
            }

            public String getvLatitude() {
                return vLatitude;
            }

            public void setvLatitude(String vLatitude) {
                this.vLatitude = vLatitude;
            }

            public String getvLongitude() {
                return vLongitude;
            }

            public void setvLongitude(String vLongitude) {
                this.vLongitude = vLongitude;
            }
        }


    }
}
