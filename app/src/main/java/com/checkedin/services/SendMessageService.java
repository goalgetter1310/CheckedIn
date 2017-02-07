package com.checkedin.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.checkedin.model.Message;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.ManageDatabase;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

public class SendMessageService extends Service implements Runnable, VolleyStringRequest.AfterResponse {

    private boolean isWsCallRunning;
    private WebServiceCall webServiceCall;
    private int msgId;
    private ManageDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        webServiceCall = new WebServiceCall(this);
        database = new ManageDatabase();
        database.openDatabase(this);
        UserPreferences.msgServiceRunning(this);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(this).start();

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        database.closeDatabase();
        UserPreferences.stopMsgService(this);
    }

    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel mSendChatMsg = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
        if (mSendChatMsg != null) {
            if (mSendChatMsg.getStatus() == BaseModel.STATUS_SUCCESS || mSendChatMsg.getStatus() == BaseModel.STATUS_CHAT_MESSAGE) {
                database.removeChatMsg(msgId);
            }
        }
        isWsCallRunning = false;
    }

    @Override
    public void onErrorReceive() {
        isWsCallRunning = false;
    }

    @Override
    public void run() {
        Message message;
        while (true) {
            if (!Utility.checkInternetConnectivity(SendMessageService.this)) {
                break;
            }
            if (database.isEmptyChatMsg()) {
                break;
            } else {
                if (!isWsCallRunning) {
                    message = database.fetchChatMsg(this);
                    if (message != null) {
                        msgId = message.getMsgId();
                        isWsCallRunning = webServiceCall.chatMsgWsCall(this, message);
                    }
                }
            }
        }
        stopSelf();
    }

}