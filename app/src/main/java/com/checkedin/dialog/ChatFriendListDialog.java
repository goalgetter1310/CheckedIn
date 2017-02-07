package com.checkedin.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.ChatFriendListAdapter;
import com.checkedin.adapter.ChatFriendListAdapter.FriendClickListener;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;

@SuppressLint("InflateParams")
public class ChatFriendListDialog extends Dialog implements View.OnClickListener, TextWatcher, VolleyStringRequest.AfterResponse {

    private Context context;
    private ArrayList<Friend> alFriendList, alAllFriend;
    private ChatFriendListAdapter adptFriendList;

    private RecyclerView rvFriendList;
    private ImageView ivBack;
    private WebServiceCall webServiceCall;
    private EditText etFriendSearch;
    private LinearLayout llLoading;
    private int page, totalFriend;
    private FriendClickListener friendClickListener;
    private boolean isSearching;

    public ChatFriendListDialog(Context context, FriendClickListener friendClickListener) {
        super(context, R.style.Theme_AppTheme);
        this.context = context;
        this.friendClickListener = friendClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_chat_friend_list);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initViews();
        webServiceCall = new WebServiceCall(this);
        rvFriendList.setLayoutManager(new LinearLayoutManager(context));

        webServiceCall.friendListWsCall(getContext(), UserPreferences.fetchUserId(context), page, true);
        rvFriendList.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (adptFriendList.getItemCount() < totalFriend && !isSearching) {
                    llLoading.setVisibility(LinearLayout.VISIBLE);
                    webServiceCall.friendListWsCall(getContext(), UserPreferences.fetchUserId(context), page, false);
                }
            }
        });

        etFriendSearch.setCompoundDrawablesWithIntrinsicBounds(Utility.drawableTint(getContext(), R.drawable.ic_search_white_16dp, R.color.colorPrimary), null, null, null);

        rvFriendList.setAdapter(adptFriendList);
        etFriendSearch.addTextChangedListener(this);
        ivBack.setOnClickListener(this);
    }

    private void initViews() {

        llLoading = (LinearLayout) findViewById(R.id.ll_friend_list_loading);
        etFriendSearch = (EditText) findViewById(R.id.et_friend_list_search);
        ivBack = (ImageView) findViewById(R.id.iv_chat_friend_list_back);
        rvFriendList = (RecyclerView) findViewById(R.id.rv_friend_list);
        alFriendList = new ArrayList<>();
        alAllFriend = new ArrayList<>();
        webServiceCall = new WebServiceCall(this);
        adptFriendList = new ChatFriendListAdapter(context, R.layout.adapter_friend_list, alFriendList, friendClickListener);

    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.iv_chat_friend_list_back:
                    dismiss();
                    break;
            }
        }
    }


    @Override
    public void onResponseReceive(int requestCode) {
        llLoading.setVisibility(LinearLayout.GONE);
        FriendListModel mFriendList = (FriendListModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendListModel.class, "Friend List Response");
        if (mFriendList != null) {
            if (mFriendList.getStatus() == BaseModel.STATUS_SUCCESS) {
                if (page == 0) {
                    alFriendList.clear();
                    alAllFriend.clear();
                }
                totalFriend = mFriendList.getTotalFriends();
                alFriendList.addAll(mFriendList.getData());
                alAllFriend.addAll(mFriendList.getData());
                adptFriendList.notifyDataSetChanged();

                page++;

            } else {
                Toast.makeText(context, mFriendList.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        Toast.makeText(context, R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    @SuppressLint("DefaultLocale")
    private void friendSearch() {
        String search = etFriendSearch.getText().toString();
        if (!TextUtils.isEmpty(search)) {
            alFriendList.clear();
            for (Friend friend : alAllFriend) {
                if (friend.getFullName().toLowerCase(Locale.getDefault()).contains(search.toLowerCase())) {
                    alFriendList.add(friend);
                }
            }
        } else {
            isSearching = false;
            alFriendList.clear();
            alFriendList.addAll(alAllFriend);
        }
        adptFriendList.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable arg0) {

    }

    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        isSearching = true;
        if (alAllFriend.size() != 0) {
            friendSearch();
        }
    }

    @Override
    public void dismiss() {
        if (webServiceCall != null && webServiceCall.volleyRequestInstatnce() != null)
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_FRIEND_LIST);
        super.dismiss();

    }
}
