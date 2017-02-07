package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.adapter.StarViewUserAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.UserDetail;
import com.checkedin.model.response.StarViewUserModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class StarViewUserFrg extends Fragment implements View.OnClickListener, VolleyStringRequest.AfterResponse {

    private boolean isStar;
    private ArrayList<UserDetail> alUserDetails;
    private  String postId;
    private WebServiceCall webServiceCall;
    private StarViewUserAdapter adptStarViewUser;
    private RecyclerView rvStarViewUser;
    private LinearLayoutManager mLayoutManager;

    private int page, totalRecords;
    private boolean isLoading;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_star_view_user, container, false);

        if (getArguments() != null) {
            isStar = getArguments().getBoolean("is_star");
            postId = getArguments().getString("post_id");
        }


        rvStarViewUser = (RecyclerView) view.findViewById(R.id.rv_star_view_user);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_title);
        ImageView ivBack = (ImageView) view.findViewById(R.id.iv_back);
        mLayoutManager = new LinearLayoutManager(getContext());
        alUserDetails = new ArrayList<>();
        adptStarViewUser = new StarViewUserAdapter(getContext(), alUserDetails);
        webServiceCall = new WebServiceCall(this);
        tvTitle.setText(isStar ? R.string.star : R.string.view);


        webServiceCall.starViewUserWsCall(getContext(), postId, isStar, page);
        rvStarViewUser.setLayoutManager(mLayoutManager);
        rvStarViewUser.setAdapter(adptStarViewUser);

        scrollListenerOnrecyclerView();
        ivBack.setOnClickListener(this);

        return view;
    }


    private void scrollListenerOnrecyclerView() {
        rvStarViewUser.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptStarViewUser.getItemCount() < totalRecords) {
                        page += 10;
                        isLoading = true;
                        webServiceCall.starViewUserWsCall(getContext(), postId, isStar, page);
                    }
                }
            }

        });
    }


    @Override
    public void onResponseReceive(int requestCode) {
        isLoading = false;
        StarViewUserModel mStarViewUser = (StarViewUserModel) webServiceCall.volleyRequestInstatnce().getModelObject(StarViewUserModel.class, StarViewUserModel.class.getSimpleName());
        if (mStarViewUser != null) {
            totalRecords = mStarViewUser.getTotalRecords();
            alUserDetails.addAll(mStarViewUser.getStarViewUser());
            adptStarViewUser.notifyDataSetChanged();
        }

    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        Toast.makeText(getActivity(), R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.iv_back:
                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                    break;
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

    }

}
