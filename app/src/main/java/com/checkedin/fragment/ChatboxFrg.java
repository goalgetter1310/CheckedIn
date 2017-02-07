package com.checkedin.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.ChatboxAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.Message;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.GetMessageModel;
import com.checkedin.services.SendMessageService;
import com.checkedin.utility.ManageDatabase;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;
import com.rockerhieu.emojicon.emoji.Emojicon;

import org.apache.commons.lang3.StringEscapeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ChatboxFrg extends Fragment implements OnClickListener, OnEditorActionListener, EmojiconGridFragment.OnEmojiconClickedListener,
        EmojiconsFragment.OnEmojiconBackspaceClickedListener, VolleyStringRequest.AfterResponse {

    private RecyclerView rvChatbox;
    private ImageView ivSend;
    private EmojiconEditText etMessage;

    private ArrayList<Message> alMessageList;
    private ChatboxAdapter adptMsg;

    private String friendId, friendName, friendImgUrl;
    private LinearLayoutManager mLayoutManager;

    private WebServiceCall webServiceCall;
    private boolean isLoading;
    private int page, totalMsg;
    private View frameEmojies;
    private ImageView ivEmojies;
    private boolean isEmojies;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_chatbox, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);

        Bundle argument = getArguments();
        friendId = argument.getString("friend_id");
        friendName = argument.getString("friend_name");
        friendImgUrl = argument.getString("friend_image_url");


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().trackScreenView(AnalyticsTrackers.ANALYTICS_PAGE_CHAT);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mMessageReceiver, new IntentFilter("live_msg_receiver"));
        UserPreferences.saveLiveChatId(getActivity(), friendId);
    }

    private void setEmojiconFragment(boolean useSystemDefault) {
        EmojiconsFragment emojisFrg = EmojiconsFragment.newInstance(useSystemDefault);
        emojisFrg.setmOnEmojiconBackspaceClickedListener(this);
        getChildFragmentManager().beginTransaction().replace(R.id.emojicons, emojisFrg).commit();
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);

        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        rvChatbox.setLayoutManager(mLayoutManager);
        rvChatbox.setAdapter(adptMsg);
        scrollListenerOnrecyclerView();
        webServiceCall.getChatMsgWsCall(getContext(), page, friendId);
        isLoading = true;

        ivSend.setOnClickListener(this);
        ivEmojies.setOnClickListener(this);


        etMessage.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                frameEmojies.setVisibility(View.GONE);
                ivEmojies.setImageResource(R.drawable.ic_smily);
            }
        });
        etMessage.setOnEditorActionListener(this);
        etMessage.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
        menu.findItem(R.id.itm_menu_search_place).setVisible(false);
        menu.findItem(R.id.itm_menu_post).setVisible(false);
        menu.findItem(R.id.itm_menu_chat_friend).setVisible(false);

        MenuItem menuItem = menu.findItem(R.id.itm_menu_chat_user);
        menuItem.setActionView(R.layout.menu_chat_user);
        menuItem.getActionView();

        ImageView ivProfileImg = (ImageView) menuItem.getActionView();

        Utility.loadImageGlide(ivProfileImg, friendImgUrl);
        ivProfileImg.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    TimelineFrg friendProfile = new TimelineFrg();
                    Bundle argument = new Bundle();
                    argument.putString("friend_id", friendId);
                    friendProfile.setArguments(argument);
                    if (DialogFragmentContainer.isDialogOpen) {
                        DialogFragmentContainer.getInstance().fragmentTransition(friendProfile, true);

                    } else {
                        DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                        dialogFrgContainer.init(friendProfile);
                        dialogFrgContainer.show(getActivity().getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    }
                }
            }
        });

        menuItem.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }


    private void initViews(final View view) {

        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(friendName);

        rvChatbox = (RecyclerView) view.findViewById(R.id.rv_chatbox);
        ivSend = (ImageView) view.findViewById(R.id.iv_chat_send);
        etMessage = (EmojiconEditText) view.findViewById(R.id.et_chat_msg);
        frameEmojies = view.findViewById(R.id.emojicons);
        ivEmojies = (ImageView) view.findViewById(R.id.iv_chatbox_emojis);

        alMessageList = new ArrayList<>();
        adptMsg = new ChatboxAdapter(getContext(), alMessageList);
        webServiceCall = new WebServiceCall(this);

    }

    private void scrollListenerOnrecyclerView() {
        rvChatbox.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && alMessageList.size() < totalMsg) {
                        page += 10;
                        webServiceCall.getChatMsgWsCall(getContext(), page, friendId);
                        isLoading = true;
                    }
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_chat_send:
                sendMsg();
                break;
            case R.id.iv_chatbox_emojis:
                showEmojisOrKeyboard();
                break;
            case R.id.et_chat_msg:
                if (isEmojies)
                    hideSoftKeyboard();
                break;
        }

    }

    private void showEmojisOrKeyboard() {
        if (!isEmojies) {
            hideSoftKeyboard();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    frameEmojies.setVisibility(View.VISIBLE);
                    ivEmojies.setImageResource(R.drawable.ic_keyboard);
                    setEmojiconFragment(false);
                }
            }, 400);

            isEmojies = true;
        } else {
            frameEmojies.setVisibility(View.GONE);
            ivEmojies.setImageResource(R.drawable.ic_smily);
            showSoftKeyboard();
            isEmojies = false;
        }
    }

    private void sendMsg() {
        String chatMsg = StringEscapeUtils.escapeJava(etMessage.getText().toString().trim());

        if (!chatMsg.isEmpty()) {
            SimpleDateFormat mFormat = new SimpleDateFormat(Utility.SERVER_MESSAGE_DATE_FORMAT, Locale.getDefault());
            mFormat.setTimeZone(TimeZone.getTimeZone(Utility.SERVER_TIMEZONE));

            Message message = new Message(getContext());
            message.setSenderId(UserPreferences.fetchUserId(getActivity()));
            message.setReceiverId(friendId);
            message.setMsgType(Message.MESSAGE_TYPE_TEXT);
            message.setMsgTextOrImage(chatMsg);
            message.setCreateTime(mFormat.format(Calendar.getInstance().getTime()));
            message.setFriendImgUrl(friendImgUrl);

            alMessageList.add(0, message);
            adptMsg.notifyItemInserted(0);
            rvChatbox.scrollToPosition(0);
            etMessage.setText("");

            ManageDatabase database = new ManageDatabase();
            database.openDatabase(getActivity());
            database.saveChatMsg(message);
            database.closeDatabase();

            if (!UserPreferences.isMsgServiceRunning(getActivity())) {
                Intent messageIntent = new Intent(getActivity(), SendMessageService.class);
                getActivity().startService(messageIntent);
            }
        }
    }

    @Override
    public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
        sendMsg();
        return true;
    }


    private void onMessageReceive(String senderId, String msg) {
        Message message = new Message(getContext());
        message.setSenderId(senderId);
        message.setReceiverId(UserPreferences.fetchUserId(getActivity()));
        message.setCreateTime(new SimpleDateFormat(Utility.SERVER_MESSAGE_DATE_FORMAT, Locale.getDefault()).format(Calendar.getInstance().getTime()));
        message.setMsgType(Message.MESSAGE_TYPE_TEXT);
        message.setMsgTextOrImage(msg);
        message.setFriendImgUrl(friendImgUrl);
        alMessageList.add(0, message);
        rvChatbox.scrollToPosition(0);

        adptMsg.notifyItemInserted(0);
        webServiceCall.getChatMsgWsCall(getContext(), 0, friendId, -1, false);

    }


    @Override
    public void onResponseReceive(int requestCode) {
        if (requestCode == WebServiceCall.REQUEST_CODE_MESSAGES_LIST) {
            isLoading = false;
            GetMessageModel mGetMessage = (GetMessageModel) webServiceCall.volleyRequestInstatnce().getModelObject(GetMessageModel.class, "GetMessageModel");
            if (mGetMessage != null) {
                if (mGetMessage.getStatus() == BaseModel.STATUS_SUCCESS) {

                    totalMsg = mGetMessage.getData().getTotal();
                    ArrayList<Message> alSortMessage = new ArrayList<>();

                    for (int counter = 0; counter < mGetMessage.getData().getMessages().size(); counter++) {
                        Message message = new Message(getContext());
                        message.setSenderId(mGetMessage.getData().getMessages().get(counter).getiMsgFrom());
                        message.setReceiverId(mGetMessage.getData().getMessages().get(counter).getiMsgTo());
                        message.setMsgType(mGetMessage.getData().getMessages().get(counter).geteMessageType());
                        message.setMsgTextOrImage(mGetMessage.getData().getMessages().get(counter).gettMessage());
                        message.setCreateTime(mGetMessage.getData().getMessages().get(counter).getdCreated());
                        message.setFriendImgUrl(friendImgUrl);
                        alSortMessage.add(message);
                    }

                    alMessageList.addAll(alSortMessage);
                    adptMsg.notifyDataSetChanged();

                } else {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mGetMessage.getMessage());
                }
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
            }
        }
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String id = intent.getStringExtra("receive_chat_id");
            String message = intent.getStringExtra("receive_chat_msg");
            onMessageReceive(id, message);
        }
    };

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(etMessage, emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(etMessage);
    }


    private void hideSoftKeyboard() {
        if (etMessage != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(etMessage.getWindowToken(), 0);
        }
    }


    private void showSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInputFromWindow(etMessage.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            isEmojies = true;
            showEmojisOrKeyboard();
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            isEmojies = false;
            showEmojisOrKeyboard();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mMessageReceiver);
        UserPreferences.removeLiveChatId(getActivity());
    }

    @Override
    public void onDestroyView() {
        frameEmojies.setVisibility(View.GONE);
        webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_MESSAGES_LIST);
        super.onDestroyView();
    }

}
