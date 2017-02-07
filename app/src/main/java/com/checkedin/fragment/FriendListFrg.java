package com.checkedin.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.FriendAdapter;
import com.checkedin.adapter.FriendListAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.RecyclerViewFastScroller;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
import java.util.Locale;

public class FriendListFrg extends Fragment implements View.OnClickListener, TextWatcher, FriendAdapter.PageChange, VolleyStringRequest.AfterResponse {

    private ArrayList<Friend> alFriendList, alAllFriend;
    private FriendListAdapter adptFriendList;

    private RecyclerViewFastScroller rvfsFriendList;
    private FloatingActionButton fabNewFriend;
    private EditText etFriendSearch;
    private LinearLayout llLoading;
    private int page, totalFriend;
    private boolean isSearching,isLoading;
    private View view;
    private WebServiceCall webServiceCall;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.frg_friend_list, container, false);
            initViews(view);

            RecyclerView rvFriendList = (RecyclerView) view.findViewById(R.id.rv_friend_list);
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            rvFriendList.setLayoutManager(mLayoutManager);
            rvfsFriendList.setRecyclerView(rvFriendList);
            rvfsFriendList.setViewsToUse(R.layout.recycler_view_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

            webServiceCall.friendListWsCall(getContext(), UserPreferences.fetchUserId(getContext()), page, false);
            rvFriendList.addOnScrollListener(new OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    int visibleItemCount = mLayoutManager.getChildCount();
                    int totalItemCount = mLayoutManager.getItemCount();
                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptFriendList.getItemCount() < totalFriend && !isSearching&&!isLoading) {
                        llLoading.setVisibility(LinearLayout.VISIBLE);
                        page += 10;
                        isLoading=true;
                        Log.i("called","yes");
                        webServiceCall.friendListWsCall(getContext(), UserPreferences.fetchUserId(getContext()), page, false);

                    }
                }
            });
            rvFriendList.setAdapter(adptFriendList);

            etFriendSearch.setCompoundDrawablesWithIntrinsicBounds(Utility.drawableTint(getContext(), R.drawable.ic_search_white_16dp, R.color.colorPrimary), null, null, null);

            etFriendSearch.addTextChangedListener(this);
            fabNewFriend.setOnClickListener(this);
        }
        return view;
    }

    private void initViews(View view) {

        llLoading = (LinearLayout) view.findViewById(R.id.ll_friend_list_loading);
        etFriendSearch = (EditText) view.findViewById(R.id.et_friend_list_search);
        fabNewFriend = (FloatingActionButton) view.findViewById(R.id.fab_friend_list_add);

        rvfsFriendList = (RecyclerViewFastScroller) view.findViewById(R.id.rvfs_friend_list);

        alFriendList = new ArrayList<>();
        alAllFriend = new ArrayList<>();

        webServiceCall = new WebServiceCall(this);
        adptFriendList = new FriendListAdapter(getActivity(), R.layout.adapter_friend_list, alFriendList, this);
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.fab_friend_list_add:
                    SearchFriendFrg friendDialog = new SearchFriendFrg();
                    ((DialogFragmentContainer) getParentFragment().getParentFragment()).fragmentTransition(friendDialog, true);
                    break;
            }
        }
    }

    @Override
    public void onPageChange() {
    }


    @Override
    public void onResponseReceive(int requestCode) {
        llLoading.setVisibility(LinearLayout.GONE);
        isLoading=false;
        FriendListModel mFriendList = (FriendListModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendListModel.class, "Friend List Response");
        if (mFriendList != null) {
            if (mFriendList.getStatus() == BaseModel.STATUS_SUCCESS) {
                if (page == 0) {
                    alFriendList.clear();
                    alAllFriend.clear();
                    totalFriend = mFriendList.getTotalFriends();
                    if (totalFriend > 0)
                        ((FriendFrg) getParentFragment()).setFriendListTitle("FRIENDS (" + totalFriend + ")", 0);
                }

                alFriendList.addAll(mFriendList.getData());
                alAllFriend.addAll(mFriendList.getData());
                adptFriendList.notifyDataSetChanged();
                if (alAllFriend.size() > 0)
                    rvfsFriendList.setVisibility(View.VISIBLE);
                else
                    rvfsFriendList.setVisibility(View.GONE);
            } else {
                Toast.makeText(getActivity(), mFriendList.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        llLoading.setVisibility(LinearLayout.GONE);
        isLoading=false;
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

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
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etFriendSearch.getWindowToken(), 0);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webServiceCall != null && webServiceCall.volleyRequestInstatnce() != null)
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_FRIEND_LIST);
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
        if (alFriendList.size() == 0) {
            rvfsFriendList.setVisibility(View.GONE);
        } else {
            rvfsFriendList.setVisibility(View.VISIBLE);
        }
    }

}
