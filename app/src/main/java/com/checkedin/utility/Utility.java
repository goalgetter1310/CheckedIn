package com.checkedin.utility;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.checkedin.R;
import com.checkedin.dialog.ConfirmDialog;
import com.checkedin.views.CropSquareTransformation;

import java.io.File;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public final class Utility {

    public static long doubleTapTime = 0;
    public static String currentFragment;

    public static final String SERVER_DATE_FORMAT = "yyyy-MM-dd kk:mm:ss";
    public static final String SERVER_MESSAGE_DATE_FORMAT = "yyyy-MM-dd kk:mm:ssZ";
    public static final String SERVER_TIMEZONE = "GMT";

    public static boolean isOnlyAlphabet(String input) {
        boolean isOnlyAlphabet = true;
        for (int counter = 0; counter < input.length(); counter++) {
            if (!Character.isLetter(input.charAt(counter))) {
                isOnlyAlphabet = false;
                break;
            }
        }
        return isOnlyAlphabet;
    }

    public static boolean checkLocationSetting(final Context context) {
        boolean networkEnabled;
        boolean gpsEnabled;

        LocationManager    mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);


        gpsEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        networkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


        if (!gpsEnabled && !networkEnabled) {

            new ConfirmDialog(context, R.string.gps_enable_request, new ConfirmDialog.OnConfirmYes() {
                @Override
                public void confirmYes() {
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(myIntent);
                }
            }).show();

            return false;
        }
        return true;
    }

    @SuppressWarnings("deprecation")
    @TargetApi(Build.VERSION_CODES.M)
    public static int colorRes(Context context, @ColorRes int color) {
        return android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.M ? context.getResources().getColor(color) : context.getColor(color);
    }

    public static boolean checkInternetConnectivity(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
            return activeNetwork != null && (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void loadImageGlide(ImageView imageView, String imageUrl) {
        Glide.with(imageView.getContext()).load(imageUrl).dontAnimate().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder).into(imageView);
    }

    public static void loadImageGlide(ImageView imageView, String originalSizePath, CropSquareTransformation cropSquareTransformation) {
        Glide.with(imageView.getContext()).load(originalSizePath).placeholder(R.drawable.ic_placeholder)
                .bitmapTransform(cropSquareTransformation).dontAnimate().diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .error(R.drawable.ic_placeholder).into(imageView);
    }

    public static void loadImageGlide(ImageView imageView, File imageUrl) {
        Glide.with(imageView.getContext()).load(imageUrl).placeholder(R.drawable.ic_placeholder).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE).error(R.drawable.ic_placeholder).into(imageView);
    }

    public static void showSnackBar(View view, String message) {

        try {
            if (view != null && message != null) {
                final Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
                snackbar.setAction(R.string.hide, new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                });
                if (view instanceof FrameLayout) {
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundColor(colorRes(view.getContext(), R.color.colorPrimary));
                    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) snackbarView.getLayoutParams();
                    params.gravity = Gravity.BOTTOM;
                    snackbarView.setLayoutParams(params);
                }
                ((TextView) snackbar.getView().findViewById(android.support.design.R.id.snackbar_text)).setTextColor(Color.WHITE);

                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isPastDate(Calendar selectedDate) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        return selectedDate.compareTo(calendar) < 1;
    }


    public static String timeAgo(long millis, String outputDateFormat) {
        return timeAgo(millis, outputDateFormat, true);
    }

    public static String timeAgo(long millis, String outputDateFormat, boolean isDaysOfMonth) {
        long diff = System.currentTimeMillis() - millis;

        double seconds = Math.abs(diff) / 1000;
        double minutes = seconds / 60;
        double hours = minutes / 60;

        String words;
        if (seconds < 45) {
            words = "less than a minute ago";
        } else if (seconds < 90) {
            words = "a minute ago";
        } else if (minutes < 45) {
            words = Math.round(minutes) + " minutes ago";
        } else if (minutes < 90) {
            words = "an hour ago";
        } else if (hours < 24) {
            words = Math.round(hours) + " hours ago";
        } else if (hours < 42) {
            words = "Yesterday";
        } else {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(millis);

            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(new String[]{"AM", "PM"});

            if (isDaysOfMonth)
                outputDateFormat = outputDateFormat.replace("dd", "dd'" + dayOfMonthStr(cal.get(Calendar.DAY_OF_MONTH)) + "'");

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(outputDateFormat, Locale.getDefault());
            simpleDateFormat.setDateFormatSymbols(symbols);
            return simpleDateFormat.format(cal.getTime());
        }
        return words;
    }


    public static String dayOfMonthStr(int dayOfMonth) {
        if (dayOfMonth >= 11 && dayOfMonth <= 13) {
            return "th";
        } else {
            switch (dayOfMonth % 10) {
                case 1:
                    return "st";
                case 2:
                    return "nd";
                case 3:
                    return "rd";
                default:
                    return "th";
            }
        }
    }

    public static String formateDate(String strDate, String inputDateFormat, String outputDateFormat) throws ParseException {
        return new SimpleDateFormat(outputDateFormat, Locale.getDefault()).format(new SimpleDateFormat(inputDateFormat, Locale.getDefault()).parse(strDate));
    }
    public static String formatSampleDate(String strDate) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(Utility.SERVER_DATE_FORMAT, Locale.getDefault());
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));
            cal.setTime(simpleDateFormat.parse(strDate));
            DateFormatSymbols symbols = new DateFormatSymbols(Locale.getDefault());
            symbols.setAmPmStrings(new String[]{"AM", "PM"});
            String outputDateFormat ="dd MMM hh:mm a";

            outputDateFormat = outputDateFormat.replace("dd", "dd'" + dayOfMonthStr(cal.get(Calendar.DAY_OF_MONTH)) + "'");
            SimpleDateFormat simpleDate = new SimpleDateFormat(outputDateFormat, Locale.getDefault());
            simpleDate.setDateFormatSymbols(symbols);
            return simpleDate.format(cal.getTime());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strDate;
    }

    public static Drawable drawableTint(Context context, @DrawableRes int drawableRes, @ColorRes int color) {
        Drawable drawable;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            drawable = context.getResources().getDrawable(drawableRes, context.getTheme());
        } else {
            drawable = context.getResources().getDrawable(drawableRes);
        }
        drawable.setColorFilter(colorRes(context, color), PorterDuff.Mode.SRC_ATOP);

        return drawable;
    }

    public static String bundle2string(Bundle bundle) {
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";

        return string;
    }


    public static void hideKeyboard(Activity activity) {
        if (activity != null && activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }

    }

    public static void logUtils(String printMsg) {
        Log.i("TAG", printMsg);
    }

    public static String parseDate(String date, String inputFormat, String outputFormat) {
        SimpleDateFormat mFormat = new SimpleDateFormat(inputFormat, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        String outputDate = "";
        try {
            calendar.setTime(mFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        outputDate = new SimpleDateFormat(outputFormat, Locale.getDefault()).format(calendar.getTime());
        return outputDate;
    }


    public static void hideSoftKeyboard(View view) {
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

    }
}
