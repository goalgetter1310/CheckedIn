package com.checkedin.fragment;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.TimelineAllFriendAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.FrgTimelineAllFriendBinding;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class TimelineAllFriendFrg extends Fragment implements VolleyStringRequest.AfterResponse {

    private ArrayList<Friend> alAllFriend;
    private TimelineAllFriendAdapter adptTimelineAllFriend;
    private WebServiceCall webServiceCall;

    private LinearLayoutManager mLayoutManager;
    private FrgTimelineAllFriendBinding mBinding;

    private String userId;
    private int totalFriend;
    private int page;
    private boolean isLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = DataBindingUtil.inflate(getActivity().getLayoutInflater(), R.layout.frg_timeline_all_friend, container, false);

        Bundle extras = getArguments();
        userId = extras.getString("timeline_all_friend_id");

        webServiceCall = new WebServiceCall(this);
        alAllFriend = new ArrayList<>();
        adptTimelineAllFriend = new TimelineAllFriendAdapter(getContext(), alAllFriend);
        mLayoutManager = new LinearLayoutManager(getContext());


        mBinding.rvAllFriends.setLayoutManager(mLayoutManager);
        mBinding.rvAllFriends.setAdapter(adptTimelineAllFriend);
        isLoading = true;
        webServiceCall.friendListWsCall(getContext(), userId, page, true);

        mBinding.rvAllFriends.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptTimelineAllFriend.getItemCount() < totalFriend && !isLoading) {
                    page += 10;
                    isLoading = true;
                    webServiceCall.friendListWsCall(getContext(), userId, page, true);

                }
            }
        });

        mBinding.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                }
            }
        });
        return mBinding.getRoot();
    }


    @Override
    public void onResponseReceive(int requestCode) {
        isLoading = false;
        FriendListModel mFriendList = (FriendListModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendListModel.class, FriendListModel.class.getSimpleName());
        if (mFriendList != null) {
            if (mFriendList.getStatus() == BaseModel.STATUS_SUCCESS) {

                totalFriend = mFriendList.getTotalFriends();

                mBinding.tvTitle.setText(String.format("%s", getString(R.string.friends) + " (" + totalFriend + ")"));

                alAllFriend.addAll(mFriendList.getData());
                adptTimelineAllFriend.notifyDataSetChanged();
            } else {
                Toast.makeText(getContext(), mFriendList.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), R.string.server_connect_error, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        page = 0;
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        Toast.makeText(getContext(), R.string.server_connect_error, Toast.LENGTH_SHORT).show();
    }
}
