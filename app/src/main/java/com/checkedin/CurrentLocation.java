package com.checkedin;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;

import com.checkedin.dialog.ConfirmDialog;
import com.vinay.utillib.permissionutils.PermissionEverywhere;
import com.vinay.utillib.permissionutils.PermissionResponse;
import com.vinay.utillib.permissionutils.PermissionResultCallback;

import java.util.ArrayList;
import java.util.List;

public class CurrentLocation {
    private final Context context;
    private LocationManager mLocationManager;
    private Location location;
    private boolean isGrantedPermission;


    public CurrentLocation(Context context) {
        this.context = context;
    }


    public Location getCurrentLocation() {
        if (checkLocationSetting())
        {
            List<String> providers = new ArrayList<>();
            providers.add(LocationManager.GPS_PROVIDER);
            providers.add(LocationManager.NETWORK_PROVIDER);
            for (String provider : providers) {
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    PermissionEverywhere.getPermission(
                            context,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                            101)
                            .enqueue(new PermissionResultCallback() {
                                @Override
                                public void onComplete(PermissionResponse permissionResponse) {
                                    isGrantedPermission = permissionResponse.isGranted();
                                }
                            });
                    if (!isGrantedPermission)
                        return null;
                }
                location = mLocationManager.getLastKnownLocation(provider);
                mLocationManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }

                    @Override
                    public void onLocationChanged(Location location) {
                        CurrentLocation.this.location = location;

                    }
                });
            }
        }
        return location;
    }

    private boolean checkLocationSetting() {
        boolean networkEnabled;
        boolean gpsEnabled;
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        }

        gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (!gpsEnabled && !networkEnabled) {
//
//            new ConfirmDialog(context, R.string.gps_enable_request, new ConfirmDialog.OnConfirmYes() {
//                @Override
//                public void confirmYes() {
//                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    context.startActivity(myIntent);
//                }
//            }).show();

            return false;
        }
        return true;
    }
}
