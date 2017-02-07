package com.checkedin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.dialog.GuideDialog;
import com.checkedin.dialog.GuideTabDialog;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.NotifyCountModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

public class HomeFrg extends Fragment implements OnClickListener, VolleyStringRequest.AfterResponse {
    private LinearLayout llCheckIn, llFriendsActivity, llServer, llChatbox, llMyActivity, llFuturePlanning;
    private WebServiceCall webServiceCall;
    private Menu menu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frg_home, container, false);
        setHasOptionsMenu(true);
        initViews(view);

        webServiceCall.notifyCountWsCall(getContext());

        llCheckIn.setOnClickListener(this);
        llFriendsActivity.setOnClickListener(this);
        llServer.setOnClickListener(this);
        llChatbox.setOnClickListener(this);
        llMyActivity.setOnClickListener(this);
        llFuturePlanning.setOnClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (UserPreferences.isFirstTime(getContext())) {
            UserPreferences.removeFirstTime(getContext());
            new GuideTabDialog(getContext()).show();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        menu.findItem(R.id.itm_menu_notification).setVisible(true);
        menu.findItem(R.id.itm_menu_friend).setVisible(true);
        menu.findItem(R.id.itm_menu_search_place).setVisible(false);
        menu.findItem(R.id.itm_menu_post).setVisible(false);
        menu.findItem(R.id.itm_menu_server_filter).setVisible(false);
        this.menu = menu;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (Utility.doubleTapTime + 900 < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (item.getItemId()) {
                case R.id.itm_menu_notification:

                    NotificationListFrg notificationListFrg = new NotificationListFrg();
                    DialogFragmentContainer dialogFrgContainerN = DialogFragmentContainer.getInstance();
                    dialogFrgContainerN.init(notificationListFrg);
                    dialogFrgContainerN.show(getActivity().getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    item.setIcon(R.drawable.ic_notify_outline_white_24dp);
                    break;
                case R.id.itm_menu_friend:
                    FriendFrg friendFrg = new FriendFrg();
                    DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                    dialogFrgContainer.init(friendFrg);
                    dialogFrgContainer.show(getActivity().getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                    break;

            }
        }
        return false;
    }

    private void initViews(View view) {

        ((MainActivity) getActivity()).setToolbarTitle("");
        ((MainActivity) getActivity()).toggleActionBarIcon(1, true);
        ((MainActivity) getActivity()).showSearch(View.VISIBLE);
        llCheckIn = (LinearLayout) view.findViewById(R.id.ll_home_location);
        llFriendsActivity = (LinearLayout) view.findViewById(R.id.ll_home_friends_activity);
        llServer = (LinearLayout) view.findViewById(R.id.ll_home_checkedin_lounge);
        llChatbox = (LinearLayout) view.findViewById(R.id.ll_home_chatbox);
        llMyActivity = (LinearLayout) view.findViewById(R.id.ll_home_my_activity);
        llFuturePlanning = (LinearLayout) view.findViewById(R.id.ll_home_future_plan);

        webServiceCall = new WebServiceCall(this);

    }


    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            switch (v.getId()) {
                case R.id.ll_home_location:
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(new CheckinPlacesFrg(), true);
                    break;
                case R.id.ll_home_friends_activity:
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(new FriendsActivityFrg(), true);
                    break;
                case R.id.ll_home_checkedin_lounge:
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(new CheckinLoungeFrg(), true);
                    break;
                case R.id.ll_home_chatbox:
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(new ChatListFrg(), true);
                    break;
                case R.id.ll_home_my_activity:
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(new ActivityCategoryFrg(), true);
                    break;
                case R.id.ll_home_future_plan:
                    ((BaseContainerFragment) getParentFragment()).replaceFragment(new PlanningCategoryFrg(), true);
                    break;
            }
        }
    }


    @Override
    public void onResponseReceive(int requestCode) {
        NotifyCountModel mNotifyCount = (NotifyCountModel) webServiceCall.volleyRequestInstatnce().getModelObject(NotifyCountModel.class, NotifyCountModel.class.getSimpleName());
        if (mNotifyCount != null) {
            if (mNotifyCount.getStatus() == BaseModel.STATUS_SUCCESS) {
                if (mNotifyCount.getData().getUnreadNotification() > 0) {
                    menu.findItem(R.id.itm_menu_notification).setIcon(R.drawable.ic_notify_alert_outline_white_24dp);
                }
            }
        }
    }

    @Override
    public void onErrorReceive() {
    }
}
