package com.checkedin.services;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.checkedin.R;
import com.checkedin.model.CheckinActivity;
import com.checkedin.model.FutureActivity;
import com.checkedin.model.MyActivity;
import com.checkedin.model.PostActivity;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

public class ImageUploadService extends Service implements VolleyStringRequest.AfterResponse {

    private WebServiceCall webServiceCall;
    private PostActivity postActivity;
    private int index;

    @Override
    public void onCreate() {
        super.onCreate();

        webServiceCall = new WebServiceCall(this);
        postActivity = UserPreferences.fetchPostActivity(this);
        index = postActivity.getAlTagPhoto().size() - 1;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (postActivity.isEdit()) {
            webServiceCall.editPostWsCall(this, postActivity, false);
        } else {
            if (postActivity instanceof CheckinActivity) {
                webServiceCall.checkinWsCall(this, (CheckinActivity) postActivity, index, false);
            } else if (postActivity instanceof FutureActivity) {
                webServiceCall.planingWsCall(this, (FutureActivity) postActivity, index, false);
            } else {
                webServiceCall.activityWsCall(this, (MyActivity) postActivity, index, false);
            }
        }
        Notify(this, Integer.parseInt(postActivity.getId()), " Image left " + index + 1, false);

        return START_NOT_STICKY;
    }


    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel mImageUpload = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, "Image Upload Response");
        if (mImageUpload != null && mImageUpload.getStatus() == BaseModel.STATUS_SUCCESS) {

            UserPreferences.removePostActivityData(this, index);
            if (index > 0) {
                index--;
                if (postActivity.isEdit()) {
                    webServiceCall.editPostWsCall(this, postActivity, false);
                } else {
                    if (postActivity instanceof CheckinActivity) {
                        webServiceCall.checkinWsCall(this, (CheckinActivity) postActivity, index, false);
                    } else if (postActivity instanceof FutureActivity) {
                        webServiceCall.planingWsCall(this, (FutureActivity) postActivity, index, false);
                    } else {
                        webServiceCall.activityWsCall(this, (MyActivity) postActivity, index, false);
                    }
                }
                Notify(this, Integer.parseInt(postActivity.getId()), " Image left " + (index + 1), false);
            } else {
                Notify(this, Integer.parseInt(postActivity.getId()), "Your post photos uploaded!", true);
            }


        } else {
            Notify(this, Integer.parseInt(postActivity.getId()), "Upload error!", true);
        }
        stopSelf();
    }

    @Override
    public void onErrorReceive() {
        stopSelf();
    }

    private void Notify(Context context, int id, String body, boolean isSound) {
        boolean isCancalable = true;
        if (Build.VERSION.SDK_INT > 10) {
            Notification.Builder builder = new Notification.Builder(context).setSmallIcon(R.mipmap.ic_launcher).setWhen(System.currentTimeMillis()).setOngoing(false).setContentTitle("Uploading Post Image").setContentText(body)
                    .setAutoCancel(true);
            if (isSound) {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uri);
            }
            startForeground(id, builder.getNotification());
        } else {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setContentText(body).setContentTitle("Uploading Post Image").setOngoing(false).setSmallIcon(R.mipmap.ic_launcher).setAutoCancel(isCancalable);
            if (isSound) {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                builder.setSound(uri);
            }

            startForeground(id, builder.getNotification());
        }
    }

}
