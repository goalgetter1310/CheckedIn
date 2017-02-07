package com.checkedin.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.dialog.GuideDialog;
import com.checkedin.dialog.GuideTabDialog;
import com.checkedin.tablayout.TabLayout;
import com.checkedin.tablayout.TintableImageView;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;

public class HomeCustomTabFrg extends Fragment implements OnClickListener { //}, VolleyStringRequest.AfterResponse {
    View lstView;
    private ImageView llCheckIn, llFriendsActivity, llServer, llChatbox, llMyActivity, llFuturePlanning;
    //    private WebServiceCall webServiceCall;
    private Menu menu;
    private TabLayout tablayout;
    private int[] res_titles = {
            R.drawable.ic_tab_home,
            R.drawable.ic_tab_location,
            R.drawable.ic_tab_lounge,
            R.drawable.ic_tab_activity,
            R.drawable.ic_tab_planning};

    private int[] res_title_select = {
            R.drawable.ic_tab_home_h,
            R.drawable.ic_tab_location_h,
            R.drawable.ic_tab_lounge_h,
            R.drawable.ic_tab_activity_h,
            R.drawable.ic_tab_planning_h};
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.frg_home_tab, container, false);
        setHasOptionsMenu(true);
        initViews(view);

//        webServiceCall.notifyCountWsCall(getContext());

        llCheckIn.setOnClickListener(this);
        llFriendsActivity.setOnClickListener(this);
        llServer.setOnClickListener(this);
        llMyActivity.setOnClickListener(this);
        llFuturePlanning.setOnClickListener(this);

        llChatbox.setOnClickListener(this);

        llChatbox.performClick();
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
        llCheckIn = (ImageView) view.findViewById(R.id.ll_home_location);
        tablayout = (TabLayout) view.findViewById(R.id.tab_layout);
        llFriendsActivity = (ImageView) view.findViewById(R.id.ll_home_friends_activity);
        llServer = (ImageView) view.findViewById(R.id.ll_home_checkedin_lounge);
        llChatbox = (ImageView) view.findViewById(R.id.ll_home_chatbox);
        llMyActivity = (ImageView) view.findViewById(R.id.ll_home_my_activity);
        llFuturePlanning = (ImageView) view.findViewById(R.id.ll_home_future_plan);

        for (int groupPosition = 0; groupPosition < res_titles.length; groupPosition++) {
            tablayout.addTab(tablayout.newTab()
                            .setCustomView(getTabImage(groupPosition == 0, res_titles[groupPosition]))
                            .setColor(Color.parseColor("#8A918E"), Color.parseColor("#8A918E"))
                            .setTag(groupPosition)
//                    .setText(res_titles[groupPosition])
            );
        }
        tablayout.setVisibility(View.GONE);
