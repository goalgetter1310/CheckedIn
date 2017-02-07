package com.checkedin.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.checkedin.CurrentLocation;
import com.checkedin.R;


public class MapViewDialog extends Dialog {

    private double latitude;
    private double logitude;

    private CurrentLocation currentLocation;
    private WebView wvMapview;

    public MapViewDialog(Context context, double latitude, double logitude) {
        super(context, R.style.AppTheme);
        this.latitude = latitude;
        this.logitude = logitude;
        currentLocation = new CurrentLocation(context);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_mapview);

        wvMapview = (WebView) findViewById(R.id.wv_mapview);
        wvMapview.setWebViewClient(new WebViewClient());
        wvMapview.getSettings().setJavaScriptEnabled(true);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Location currentLoc = currentLocation.getCurrentLocation();
        if (currentLoc != null) {
            String url = "http://maps.google.com/maps?" + "saddr=" + latitude + "," + logitude + "&daddr=" + currentLoc.getLatitude() + "," + currentLoc.getLongitude();
            wvMapview.loadUrl(url);
        } else {
            dismiss();
        }

    }

    @Override
    public void show() {
        super.show();
    }
}
