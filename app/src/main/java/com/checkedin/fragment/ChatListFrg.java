package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.ChatFriendListAdapter.FriendClickListener;
import com.checkedin.adapter.ChatListAdapter;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.dialog.ChatFriendListDialog;
import com.checkedin.model.ChatList;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.ChatListModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.views.swipe.SwipeOpenItemTouchHelper;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class ChatListFrg extends Fragment implements FriendClickListener, VolleyStringRequest.AfterResponse, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView rvChatList;

    private ChatListAdapter adptChatList;
    private ArrayList<ChatList> alChatList;
    private WebServiceCall webServiceCall;
    private ChatFriendListDialog mFriendListDialog;
    private int page;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading;
    private LinearLayout llLoading;
    private int total;
    private SwipeRefreshLayout srlChatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_chat_list, container, false);
        String[] friendDetails = UserPreferences.fetchChatFriendId(getActivity());
        if (friendDetails != null) {
            Bundle argument = new Bundle();
            argument.putString("friend_id", friendDetails[0]);
            argument.putString("friend_name", friendDetails[1]);
            argument.putString("friend_image_url", friendDetails[2]);
            ChatboxFrg chatboxFrg = new ChatboxFrg();
            chatboxFrg.setArguments(argument);
            ((BaseContainerFragment) getParentFragment()).replaceFragment(chatboxFrg, true);
        } else {
            initViews(view);
            setHasOptionsMenu(true);

            isLoading = true;
            srlChatList.setColorSchemeResources(R.color.colorPrimaryDark, R.color.colorPrimary, R.color.colorPrimary, R.color.colorPrimaryDark);
            srlChatList.post(new Runnable() {
                @Override
                public void run() {
                    srlChatList.setRefreshing(true);
                    webServiceCall.chatListWsCall(getContext(), page, false);
                }
            });
            webServiceCall.chatListWsCall(getContext(), page, false);

            mLayoutManager = new LinearLayoutManager(getActivity());
            rvChatList.setLayoutManager(mLayoutManager);
            rvChatList.setAdapter(adptChatList);
            SwipeOpenItemTouchHelper helper = new SwipeOpenItemTouchHelper(new SwipeOpenItemTouchHelper.SimpleCallback(SwipeOpenItemTouchHelper.START | SwipeOpenItemTouchHelper.END));
            helper.setPreventZeroSizeViewSwipes(true);
            helper.attachToRecyclerView(rvChatList);

            scrollListenerOnrecyclerView();
            srlChatList.setOnRefreshListener(this);
        }

        Utility.logUtils("onCreateView");
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
        menu.findItem(R.id.itm_menu_search_place).setVisible(false);
        menu.findItem(R.id.itm_menu_post).setVisible(false);
        menu.findItem(R.id.itm_menu_chat_friend).setVisible(true);

    }

    private void scrollListenerOnrecyclerView() {
        rvChatList.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && adptChatList.getItemCount() < total) {
                        page += 10;
                        isLoading = true;
                        llLoading.setVisibility(View.VISIBLE);
                        webServiceCall.chatListWsCall(getContext(), page, false);
                    }
                }
            }

        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itm_menu_chat_friend) {
            mFriendListDialog.show();
        }
        return false;
    }

    private void initViews(View view) {

        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_chat_box));

        rvChatList = (RecyclerView) view.findViewById(R.id.rv_chat_list);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_chat_list_loading);
        srlChatList = (SwipeRefreshLayout) view.findViewById(R.id.srl_chat_list);

        alChatList = new ArrayList<>();
        mFriendListDialog = new ChatFriendListDialog(getActivity(), this);
        adptChatList = new ChatListAdapter(getActivity(), alChatList, this);
        webServiceCall = new WebServiceCall(this);
    }


    @Override
    public void onResponseReceive(int requestCode) {
        srlChatList.setRefreshing(false);
        isLoading = false;
        llLoading.setVisibility(View.GONE);
        ChatListModel mChatList = (ChatListModel) webServiceCall.volleyRequestInstatnce().getModelObject(ChatListModel.class, "ChatListModel Response");
        if (mChatList != null) {
            if (mChatList.getStatus() == BaseModel.STATUS_SUCCESS) {
                total = mChatList.getTotRecords();
                if (page == 0)
                    alChatList.clear();

                for (ChatList chatList : mChatList.getData()) {
                    chatList.setCreatedMills();
                    alChatList.add(chatList);
                }
                adptChatList.notifyDataSetChanged();
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mChatList.getMessage());
            }
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        srlChatList.setRefreshing(false);
        isLoading = false;
        llLoading.setVisibility(View.GONE);
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (!Utility.checkInternetConnectivity(getActivity())) {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getActivity().getString(R.string.no_internet_connect));
        }
    }

    public void onResumeCustom() {
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_chat_box));
    }

    @Override
    public void onFriendClicked(Friend friend) {
        if (mFriendListDialog != null) {
            Bundle argument = new Bundle();
            argument.putString("friend_id", friend.getId());
            argument.putString("friend_name", friend.getFullName());
            argument.putString("friend_image_url", friend.getThumbImage());
            ChatboxFrg chatboxFrg = new ChatboxFrg();
            chatboxFrg.setArguments(argument);
            ((BaseContainerFragment) getParentFragment()).replaceFragment(chatboxFrg, true);
            mFriendListDialog.dismiss();
        }
    }

    @Override
    public void onRefresh() {
        isLoading = true;
        page = 0;
        webServiceCall.chatListWsCall(getContext(), page, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
