package com.checkedin.services;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.checkedin.R;
import com.checkedin.activity.CheckinConfirmActivity;
import com.checkedin.activity.MainActivity;
import com.checkedin.model.Notification;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;

public class GcmIntentService extends IntentService {

    public GcmIntentService() {
        super("GcmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Utility.logUtils("Push Notification received : " + Utility.bundle2string(intent.getExtras()));
        String notifyType, notifymessage;
        notifyType = intent.getExtras().getString("notificationType");
        notifymessage = intent.getExtras().getString("message");
        String userId = UserPreferences.fetchUserId(this);
        String receiverId = intent.getExtras().getString("receiver_id");
        if (!TextUtils.isEmpty(receiverId) && receiverId.equals(userId)) {
            if (notifyType != null && notifymessage != null) {
                switch (notifyType) {
                    case Notification.NOTIFY_TYPE_TEXT_MSG:

                        String likeChatId = UserPreferences.fetchLiveChatId(this);
                        String senderId = intent.getExtras().getString("sender_id");
                        String textMsg = intent.getExtras().getString("text_message");
                        if (likeChatId != null && !likeChatId.isEmpty() && likeChatId.equals(senderId)) {
                            liveChatNotify(senderId, textMsg);
                        } else {
                            sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        }
                        break;
                    case Notification.NOTIFY_TYPE_IMG_MSG:
                        if (UserPreferences.fetchLiveChatId(this) != null) {
                            liveChatNotify(intent.getExtras().getString("sender_id"), notifymessage);
                        } else {
                            sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        }
                        break;
                    case Notification.NOTIFY_TYPE_FRIEND_REQUEST:

                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_FRIEND_IN_LOUNGE:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_BIRTHDAY:
                        sendNotification(getString(R.string.app_name), notifymessage, null);
                        break;
                    case Notification.NOTIFY_TYPE_TAG_ACTIVITY:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_POST_COMMENT:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_ACCEPT_FRIEND_REQUEST:
                        String friendId = intent.getExtras().getString("sender_id");
                        UserPreferences.saveFriendConfirmId(this, friendId);
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_FAV_ACTIVITY:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_FAV_CHECKIN:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_FAV_PLANNING:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_OWNER_COMMENT:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_OTHER_POST_COMMENT:
                        sendNotification(getString(R.string.app_name), notifymessage, notifyType);
                        break;
                    case Notification.NOTIFY_TYPE_CHECKIN_CONFIRM:
                        showCheckinConfirm();
                        break;
                }
            }
        }
    }

    private void sendNotification(String title, String msg, String notifyType) {
        if (UserPreferences.fetchUserId(getApplicationContext()) != null) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("IsNotification",true);

            UserPreferences.saveNotifyType(this, notifyType);
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            int NOTIFICATION_ID = 13;
            PendingIntent contentIntent = PendingIntent.getActivity(this, NOTIFICATION_ID, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.mipmap.ic_launcher).setContentTitle(title).setStyle(new NotificationCompat.BigTextStyle().bigText(msg)).setContentText(msg).setAutoCancel(true)
                    .setSound(uri);
            mBuilder.setContentIntent(contentIntent);
            mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
        }
    }

    private void liveChatNotify(String senderId, String msg) {
        Intent intent = new Intent("live_msg_receiver");
        intent.putExtra("receive_chat_id", senderId);
        intent.putExtra("receive_chat_msg", msg);

        sendBroadcast(intent);
    }

    private void showCheckinConfirm() {
        Intent iCheckinConfirmActivity = new Intent(this, CheckinConfirmActivity.class);
        iCheckinConfirmActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(iCheckinConfirmActivity);
    }


//	 01-05 15:46:33.807: D/Tag(9773): Pudh Notification received : Bundle{
//	 message_type => Text; from => 690780215968; sender_id => 10; message =>
//	 mm; notificationType => text_message; android.support.content.wakelockid
//	 => 1; collapse_key => do_not_collapse; }Bundle

}
