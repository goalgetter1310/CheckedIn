package com.checkedin.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.checkedin.R;
import com.checkedin.adapter.MessageChatRoomAdapter;
import com.checkedin.databinding.ActivityChatboxBinding;
import com.checkedin.model.MessageChatRoom;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class ChatRoomActivity extends AppCompatActivity implements EmojiconsFragment.OnEmojiconBackspaceClickedListener, EmojiconGridFragment.OnEmojiconClickedListener {

    private Socket socket;
    private String roomId, socketId, message;
    private MessageChatRoomAdapter mAdapter;
    private ArrayList<MessageChatRoom> msgList;
    private ActivityChatboxBinding mBinding;
    private boolean flagEmojiKeybordShow = false;
    private String locId, locName;
    //    private boolean isFirst = false;
    private LinearLayoutManager layoutManager;
    private boolean isLoading;
    private int page = 0, limit = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_chatbox);
        msgList = new ArrayList<>();
//        isFirst = true;
        if (getIntent().hasExtra("locId")) {
            locId = getIntent().getStringExtra("locId");
            locName = getIntent().getStringExtra("locName");
        }
        mBinding.tvMainTitle.setText(locName);
        mBinding.avi.setVisibility(View.VISIBLE);

        try {
            socket = IO.socket("http://52.36.34.178:3001");
            socket.on(Socket.EVENT_CONNECT, onConnect);
            socket.on(Socket.EVENT_DISCONNECT, onDisconnect);
            socket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
            socket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
            socket.on("getActiveUsers", socketGetActiveUserListner);
            socket.on("addMeToRoom", socketAddMeToRoomListner);
            socket.on("getChatMessage", newMessageListner);
            socket.on("notifyUser", onNotifyUser);
            socket.on("addChatMessage", addChatMessage);
            socket.connect();    //Connect socket to server
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        mAdapter = new MessageChatRoomAdapter(ChatRoomActivity.this, new ArrayList<MessageChatRoom>());

        layoutManager = new LinearLayoutManager(ChatRoomActivity.this);
        layoutManager.setReverseLayout(true);

        mBinding.rvChatbox.setLayoutManager(layoutManager);
        mBinding.rvChatbox.setAdapter(mAdapter);
        scrollListenerOnrecyclerView();
        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mBinding.ivChatSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(mBinding.etChatMsg.getText().toString().trim())) {
                    JSONObject jObj = new JSONObject();


                    try {
                        jObj.put("iRoomID", roomId);
                        jObj.put("iUserID", UserPreferences.fetchUserId(ChatRoomActivity.this));
                        jObj.put("tMessage", mBinding.etChatMsg.getText().toString().trim());
                        jObj.put("dCreatedDate", setCreateTime());
                        Log.d("TAG", "tMessage :=> " + mBinding.etChatMsg.getText().toString().trim());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    socket.emit("addChatMessage", jObj);
                }
            }
        });

        mBinding.etChatMsg.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    flagEmojiKeybordShow = true;
                    mBinding.emojicons.setVisibility(View.GONE);
                }
            }
        });

        mBinding.ivChatboxEmojis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flagEmojiKeybordShow) {
                    flagEmojiKeybordShow = false;
                    Utility.hideKeyboard(ChatRoomActivity.this);
                    mBinding.emojicons.setVisibility(View.VISIBLE);
                } else {
                    flagEmojiKeybordShow = true;
                    mBinding.emojicons.setVisibility(View.GONE);
                }
            }
        });
    }

    private void scrollListenerOnrecyclerView() {
        mBinding.rvChatbox.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();



                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {


                        page += limit;
                        JSONObject jObj = new JSONObject();

                        try {
                            jObj.put("iRoomID", roomId);
                            jObj.put("offset", page);
                            jObj.put("limit", limit);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
//                        isFirst = true;
                        socket.emit("getChatMessage", jObj);
                        isLoading = true;
                        mBinding.avi.show();
                    }
                }
            }

        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();

        socket.off(Socket.EVENT_CONNECT, onConnect);
        socket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        socket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        socket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        socket.off("getActiveUsers", socketGetActiveUserListner);
        socket.off("addMeToRoom", socketAddMeToRoomListner);
        socket.off("getChatMessage", newMessageListner);
        socket.off("notifyUser", onNotifyUser);
        socket.off("addChatMessage", addChatMessage);
    }

    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("TAG", "on connect call");

                        JSONObject jObj = new JSONObject();

                        try {
                            jObj.put("iLocationID", locId);
                            jObj.put("dCreatedDate", setCreateTime());
                            jObj.put("iUserID", UserPreferences.fetchUserId(ChatRoomActivity.this));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        socket.emit("addToDiscussion", jObj);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onNotifyUser = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("TAG", "on onNotifyUser call");
                        Log.d("TAG", "onNotifyUser :-> " + args[0] + "");

                        JSONObject data = (JSONObject) args[0];

                        try {
                            roomId = data.getString("iRoomID");
                            message = data.getString("message");
//                            socketId = data.getString("socketId");

                            JSONObject jObj = new JSONObject();

                            try {
                                jObj.put("iRoomID", roomId);
                                jObj.put("offset", 0);
                                jObj.put("limit", 10);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                            socket.emit("getChatMessage", jObj);

                        } catch (JSONException e) {
                            Log.v("TAG", "Error Getting the JSON Data!" + e.getMessage());
                            return;
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("TAG", "on disconnect call");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("TAG", "onConnectError");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };
    private Emitter.Listener addChatMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("TAG", "addChatMessage");
                        Log.d("TAG", "addChatMessage :=> " + args[0]);

                        try {
                            JSONObject jsonObject = (JSONObject) args[0];
                            MessageChatRoom msg = new MessageChatRoom();
                            msg.setIMessageID(jsonObject.getInt("iMessageID"));
                            msg.setTMessage(jsonObject.getString("tMessage"));
                            msg.setIRoomID(jsonObject.getInt("iRoomID"));
                            msg.setIUserID(jsonObject.getInt("iUserID"));
                            msg.setVFirstName(jsonObject.getString("vFirstName"));
                            msg.setVLastName(jsonObject.getString("vLastName"));
                            msg.setTUpdateDate(jsonObject.getString("dCreatedDate"));
                            mAdapter.addMessage(msg);
                            mAdapter.notifyItemInserted(0);
                            mBinding.rvChatbox.scrollToPosition(0);
//                            scrollToBottom();
                            mBinding.etChatMsg.setText("");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener socketGetActiveUserListner = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
//                        if (isFirst) {
//                            isFirst = false;
                        Log.d("TAG", "socketGetActiveUserListner");
                        Log.d("TAG", "socketGetActiveUserListner :=> " + args[0]);


                        JSONObject jObj = new JSONObject();

                        try {
                            jObj.put("iRoomID", roomId);
                            jObj.put("offset", 0);
                            jObj.put("limit", 100);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        socket.emit("getChatMessage", jObj);

//                            JSONArray data = (JSONArray) args[0];
//                            for (int i = 0; i < data.length(); i++) {
//                                JSONObject jsonObject = data.getJSONObject(i);
//                                MessageChatRoom msg = new MessageChatRoom();
//                                msg.setIMessageID(jsonObject.getInt("iMessageID"));
//                                msg.setVSocketID(jsonObject.getString("vSocketID"));
//                                msg.setTMessage(jsonObject.getString("tMessage"));
//                                msg.setTUpdateDate(jsonObject.getString("tUpdateDate"));
//                                msg.setIRoomID(jsonObject.getInt("iRoomID"));
//                                msg.setIUserID(jsonObject.getInt("iUserID"));
//                                msg.setVFirstName(jsonObject.getString("vFirstName"));
//                                msg.setVLastName(jsonObject.getString("vLastName"));
//                                if (!TextUtils.isEmpty(msg.getTMessage().toString().trim()))
//                                    msgList.add(msg);
//                            }
//
//                            mAdapter.addMessageList(msgList);
//                            scrollToBottom();
//                            mBinding.avi.hide();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener getChatMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    try {
//                        if (isFirst) {
//                            isFirst = false;
                        isLoading = false;
                        Log.d("TAG", "getChatMessage");
                        Log.d("TAG", "getChatMessage :=> " + args[0]);
                        JSONArray data = (JSONArray) args[0];

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            MessageChatRoom msg = new MessageChatRoom();
                            msg.setIMessageID(jsonObject.getInt("iMessageID"));
//                                msg.setVSocketID(jsonObject.getString("vSocketID"));
                            msg.setTMessage(jsonObject.getString("tMessage"));
                            msg.setTUpdateDate(jsonObject.getString("dCreatedDate"));
//                                msg.setcre(jsonObject.getString("tUpdateDate"));
                            msg.setIRoomID(jsonObject.getInt("iRoomID"));
                            msg.setIUserID(jsonObject.getInt("iUserID"));
                            msg.setVFirstName(jsonObject.getString("vFirstName"));
                            msg.setVLastName(jsonObject.getString("vLastName"));
                            if (!TextUtils.isEmpty(msg.getTMessage().toString().trim()))
                                msgList.add(msg);
                        }

                        mAdapter.addMessageList(msgList);
                        scrollToBottom();
                        mBinding.avi.hide();
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener socketAddMeToRoomListner = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Log.d("TAG", "socketAddMeToRoomListner");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    };

    private Emitter.Listener newMessageListner = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        Log.d("TAG", "newMessageListner");
                        Log.d("TAG", "newMessageListner :=> " + args[0]);
                        JSONArray data = (JSONArray) args[0];
                        msgList.clear();

                        if(data.length()>0) {
                            isLoading = false;

                            for (int i = 0; i < data.length(); i++) {
                                JSONObject jsonObject = data.getJSONObject(i);
                                MessageChatRoom msg = new MessageChatRoom();
                                msg.setIMessageID(jsonObject.getInt("iMessageID"));
                                msg.setTMessage(jsonObject.getString("tMessage"));
                                msg.setTUpdateDate(jsonObject.getString("dCreatedDate"));
                                msg.setIRoomID(jsonObject.getInt("iRoomID"));
                                msg.setIUserID(jsonObject.getInt("iUserID"));
                                msg.setVFirstName(jsonObject.getString("vFirstName"));
                                msg.setVLastName(jsonObject.getString("vLastName"));
                                if (!TextUtils.isEmpty(msg.getTMessage().toString().trim()))
                                    msgList.add(msg);
                            }
                            mAdapter.addMessageList(msgList);
                            scrollToBottom();
                        }



                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        mBinding.avi.hide();

                    }
                }

            });
        }
    };

    private void scrollToBottom() {
        mBinding.rvChatbox.scrollToPosition(mAdapter.getItemCount() - 1);
        layoutManager.scrollToPositionWithOffset(mAdapter.getItemCount(), 0);
    }


    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(mBinding.etChatMsg);
