package com.checkedin.fragment;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.repacked.google.common.reflect.TypeToken;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;

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
import com.checkedin.adapter.SuggestedFriendListAdapter;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.FrgSuggestedFriendListBinding;
import com.checkedin.model.ContactList;
import com.checkedin.model.Friend;
import com.checkedin.model.User;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.FriendListModel;
import com.checkedin.model.response.SuggestedFriends;
import com.checkedin.utility.MyPrefs;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import java.util.ArrayList;
import java.util.List;

public class FriendSuggestionFrg extends Fragment implements View.OnClickListener, FriendAdapter.PageChange, VolleyStringRequest.AfterResponse {

    private ArrayList<User> alFriendList, alAllFriend;
    private SuggestedFriendListAdapter adptFriendList;
    private int page, totalFriend;
    private boolean isSearching,isLoading;
    private FrgSuggestedFriendListBinding mBinding;
    private WebServiceCall webServiceCall;
    private String message="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            mBinding= DataBindingUtil.inflate(inflater,R.layout.frg_suggested_friend_list, container, false);
            initViews();
            final LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            mBinding.rvSuggestedFriendList.setLayoutManager(mLayoutManager);
            mBinding.rvfsSuggestedFriendList.setRecyclerView(mBinding.rvSuggestedFriendList);
            mBinding.rvfsSuggestedFriendList.setViewsToUse(R.layout.recycler_view_fast_scroller, R.id.fastscroller_bubble, R.id.fastscroller_handle);

        String contactList=MyPrefs.getInstance(getActivity()).get("ContactList");
        if(contactList.length()>0)
            webServiceCall.suggestedFriendsCall(getContext(),contactList,false);
            mBinding.rvSuggestedFriendList.setAdapter(adptFriendList);


        return mBinding.getRoot();
    }

    private void initViews() {
        alFriendList = new ArrayList<>();
        alAllFriend = new ArrayList<>();
        webServiceCall = new WebServiceCall(this);
        adptFriendList = new SuggestedFriendListAdapter(getActivity(), R.layout.adapter_friend_list, alFriendList, this);
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(isVisibleToUser){
            if(!message.equals(""))
                Toast.makeText(getActivity(),message, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onPageChange() {
    }


    @Override
    public void onResponseReceive(int requestCode) {
        mBinding.llSuggestedFriendListLoading.setVisibility(LinearLayout.GONE);
        isLoading=false;
        SuggestedFriends mFriendList = (SuggestedFriends) webServiceCall.volleyRequestInstatnce().getModelObject(SuggestedFriends.class, "Suggested Friend Response");
        if (mFriendList != null) {
            if (mFriendList.getStatus() == BaseModel.STATUS_SUCCESS) {
                if (page == 0) {
                    alFriendList.clear();
                    alAllFriend.clear();
                    totalFriend = mFriendList.getUserDetails().size();
//                    if (totalFriend > 0)
//                        ((FriendFrg) getParentFragment()).setFriendListTitle("FRIENDS (" + totalFriend + ")", 0);
                }

                alFriendList.addAll(mFriendList.getUserDetails());
                alAllFriend.addAll(mFriendList.getUserDetails());
                adptFriendList.notifyDataSetChanged();
                message="";
                if (alAllFriend.size() > 0)
                    mBinding.rvSuggestedFriendList.setVisibility(View.VISIBLE);
                else
                    mBinding.rvSuggestedFriendList.setVisibility(View.GONE);
            } else {
                message=mFriendList.getMessage();
            }
        } else {
            message= getString(R.string.server_connect_error);

        }
    }

    @Override
    public void onErrorReceive() {
        mBinding.llSuggestedFriendListLoading.setVisibility(LinearLayout.GONE);
        isLoading=false;
        message= getString(R.string.server_connect_error);
    }


    @Override
    public void onPause() {
        super.onPause();
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mBinding.getRoot().getWindowToken(), 0);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (webServiceCall != null && webServiceCall.volleyRequestInstatnce() != null)
            webServiceCall.volleyRequestInstatnce().cancelAllRequest(WebServiceCall.REQUEST_CODE_FRIEND_LIST);
    }

}
