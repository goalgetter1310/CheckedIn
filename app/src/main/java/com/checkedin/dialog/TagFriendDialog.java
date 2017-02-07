package com.checkedin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.TagFriendAdapter;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.AutoLabelUI;
import com.checkedin.views.AutoLabelUI.OnRemoveLabelListener;
import com.checkedin.views.stickylistheaders.StickyListHeadersListView;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;


public class TagFriendDialog extends Dialog implements OnRemoveLabelListener, OnItemClickListener, View.OnClickListener, TextWatcher, VolleyStringRequest.AfterResponse {

    private TagFriendAdapter adptFriendList;
    private WebServiceCall webServiceCall;
    private ScrollView mScrollView;
    private AutoLabelUI mAutoLabel;
    private LayoutParams defaultScrollParams;
    private Activity activity;
    private TagFriendListener tagListener;
    private StickyListHeadersListView rvFriendList;
    private LinearLayout llLoading;

    private ArrayList<Friend> alFriendList, alSuggestedFriendList, alAllFriendList, alAllSuggestedFriendList;
    private EditText etSearch;
    private int page;
    private int totalRecords;
    private boolean isSearch;

    public TagFriendDialog(AppCompatActivity activity, TagFriendListener tagListener) {
        super(activity, R.style.Theme_AppTheme);
        this.activity = activity;
        this.tagListener = tagListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tag_friend);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().getAttributes().softInputMode = android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        TextView tvDone = (TextView) findViewById(R.id.tv_done);
        webServiceCall = new WebServiceCall(this);
        initViews();

        rvFriendList.setAreHeadersSticky(true);
        rvFriendList.setOnItemClickListener(TagFriendDialog.this);
        rvFriendList.setAdapter(adptFriendList);

        webServiceCall.friendListWsCall(getContext(), UserPreferences.fetchUserId(getContext()), page, true);

