package com.checkedin.fragment;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
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
import com.checkedin.adapter.HomeScreenAdapter;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.dialog.GuideDialog;

import com.checkedin.dialog.GuideTabDialog;
import com.checkedin.model.ChatList;
import com.checkedin.model.ContactList;
import com.checkedin.tablayout.TintableImageView;
import com.checkedin.utility.MyPrefs;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.google.gson.Gson;

import java.util.LinkedList;

@SuppressLint("ValidFragment")
public class HomeTabFrg extends Fragment implements TabLayout.OnTabSelectedListener {

    private ViewPager viewPager;
    private Menu menu;
    private TabLayout tablayout;
    private int pos=0;
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


    public HomeTabFrg(){

    }
    private HomeScreenAdapter homeScreenAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frg_home_custom_tab, container, false);
        setHasOptionsMenu(true);
        initViews(view);
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

                    ChatListFrg chatListFrg = new ChatListFrg();


                    ((BaseContainerFragment)getParentFragment()).replaceFragment(chatListFrg,true);
//                    DialogFragmentContainer dialogFrgContainerN = DialogFragmentContainer.getInstance();
//                    dialogFrgContainerN.init(chatListFrg);
//                    dialogFrgContainerN.show(getActivity().getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
////
//                    NotificationListFrg notificationListFrg = new NotificationListFrg();
//                    DialogFragmentContainer dialogFrgContainerN = DialogFragmentContainer.getInstance();
//                    dialogFrgContainerN.init(notificationListFrg);
//                    dialogFrgContainerN.show(getActivity().getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
//                    item.setIcon(R.drawable.ic_notify_outline_white_24dp);
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

        ((MainActivity) getActivity()).toggleActionBarIcon(1, true);
        ((MainActivity) getActivity()).showSearch(View.VISIBLE);
        viewPager= (ViewPager) view.findViewById(R.id.frg_home_viewpager);
        tablayout= (TabLayout) view.findViewById(R.id.frg_home_tab_layout);
        homeScreenAdapter=new HomeScreenAdapter(getChildFragmentManager());
        viewPager.setAdapter(homeScreenAdapter);
        tablayout.setupWithViewPager(viewPager);

        for (int groupPosition = 0; groupPosition < res_titles.length; groupPosition++) {
//            tablayout.addTab(tablayout.newTab()
//                            .setCustomView(getTabImage(groupPosition)));

            if(groupPosition==pos){
                tablayout.getTabAt(groupPosition)
                        .setCustomView(getTabSelectedImage(groupPosition));

            }
            else {
                tablayout.getTabAt(groupPosition)
                        .setCustomView(getTabImage(groupPosition));

            }
        }
        viewPager.setCurrentItem(pos);
        tablayout.setOnTabSelectedListener(this);
    }

    private View getTabImage(int res) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.row_custom_tab, null);
        ImageView imageView= (ImageView) view.findViewById(R.id.row_custom_tab_iv);
        imageView.setImageResource(res_titles[res]);
        setImageFilterWhite(imageView);

        return view;
    }

    private View getTabSelectedImage(int res) {
        View view=LayoutInflater.from(getActivity()).inflate(R.layout.row_custom_tab, null);
        ImageView imageView= (ImageView) view.findViewById(R.id.row_custom_tab_iv);
        imageView.setImageResource(res_title_select[res]);
        setImageFilter(imageView);

        return view;
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
    public void onTabSelected(TabLayout.Tab tab) {
       View view=tab.getCustomView();
        ImageView imageView= (ImageView) view.findViewById(R.id.row_custom_tab_iv);
        imageView.setImageResource(res_title_select[tab.getPosition()]);
        setImageFilter(imageView);
        viewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
        View view=tab.getCustomView();
        ImageView imageView= (ImageView) view.findViewById(R.id.row_custom_tab_iv);
        imageView.setImageResource(res_titles[tab.getPosition()]);
        setImageFilterWhite(imageView);
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

}
