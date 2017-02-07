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
import com.checkedin.model.PostActivity;
import com.checkedin.model.response.UserActivityModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class UserActivityFrg extends Fragment implements OnRefreshListener, VolleyStringRequest.AfterResponse {

    private RecyclerView rvFriendActivity;
    private LinearLayout llLoading;
    private SwipeRefreshLayout srlFriendActivity;

    private ArrayList<FriendsActivity> alFriendActivityList;
    private FriendsActivityAdapter adptFriendActivityList;
    private WebServiceCall webServiceCall;
    private LinearLayoutManager mLayoutManager;
    private int totalFriend, page;
    private boolean isLoading;
    private String friendId;
    private String categoryId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_user_activity, container, false);
        Bundle argument = getArguments();

        this.categoryId = argument.getString("activity_category_id", "1");
        this.friendId = argument.getString("friend_id", "");

        initViews(view);

        rvFriendActivity.setLayoutManager(mLayoutManager);
        rvFriendActivity.setAdapter(adptFriendActivityList);


        srlFriendActivity.post(new Runnable() {
            @Override
            public void run() {
                if (webServiceCall.userActivityWsCall(getContext(), categoryId, friendId, page)) {
                    srlFriendActivity.setRefreshing(true);
                    isLoading = true;
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
        ((TextView) view.findViewById(R.id.tv_user_activity_title)).setText(getActivity().getString(R.string.title_activity));
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
                        page += 10;
                        llLoading.setVisibility(LinearLayout.VISIBLE);
                        isLoading = webServiceCall.userActivityWsCall(getContext(), categoryId, friendId, page);

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

        UserActivityModel mFriendActivity = (UserActivityModel) webServiceCall.volleyRequestInstatnce().getModelObject(UserActivityModel.class, UserActivityModel.class.getSimpleName());
        if (mFriendActivity != null) {
            if (mFriendActivity.getData() != null) {
                totalFriend = Integer.parseInt(mFriendActivity.getData().get(0).getPost_count());
                if (page == 0) {
                    alFriendActivityList.clear();
                }
                for (int counter = 0; counter < mFriendActivity.getData().get(0).getActivities().size(); counter++) {
                    FriendsActivity friendsActivity = mFriendActivity.getData().get(0).getActivities().get(counter);
                    friendsActivity.setCategoryName(mFriendActivity.getData().get(0).getvCategoryName());
                    if (friendsActivity.getPostType().equals(PostActivity.POST_TYPE_ACTIVITY))
                        alFriendActivityList.add(friendsActivity);
                }
                adptFriendActivityList.notifyDataSetChanged();

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
        if (webServiceCall.userActivityWsCall(getContext(), categoryId, friendId, page)) {
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
