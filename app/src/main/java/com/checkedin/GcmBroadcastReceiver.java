package com.checkedin;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.checkedin.services.GcmIntentService;
import com.checkedin.utility.Utility;

public class GcmBroadcastReceiver extends WakefulBroadcastReceiver {
    public static int counter = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Utility.logUtils("Push Notification received : ");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            return;
        }
        ComponentName comp = new ComponentName(context.getPackageName(), GcmIntentService.class.getName());
        startWakefulService(context, (intent.setComponent(comp)));
        setResultCode(Activity.RESULT_OK);
    }
}