//        mBinding.edtMsg.setText(mBinding.edtMsg.getText().toString().substring(0, mBinding.edtMsg.getText().toString().length() - 1));
    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(mBinding.etChatMsg, emojicon);
    }

    public String concertDateInUTC() {
        Date myDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getDefault());
        calendar.setTime(myDate);

        Calendar cal = calendar;
        cal.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date time = cal.getTime();
        SimpleDateFormat outputFmt = new SimpleDateFormat("yyyy-MM-dd H:mm:s");//MMM dd, yyy h:mm a zz");
        String dateAsString = outputFmt.format(time);

        return dateAsString;

    }


    public String setCreateTime() {

        long timeInMilliseconds = 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//dd/MM/yyyy
            Date now = new Date();
            String strDate = sdf.format(now);
            sdf.setTimeZone(TimeZone.getDefault());
            Date mDate = sdf.parse(strDate);
            timeInMilliseconds = mDate.getTime();

        } catch (Exception e) {
            e.printStackTrace();
        }

        String updatedDate = "";
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd H:mm:s");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        updatedDate = sdf.format(new Date(timeInMilliseconds));

        return updatedDate;
    }

}


//{ socketId: 'YfeMvTF3yeujFTcHAAAB',
//        iUserID: '123',
//        iLocationID: '123456' }
//        getActiveUsersSql
//        [ RowDataPacket {
//        iMessageID: 11,
//        vSocketID: 'YfeMvTF3yeujFTcHAAAB',
//        tMessage: '',
//        tUpdateDate: Thu Jan 05 2017 13:40:05 GMT+0000 (UTC),
//        iRoomID: 2,
//        iUserID: 123,
//        vFirstName: 'Anjali',
//        vLastName: 'Agrawal' } ]


//    message Retrieve karva mate "getChatMessage"