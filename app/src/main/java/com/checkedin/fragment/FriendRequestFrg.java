package com.checkedin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.FriendAdapter;
import com.checkedin.adapter.FriendRequestAdapter;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class FriendRequestFrg extends Fragment implements FriendAdapter.PageChange, VolleyStringRequest.AfterResponse {

    private RecyclerView rvFriendRequestList;
    private LinearLayout llLoading;

    private WebServiceCall webServiceCall;
    private boolean isFirstTime;
    private int page;
    private ArrayList<Friend> alFriendRequest;
    private FriendRequestAdapter adptFriendRequest;
    private int total;
    private boolean isLoading;
//    private boolean isPageChange;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_friend_request, container, false);
        initViews(view);

        final android.support.v7.widget.LinearLayoutManager mLayoutManager = new android.support.v7.widget.LinearLayoutManager(getActivity());
        rvFriendRequestList.setLayoutManager(mLayoutManager);

        rvFriendRequestList.setAdapter(adptFriendRequest);

        rvFriendRequestList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptFriendRequest.getItemCount() < total && !isLoading) {
                    llLoading.setVisibility(LinearLayout.VISIBLE);
                    page += 10;
                    isLoading = true;

                    webServiceCall.friendRequestWsCall(getContext(), page);
                }
            }
        });


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            if (!isFirstTime) {
                if (Utility.checkInternetConnectivity(getActivity())) {
                    isFirstTime = true;
                    isLoading = true;
                    webServiceCall.friendRequestWsCall(getContext(), page);
                } else {
                    Toast.makeText(getActivity(), R.string.no_internet_connect, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initViews(View view) {
        rvFriendRequestList = (RecyclerView) view.findViewById(R.id.rv_friend_request);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_friend_request_loading);

        alFriendRequest = new ArrayList<>();
        adptFriendRequest = new FriendRequestAdapter(getActivity(), R.layout.adapter_friend_request, alFriendRequest, this);
        webServiceCall = new WebServiceCall(this);
    }


    @Override
    public void onResponseReceive(int requestCode) {
        llLoading.setVisibility(LinearLayout.GONE);
        isLoading = false;
        FriendListModel mFriendRequest = (FriendListModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendListModel.class, "Friend Request Response");
        if (mFriendRequest != null) {
            if (mFriendRequest.getStatus() == BaseModel.STATUS_SUCCESS) {

                total = mFriendRequest.getTotalFriends();
                if (total > 0)
                    ((FriendFrg) getParentFragment()).setFriendListTitle("REQUEST (" + total + ")", 1);
                alFriendRequest.addAll(mFriendRequest.getData());
                adptFriendRequest.notifyDataSetChanged();
            }
//            else {
//                if (alFriendRequest.size() == 0)
//                    Toast.makeText(getActivity(), mFriendRequest.getMessage(), Toast.LENGTH_LONG).show();
//            }
        }
//        else {
//            Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
//        }
    }


    public void updateRequestCounter() {
        total--;
        if (total == 0) {
            ((FriendFrg) getParentFragment()).setFriendListTitle("REQUEST", 1);
        } else {
            ((FriendFrg) getParentFragment()).setFriendListTitle("REQUEST (" + total + ")", 1);
        }
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        llLoading.setVisibility(LinearLayout.GONE);
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        isFirstTime = false;
    }

    @Override
    public void onPageChange() {
        try {
            if (!isFirstTime) {
                if (Utility.checkInternetConnectivity(getActivity())) {
                    isFirstTime = true;
                    isLoading = true;
                    webServiceCall.friendRequestWsCall(getContext(), page);
                } else {
                    Toast.makeText(getActivity(), R.string.no_internet_connect, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
