package com.checkedin.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.ChatRoomActivity;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.dialog.CheckinLoungeFilterDialog;
import com.checkedin.model.Friend;
import com.checkedin.model.Place;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.ServerFriendModel;
import com.checkedin.utility.Utility;
import com.checkedin.views.customgridview.GridSimpleArrayAdapter;
import com.checkedin.views.customgridview.GridView;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
public class CheckinLoungeFrg extends Fragment implements OnItemClickListener, VolleyStringRequest.AfterResponse {
    private GridView gvServer;
    private WebServiceCall webServiceCall;
    private GridSimpleArrayAdapter adptServerFriend;
    private ArrayList<Friend> alLastCheckinFriend, alAllLastCheckinFriend;
    private int page;
    private int totalRecords;
    private String searchParams;
    private String checkTitle="";
    private boolean isChat=false,isFirst=true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_server, container, false);
        intiViews(view);
        setHasOptionsMenu(true);

        gvServer.setAdapter(adptServerFriend);
        webServiceCall.checkinLoungeFriendWsCall(getContext(), searchParams, page);


        gvServer.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if ((visibleItemCount + firstVisibleItem) >= totalItemCount && adptServerFriend.getCount() < totalRecords) {
                    page++;
                    webServiceCall.checkinLoungeFriendWsCall(getContext(), searchParams, page);
                }
            }
        });

        gvServer.setOnItemClickListener(this);
        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().trackScreenView(AnalyticsTrackers.ANALYTICS_PAGE_CHECKED_LOUNGE);
    }

    private void intiViews(View view) {
//        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
//        ((MainActivity) getActivity()).showSearch(View.GONE);
        page=0;
        totalRecords=0;
        alLastCheckinFriend = new ArrayList<>();
        alAllLastCheckinFriend = new ArrayList<>();
        webServiceCall = new WebServiceCall(this);
        adptServerFriend = new GridSimpleArrayAdapter(getActivity().getApplicationContext(), alLastCheckinFriend, R.layout.adapter_server_header, R.layout.adapter_server_item);
        gvServer = (GridView) view.findViewById(R.id.gv_server);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser){
            ((MainActivity) getActivity()).setToolbarTitle(checkTitle);
            ((MainActivity) getActivity()).showSearch(View.GONE);
            if(isChat) {
                ((MainActivity) getActivity()).ivChat.setVisibility(View.VISIBLE);
            }
            isFirst=true;
        }
        else{
            isFirst=false;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
        menu.findItem(R.id.itm_menu_search_place).setVisible(false);
        menu.findItem(R.id.itm_menu_post).setVisible(false);
        menu.findItem(R.id.itm_menu_server_filter).setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.itm_menu_server_filter ) {
            String[] friendName = new String[alAllLastCheckinFriend.size()];
            for (int counter = 0; counter < friendName.length; counter++) {
                friendName[counter] = alAllLastCheckinFriend.get(counter).getFullName();
            }

            CheckinLoungeFilterDialog serverFilterList = new CheckinLoungeFilterDialog(getActivity(), friendName, this);
            serverFilterList.show();
        }
        return false;
    }


    @Override
    public void onResponseReceive(int requestCode) {
        ServerFriendModel mServerFriend = (ServerFriendModel) webServiceCall.volleyRequestInstatnce().getModelObject(ServerFriendModel.class, ServerFriendModel.class.getSimpleName());
        if (mServerFriend != null) {
            if (mServerFriend.getStatus() == BaseModel.STATUS_SUCCESS) {
                final Place lastCheckinLoc = mServerFriend.getData().getPlaceData();
                if(lastCheckinLoc.getName().trim().toString().length()>0)
                checkTitle = lastCheckinLoc.getName()+" Lounge";

//                ((MainActivity) getActivity()).setToolbarTitle(lastCheckinLoc.getName());
                totalRecords = mServerFriend.getData().getTotRecords();
                if (page == 0) {
                    alLastCheckinFriend.clear();
                    alAllLastCheckinFriend.clear();
                }
                alLastCheckinFriend.addAll(mServerFriend.getData().getUserData());

                alAllLastCheckinFriend.addAll(mServerFriend.getData().getUserData());
                adptServerFriend.notifyDataSetChanged();

                if (alAllLastCheckinFriend.size() > 0) {
                    isChat = true;
                    if(isFirst) {
                        ((MainActivity) getActivity()).ivChat.setVisibility(View.VISIBLE);
                        ((MainActivity) getActivity()).setToolbarTitle(checkTitle);
                    }
                    ((MainActivity) getActivity()).ivChat.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            startActivity(new Intent(getActivity(), ChatRoomActivity.class).putExtra("locId", lastCheckinLoc.getId())
                                    .putExtra("locName", lastCheckinLoc.getName()));
                        }
                    });

                }
                else{
                    isChat = false;
                    ((MainActivity) getActivity()).ivChat.setVisibility(View.INVISIBLE);
                }
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mServerFriend.getMessage());
            }
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getActivity().getResources().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getActivity().getResources().getString(R.string.server_connect_error));
    }

    public void onFilterApply(String searchParams) {
        page = 0;
        this.searchParams = searchParams;
        webServiceCall.checkinLoungeFriendWsCall(getContext(), searchParams, page);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long arg3) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            TimelineFrg friendList = new TimelineFrg();
            Bundle argument = new Bundle();
            argument.putString("friend_id", alLastCheckinFriend.get(position).getId());
            friendList.setArguments(argument);
            DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
            dialogFrgContainer.init(friendList);
            dialogFrgContainer.show(getActivity().getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
        }
    }
}
