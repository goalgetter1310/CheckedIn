package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.FavouriteAdapter;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class FavouritesFrg extends Fragment implements VolleyStringRequest.AfterResponse {

    private RecyclerView rvFavourites;
    private FavouriteAdapter adptFavourite;
    private WebServiceCall webServiceCall;
    private ArrayList<Friend> alFavourite;
    private int page;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading;
    private int total;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_favourite, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        rvFavourites.setLayoutManager(mLayoutManager);
        rvFavourites.setAdapter(adptFavourite);
        isLoading = true;
        webServiceCall.favListWsCall(getContext(), page);

        scrollListenerOnrecyclerView();
        return view;
    }

    private void initViews(View view) {
        ((MainActivity) getActivity()).toggleActionBarIcon(1, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_favourites));

        rvFavourites = (RecyclerView) view.findViewById(R.id.rv_favourite_list);
        alFavourite = new ArrayList<>();

        mLayoutManager = new LinearLayoutManager(getActivity());
        adptFavourite = new FavouriteAdapter(getActivity(), R.layout.adapter_friend_list, alFavourite);
        webServiceCall = new WebServiceCall(this);
    }

    private void scrollListenerOnrecyclerView() {
        rvFavourites.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptFavourite.getItemCount() < total) {
                        page += 10;
                        isLoading = true;
                        webServiceCall.favListWsCall(getContext(), page);
                    }
                }
            }

        });
    }


    @Override
    public void onResponseReceive(int requestCode) {
        isLoading = false;
        FriendListModel mFavList = (FriendListModel) webServiceCall.volleyRequestInstatnce().getModelObject(FriendListModel.class, "FriendListModel");
        if (mFavList != null) {
            if (mFavList.getStatus() == BaseModel.STATUS_SUCCESS) {
                alFavourite.clear();
                total = mFavList.getTotalFriends();
                alFavourite.addAll(mFavList.getData());
                adptFavourite.notifyDataSetChanged();
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mFavList.getMessage());
            }

        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        isLoading = false;
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
    }

}