        rvFriendList.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((visibleItemCount + firstVisibleItem) >= totalItemCount && alAllFriendList.size() < totalRecords && !isSearch) {
                    llLoading.setVisibility(LinearLayout.VISIBLE);
                    page += 10;
                    webServiceCall.friendListWsCall(getContext(),UserPreferences.fetchUserId(getContext()), page, false);
                }
            }

        });

        etSearch.addTextChangedListener(this);
        ivBack.setOnClickListener(this);
        tvDone.setOnClickListener(this);
    }

    private Activity getActivity() {
        return activity;
    }

    private void initViews() {
        rvFriendList = (StickyListHeadersListView) findViewById(R.id.rv_friend_list);
        etSearch = (EditText) findViewById(R.id.et_friend_list_search);
        llLoading = (LinearLayout) findViewById(R.id.ll_friend_list_loading);
        mAutoLabel = (AutoLabelUI) findViewById(R.id.label_view);
        mAutoLabel.setOnRemoveLabelListener(TagFriendDialog.this);
        webServiceCall = new WebServiceCall(this);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        defaultScrollParams = mScrollView.getLayoutParams();


        alFriendList = new ArrayList<>();
        alSuggestedFriendList = new ArrayList<>();
        alAllFriendList = new ArrayList<>();
        alAllSuggestedFriendList = new ArrayList<>();

        adptFriendList = new TagFriendAdapter(getActivity(), alFriendList, alSuggestedFriendList);


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
              Utility.hideKeyboard(getActivity());
                TagFriendDialog.this.dismiss();
                break;
            case R.id.tv_done:
                Utility.hideKeyboard(getActivity());
                ArrayList<Friend> taggedFriends = new ArrayList<>();
                for (Friend friend : alFriendList) {
                    if (friend.isSelected())
                        taggedFriends.add(friend);
                }
                tagListener.onFriendTagged(taggedFriends);
                TagFriendDialog.this.dismiss();
                break;
        }
    }


    @Override
    public void onResponseReceive(int requestCode) {
        llLoading.setVisibility(View.GONE);

        FriendListModel mFriendList = (FriendListModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendListModel.class, "FriendList Response");
        if (mFriendList != null) {
            if (mFriendList.getStatus() == BaseModel.STATUS_SUCCESS) {
                if (mFriendList.getData() != null) {
                    mAutoLabel.removeAllViews();

                    if (page == 0) {
                        totalRecords = mFriendList.getTotalFriends();
                        alSuggestedFriendList.addAll(mFriendList.getSuggestedFriend());
                        alAllSuggestedFriendList.addAll(alSuggestedFriendList);
                    }


                    alFriendList.addAll(mFriendList.getData());
                    alAllFriendList.addAll(alFriendList);


                    adptFriendList.notifyDataSetChanged();
                }
            } else {
                Toast.makeText(getActivity(), mFriendList.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        llLoading.setVisibility(View.GONE);
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRemoveLabel(Object view, int position) {
        adjustTagViewSize(false);
        removeTag((String) view);
    }


    private void adjustTagViewSize(final boolean isAdded) {
        mAutoLabel.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mAutoLabel.getChildCount() == 0) {
                    mScrollView.setLayoutParams(defaultScrollParams);
                    if (!isAdded)
                        mScrollView.setPadding(3, 0, 3, 0);
                    return;
                }
                if (mAutoLabel.getHeight() >= (mAutoLabel.getChildAt(0).getHeight() * 2)) {
                    LinearLayout.LayoutParams newScrollParams = new LinearLayout.LayoutParams(defaultScrollParams);
                    newScrollParams.height = (mAutoLabel.getChildAt(0).getHeight() * 2) + 10;
                    mScrollView.setLayoutParams(newScrollParams);
                } else {
                    mScrollView.setLayoutParams(defaultScrollParams);
                }
                if (isAdded) {
                    mScrollView.fullScroll(View.FOCUS_DOWN);
                }
            }
        }, 400);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        selectUnSelectTag(position);

    }

    private void removeTag(String userId) {
        for (int counter = 0; counter < alAllFriendList.size(); counter++) {
            if (alAllFriendList.get(counter).getId().equals(userId)) {
                alAllFriendList.get(counter).setSelected(false);
                break;
            }
        }
        for (int counter = 0; counter < alAllSuggestedFriendList.size(); counter++) {
            if (alAllSuggestedFriendList.get(counter).getId().equals(userId)) {
                alAllSuggestedFriendList.get(counter).setSelected(false);
                break;
            }
        }

    }

    public void selectUnSelectTag(int selectedPosition) {
        ArrayList<Friend> alFriend;
        String userId;
        if (selectedPosition < alSuggestedFriendList.size()) {
            userId = alSuggestedFriendList.get(selectedPosition).getId();
            alSuggestedFriendList.get(selectedPosition).setSelected(!alSuggestedFriendList.get(selectedPosition).isSelected());
            alFriend = alFriendList;

            if (alSuggestedFriendList.get(selectedPosition).isSelected()) {
                mAutoLabel.addLabelByTag(alSuggestedFriendList.get(selectedPosition).getId(), alSuggestedFriendList.get(selectedPosition).getFullName());
            } else {
                mAutoLabel.removeLabelByTag(alSuggestedFriendList.get(selectedPosition).getId(), alSuggestedFriendList.get(selectedPosition).getFullName());
            }
            adjustTagViewSize(alSuggestedFriendList.get(selectedPosition).isSelected());
        } else {
            int position = selectedPosition - alSuggestedFriendList.size();
            userId = alFriendList.get(position).getId();
            alFriendList.get(position).setSelected(!alFriendList.get(position).isSelected());
            alFriend = alSuggestedFriendList;

            if (alFriendList.get(position).isSelected()) {
                mAutoLabel.addLabelByTag(alFriendList.get(position).getId(), alFriendList.get(position).getFullName());
            } else {
                mAutoLabel.removeLabelByTag(alFriendList.get(position).getId(), alFriendList.get(position).getFullName());
            }
            adjustTagViewSize(alFriendList.get(position).isSelected());


        }

        for (int counter = 0; counter < alFriend.size(); counter++) {
            if (userId.equals(alFriend.get(counter).getId())) {
                alFriend.get(counter).setSelected(!alFriend.get(counter).isSelected());
                break;
            }
        }
        adptFriendList.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String search = etSearch.getText().toString().trim();
        alFriendList.clear();
        alSuggestedFriendList.clear();
        if (TextUtils.isEmpty(search)) {
            alFriendList.addAll(alAllFriendList);
            alSuggestedFriendList.addAll(alAllSuggestedFriendList);
            isSearch = false;
        } else {
            isSearch = true;
            search = search.toLowerCase(Locale.getDefault());
            for (int counter = 0; counter < alAllSuggestedFriendList.size(); counter++) {
                if (alAllSuggestedFriendList.get(counter).getFullName().toLowerCase(Locale.getDefault()).startsWith(search)) {
                    alSuggestedFriendList.add(alAllSuggestedFriendList.get(counter));
                }
            }
            for (int counter = 0; counter < alAllFriendList.size(); counter++) {
                if (alAllFriendList.get(counter).getFullName().toLowerCase(Locale.getDefault()).startsWith(search)) {
                    alFriendList.add(alAllFriendList.get(counter));
                }
            }
        }
        adptFriendList.notifyDataSetChanged();
    }

    public interface TagFriendListener {
        void onFriendTagged(ArrayList<Friend> taggedFriends);
    }

}