//        for (int i = 0; i < res_titles.length; i++) {
//            TintableImageView tab1 = (TintableImageView) LayoutInflater.from(getActivity()).inflate(R.layout.row_tab, null);
//            tab1.setImageResource(res_titles[i]);
//            tab1.setSelected(i == 0);
//            tablayout.getTabAt(i).setCustomView(tab1);
//        }
    }

    private View getTabImage(boolean isSelect, int res) {
        TintableImageView tab1 = (TintableImageView) LayoutInflater.from(getActivity()).inflate(R.layout.row_tab, null);
        tab1.setImageResource(res);
        tab1.setSelected(isSelect);
        return tab1;
    }

    private void setImageFilter(ImageView v){
        v.setBackgroundColor(getResources().getColor(R.color.color_dark_orange));
        v.setColorFilter(ContextCompat.getColor(getActivity(),R.color.colorPrimary));
    }
    private void setImageFilterWhite(ImageView v){
        v.setBackgroundColor(getResources().getColor(R.color.light_white_));
        v.setColorFilter(ContextCompat.getColor(getActivity(),R.color.color_gray));
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            ((ImageView)view.findViewById(R.id.ll_home_chatbox)).setImageResource(R.drawable.ic_tab_home);
            ((ImageView)view.findViewById(R.id.ll_home_friends_activity)).setImageResource(R.drawable.ic_tab_home);
            setImageFilterWhite((ImageView)view.findViewById(R.id.ll_home_friends_activity));
            ((ImageView)view.findViewById(R.id.ll_home_location)).setImageResource(R.drawable.ic_tab_location);
            setImageFilterWhite((ImageView)view.findViewById(R.id.ll_home_location));
            ((ImageView)view.findViewById(R.id.ll_home_checkedin_lounge)).setImageResource(R.drawable.ic_tab_lounge);
            setImageFilterWhite((ImageView)view.findViewById(R.id.ll_home_checkedin_lounge));
            ((ImageView)view.findViewById(R.id.ll_home_my_activity)).setImageResource(R.drawable.ic_tab_activity);
            setImageFilterWhite((ImageView)view.findViewById(R.id.ll_home_my_activity));
            ((ImageView)view.findViewById(R.id.ll_home_future_plan)).setImageResource(R.drawable.ic_tab_planning);
            setImageFilterWhite((ImageView)view.findViewById(R.id.ll_home_future_plan));
            switch (v.getId()) {
                case R.id.ll_home_chatbox:
                    ((BaseContainerFragment) getParentFragment()).replaceTabFragment(new ChatListFrg(), true);
                    break;
                case R.id.ll_home_friends_activity:
                    ((ImageView)view.findViewById(R.id.ll_home_friends_activity)).setImageResource(R.drawable.ic_tab_home_h);
                    setImageFilter((ImageView)view.findViewById(R.id.ll_home_friends_activity));
                    ((BaseContainerFragment) getParentFragment()).replaceTabFragment(new FriendsActivityFrg(), true);
                    break;
                case R.id.ll_home_location:
                    ((BaseContainerFragment) getParentFragment()).replaceTabFragment(new CheckinPlacesFrg(), true);
                    ((ImageView)view.findViewById(R.id.ll_home_location)).setImageResource(R.drawable.ic_tab_location_h);
                    setImageFilter((ImageView)view.findViewById(R.id.ll_home_location));
                    break;
                case R.id.ll_home_checkedin_lounge:
                    ((BaseContainerFragment) getParentFragment()).replaceTabFragment(new CheckinLoungeFrg(), true);
                    ((ImageView)view.findViewById(R.id.ll_home_checkedin_lounge)).setImageResource(R.drawable.ic_tab_lounge_h);
                    setImageFilter((ImageView)view.findViewById(R.id.ll_home_checkedin_lounge));
                    break;
                case R.id.ll_home_my_activity:
                    ((BaseContainerFragment) getParentFragment()).replaceTabFragment(new ActivityCategoryFrg(), true);
                    ((ImageView)view.findViewById(R.id.ll_home_my_activity)).setImageResource(R.drawable.ic_tab_activity_h);
                    setImageFilter((ImageView)view.findViewById(R.id.ll_home_my_activity));
                    break;
                case R.id.ll_home_future_plan:
                    ((BaseContainerFragment) getParentFragment()).replaceTabFragment(new PlanningCategoryFrg(), true);
                    ((ImageView)view.findViewById(R.id.ll_home_future_plan)).setImageResource(R.drawable.ic_tab_planning_h);
                    setImageFilter((ImageView)view.findViewById(R.id.ll_home_future_plan));
                    break;
            }
        }
//        v.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//        ((ImageView) v).setColorFilter(Color.WHITE);
//        if (lstView != null) {
//            lstView.setBackgroundColor(Color.WHITE);
//            ((ImageView) lstView).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//        }
//        v.setBackgroundColor(Color.WHITE);
//        ((ImageView) v).setColorFilter(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//        if (lstView != null) {
//            lstView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary));
//            ((ImageView) lstView).setColorFilter(Color.WHITE);
//        }
//        lstView = v;
    }


//    @Override
//    public void onResponseReceive(int requestCode) {
//        NotifyCountModel mNotifyCount = (NotifyCountModel) webServiceCall.volleyRequestInstatnce().getModelObject(NotifyCountModel.class, NotifyCountModel.class.getSimpleName());
//        if (mNotifyCount != null) {
//            if (mNotifyCount.getStatus() == BaseModel.STATUS_SUCCESS) {
//                if (mNotifyCount.getData().getUnreadNotification() > 0) {
//                    menu.findItem(R.id.itm_menu_notification).setIcon(R.drawable.ic_notify_alert_outline_white_24dp);
//                }
//            }
//        }
//    }
//
//    @Override
//    public void onErrorReceive() {
//    }
}
