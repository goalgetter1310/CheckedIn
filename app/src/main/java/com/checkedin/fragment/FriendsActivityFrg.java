package com.checkedin.fragment;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.FriendsActivityAdapter;
import com.checkedin.model.FriendsActivity;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendActivityModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class FriendsActivityFrg extends Fragment implements OnRefreshListener, VolleyStringRequest.AfterResponse {

    private RecyclerView rvFriendActivity;
    private LinearLayout llLoading;
    private SwipeRefreshLayout srlFriendActivity;

    private ArrayList<FriendsActivity> alFriendActivityList;
    private FriendsActivityAdapter adptFriendActivityList;
    private WebServiceCall webServiceCall;
    private Resources resource;
    private LinearLayoutManager mLayoutManager;
    private int totalFriend;
    private int page;
    private boolean isLoading;
    private boolean isRefresh;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_friends_activity, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        rvFriendActivity.setLayoutManager(mLayoutManager);
        rvFriendActivity.setAdapter(adptFriendActivityList);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().trackScreenView(AnalyticsTrackers.ANALYTICS_PAGE_FRIEND_ACTIVITY);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        srlFriendActivity.post(new Runnable() {
            @Override
            public void run() {

                if (webServiceCall.friendActivityWsCall(getContext(), page)) {
                    isLoading = true;
                    srlFriendActivity.setRefreshing(true);
                }
            }
        });
        srlFriendActivity.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        scrollListenerOnrecyclerView();

        srlFriendActivity.setOnRefreshListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.itm_menu_notification).setVisible(true);
        menu.findItem(R.id.itm_menu_friend).setVisible(true);
        Drawable firstIcon = menu.findItem(R.id.itm_menu_notification).getIcon();
        firstIcon.mutate();
        firstIcon.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_IN);
    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            ((MainActivity) getActivity()).ivChat.setVisibility(View.INVISIBLE);
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_checkedlin));
            ((MainActivity) getActivity()).showSearch(View.VISIBLE);
        }
    }



    private void initViews(View view) {

//        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
//        ((MainActivity) getActivity()).showSearch(View.GONE);
//        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_friend_activity));

        resource = getResources();
        webServiceCall = new WebServiceCall(this);
        mLayoutManager = new LinearLayoutManager(getContext());
        alFriendActivityList = new ArrayList<>();
        adptFriendActivityList = new FriendsActivityAdapter(getActivity(), alFriendActivityList, this);

        srlFriendActivity = (SwipeRefreshLayout) view.findViewById(R.id.srl_friend_activity);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_friend_activity_loading);
        rvFriendActivity = (RecyclerView) view.findViewById(R.id.rv_friend_activity);

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
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && alFriendActivityList.size() < totalFriend) {
                        llLoading.setVisibility(LinearLayout.VISIBLE);
                        page += 10;
                        isLoading = webServiceCall.friendActivityWsCall(getContext(), page);
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

        FriendActivityModel mFriendActivity = (FriendActivityModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendActivityModel.class, "Friend Activity");
        if (mFriendActivity != null) {
            if (mFriendActivity.getStatus() == BaseModel.STATUS_SUCCESS) {
                totalFriend = Integer.parseInt(mFriendActivity.getData().getTotalRecords());
                if (page == 0) {
                    alFriendActivityList.clear();
                    alFriendActivityList.addAll(mFriendActivity.getData().getRecords());
                    adptFriendActivityList.notifyDataSetChanged();
                } else {
                    alFriendActivityList.addAll(mFriendActivity.getData().getRecords());
                    adptFriendActivityList.notifyItemInserted(adptFriendActivityList.getItemCount() - 1);
                }

            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mFriendActivity.getMessage());
            }
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), resource.getString(R.string.server_connect_error));
        }
        isRefresh = false;
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        srlFriendActivity.setRefreshing(false);
        llLoading.setVisibility(LinearLayout.GONE);
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), resource.getString(R.string.server_connect_error));
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        srlFriendActivity.setRefreshing(true);
        page = 0;
        if (webServiceCall.friendActivityWsCall(getContext(), page)) {
            isLoading = true;
        } else {
            srlFriendActivity.setRefreshing(false);
        }
    }

    @Override
    public void onDestroyView() {
        webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_FRIEND_ACTIVITY);
        super.onDestroyView();
    }
}
