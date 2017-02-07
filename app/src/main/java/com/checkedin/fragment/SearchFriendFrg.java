package com.checkedin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.FriendSearchAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.SearchFriendModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class SearchFriendFrg extends Fragment implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener, VolleyStringRequest.AfterResponse {

    private ImageView ivBack, ivCancel;
    private EditText etSearch;
    private WebServiceCall webServiceCall;
    private ArrayList<Friend> alFriendSearchList;
    private FriendSearchAdapter adptFriendSearchList;
    private RecyclerView rvFriendSearch;
    private boolean isLoading;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_friend_search, container, false);
        initViews(view);

        rvFriendSearch.setLayoutManager(new LinearLayoutManager(getActivity()));

        etSearch.addTextChangedListener(this);
        etSearch.setOnEditorActionListener(this);
        ivBack.setOnClickListener(this);
        ivCancel.setOnClickListener(this);

        return view;
    }

    private void initViews(View view) {
        rvFriendSearch = (RecyclerView) view.findViewById(R.id.rv_friend_search);
        ivBack = (ImageView) view.findViewById(R.id.iv_friend_search_back);
        ivCancel = (ImageView) view.findViewById(R.id.iv_friend_search_cancel);
        etSearch = (EditText) view.findViewById(R.id.et_friend_search);

        webServiceCall = new WebServiceCall(this);
        alFriendSearchList = new ArrayList<>();
        adptFriendSearchList = new FriendSearchAdapter(this, getActivity(), R.layout.adapter_friend_list, alFriendSearchList);
        rvFriendSearch.setAdapter(adptFriendSearchList);
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.iv_friend_search_back:
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                    break;

                case R.id.iv_friend_search_cancel:
                    etSearch.setText("");
                    break;
            }
        }
    }


    @Override
    public void onResponseReceive(int requestCode) {
        isLoading = false;
        SearchFriendModel mSearchFriend = (SearchFriendModel) webServiceCall.volleyRequestInstatnce().getModelObject(SearchFriendModel.class, "SearchFriend Response");
        if (mSearchFriend != null) {
            if (mSearchFriend.getStatus() == BaseModel.STATUS_SUCCESS) {
                alFriendSearchList.clear();
                alFriendSearchList.addAll(mSearchFriend.getData());
                adptFriendSearchList.notifyDataSetChanged();
            }

        } else {
            Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    private final CountDownTimer mTimerFinish = new CountDownTimer(300, 300) {

        @Override
        public void onTick(final long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            if (!isLoading) {
                isLoading = true;
                webServiceCall.searchFriendWsCall(getContext(), etSearch.getText().toString());
            }
        }
    };

    @Override
    public void afterTextChanged(Editable s) {

        mTimerFinish.cancel();
        if (!TextUtils.isEmpty(etSearch.getText().toString().trim())) {
            mTimerFinish.start();
        } else {
            alFriendSearchList.clear();
            adptFriendSearchList.notifyDataSetChanged();
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etSearch.getWindowToken(), 0);

    }


    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        String searchText = etSearch.getText().toString().trim();
        if (!TextUtils.isEmpty(searchText) && !isLoading) {
            isLoading = true;
            webServiceCall.searchFriendWsCall(getContext(), searchText);
        }
        return false;
    }
}
