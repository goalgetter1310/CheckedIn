package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.FriendsActivityAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.FriendsActivity;
import com.checkedin.model.response.UseCheckinModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class UserCheckinFrg extends Fragment implements OnRefreshListener, VolleyStringRequest.AfterResponse {

    private RecyclerView rvFriendActivity;
    private LinearLayout llLoading;
    private SwipeRefreshLayout srlFriendActivity;

    private ArrayList<FriendsActivity> alFriendActivityList;
    private FriendsActivityAdapter adptFriendActivityList;
    private WebServiceCall webServiceCall;
    private LinearLayoutManager mLayoutManager;
    private int totalRecord, page;
    private boolean isLoading;
    private String friendId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_user_activity, container, false);
        initViews(view);

        friendId = getArguments().getString("friend_id");
        rvFriendActivity.setLayoutManager(mLayoutManager);
        rvFriendActivity.setAdapter(adptFriendActivityList);

        srlFriendActivity.post(new Runnable() {
            @Override
            public void run() {
                if (webServiceCall.userCheckinWsCall(getContext(), friendId, page)) {
                    isLoading = true;
                    srlFriendActivity.setRefreshing(true);
                }
            }
        });
        srlFriendActivity.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        scrollListenerOnrecyclerView();

        srlFriendActivity.setOnRefreshListener(this);

        return view;
    }

    private void initViews(View view) {

        webServiceCall = new WebServiceCall(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        alFriendActivityList = new ArrayList<>();
        adptFriendActivityList = new FriendsActivityAdapter(getActivity(), alFriendActivityList, this);

        srlFriendActivity = (SwipeRefreshLayout) view.findViewById(R.id.srl_friend_activity);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_friend_activity_loading);
        rvFriendActivity = (RecyclerView) view.findViewById(R.id.rv_friend_activity);

        view.findViewById(R.id.iv_user_activity_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                }
            }
        });
        ((TextView) view.findViewById(R.id.tv_user_activity_title)).setText(getActivity().getString(R.string.title_location));
    }

    private void scrollListenerOnrecyclerView() {
        rvFriendActivity.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading) {
                    srlFriendActivity.setRefreshing(false);
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && alFriendActivityList.size() < totalRecord) {
                        page += 10;
                        if (webServiceCall.userCheckinWsCall(getContext(), friendId, page)) {
                            llLoading.setVisibility(LinearLayout.VISIBLE);
                            isLoading = true;
                        }
                    }
                }
            }

        });
    }


    @Override
    public void onResponseReceive(int requestCode) {
        isLoading = false;
        llLoading.setVisibility(LinearLayout.GONE);
        srlFriendActivity.setRefreshing(false);

        UseCheckinModel mFriendActivity = (UseCheckinModel) webServiceCall.volleyRequestInstatnce().getModelObject(UseCheckinModel.class, UseCheckinModel.class.getSimpleName());
        if (mFriendActivity != null) {
            if (mFriendActivity.getData() != null) {
                totalRecord = mFriendActivity.getData().getTotalCheckins();
                if (page == 0) {
                    alFriendActivityList.clear();
                    alFriendActivityList.addAll(mFriendActivity.getData().getCheckins());
                    adptFriendActivityList.notifyDataSetChanged();
                } else {
                    alFriendActivityList.addAll(mFriendActivity.getData().getCheckins());
                    adptFriendActivityList.notifyItemInserted(adptFriendActivityList.getItemCount() - 1);
                }

            } else {
                Toast.makeText(getActivity(), mFriendActivity.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        srlFriendActivity.setRefreshing(false);
        llLoading.setVisibility(LinearLayout.GONE);
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        srlFriendActivity.setRefreshing(true);
        page = 0;
        if (webServiceCall.userCheckinWsCall(getContext(), friendId, page)) {
            isLoading = true;
        } else {
            srlFriendActivity.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        page = 0;
    }
}
