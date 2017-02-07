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
import com.checkedin.model.response.UserPlanningModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class UserPlanningFrg extends Fragment implements OnRefreshListener, VolleyStringRequest.AfterResponse {

    private RecyclerView rvUserPlanning;
    private LinearLayout llLoading;
    private SwipeRefreshLayout srlUserPlanning;

    private ArrayList<FriendsActivity> alFriendActivityList;
    private FriendsActivityAdapter adptUserPlanningList;
    private WebServiceCall webServiceCall;
    private LinearLayoutManager mLayoutManager;
    private int totalFriend, page;
    private boolean isLoading;
    private String friendId;

//    public UserPlanningFrg(String friendId) {
//        this.friendId = friendId;
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_user_planning, container, false);
        initViews(view);

        friendId = getArguments().getString("friend_id");

        rvUserPlanning.setLayoutManager(mLayoutManager);
        rvUserPlanning.setAdapter(adptUserPlanningList);

        srlUserPlanning.post(new Runnable() {
            @Override
            public void run() {
                if (webServiceCall.futurePlanningWsCall(getContext(), friendId, page)) {
                    isLoading = true;
                    srlUserPlanning.setRefreshing(true);
                }

            }
        });
        srlUserPlanning.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
        scrollListenerOnrecyclerView();

        srlUserPlanning.setOnRefreshListener(this);

        return view;
    }

    private void initViews(View view) {

        webServiceCall = new WebServiceCall(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
        alFriendActivityList = new ArrayList<>();
        adptUserPlanningList = new FriendsActivityAdapter(getActivity(), alFriendActivityList, this);

        srlUserPlanning = (SwipeRefreshLayout) view.findViewById(R.id.srl_user_planning);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_user_planning_loading);
        rvUserPlanning = (RecyclerView) view.findViewById(R.id.rv_user_planning);

        view.findViewById(R.id.iv_user_planning_back).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                }
            }
        });
        ((TextView) view.findViewById(R.id.tv_user_planning_title)).setText(getActivity().getString(R.string.title_future_planning));
    }

    private void scrollListenerOnrecyclerView() {
        rvUserPlanning.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading) {
                    srlUserPlanning.setRefreshing(false);
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && alFriendActivityList.size() < totalFriend) {
                        page += 10;
                        llLoading.setVisibility(LinearLayout.VISIBLE);
                        if (webServiceCall.futurePlanningWsCall(getContext(), friendId, page)) {
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
        srlUserPlanning.setRefreshing(false);

        UserPlanningModel mUserPlanning = (UserPlanningModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserPlanningModel.class, UserPlanningModel.class.getSimpleName());
        if (mUserPlanning != null) {
            if (mUserPlanning.getData() != null) {
                totalFriend = mUserPlanning.getData().getTotRecords();
                if (page == 0)
                    alFriendActivityList.clear();
                alFriendActivityList.addAll(mUserPlanning.getData().getRecords());
                adptUserPlanningList.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), mUserPlanning.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        srlUserPlanning.setRefreshing(false);
        llLoading.setVisibility(LinearLayout.GONE);
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRefresh() {
        srlUserPlanning.setRefreshing(true);
        page = 0;
        if (webServiceCall.futurePlanningWsCall(getContext(), friendId, page)) {
            isLoading = true;
        } else {
            srlUserPlanning.setRefreshing(false);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        page = 0;
    }
}
