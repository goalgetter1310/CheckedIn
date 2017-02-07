package com.checkedin.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.NotificationListAdapter;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.model.Notification;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.NotificationListModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;
public class NotificationListFrg extends Fragment implements VolleyStringRequest.AfterResponse {

    private RecyclerView rvNotificationList;
    private WebServiceCall webServiceCall;
    private NotificationListAdapter adptNotificationList;
    private ArrayList<Notification> alNotificationList;
    private LinearLayoutManager mLayoutManager;
    private boolean isLoading;
    private int page, totalNotify;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_notification_list, container, false);
        initViews(view);
        rvNotificationList.setLayoutManager(mLayoutManager);
        rvNotificationList.setAdapter(adptNotificationList);


        isLoading = webServiceCall.notifyListWsCall(getContext(), page);
        scrollListenerOnrecyclerView();

        ImageView ivBack = (ImageView) view.findViewById(R.id.iv_notification_back);
        ivBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                    Utility.doubleTapTime = System.currentTimeMillis();
//                    ((DialogFragmentContainer) getParentFragment()).popFragment();
                    ((BaseContainerFragment) getParentFragment()).popFragment();
                }
            }
        });

        return view;
    }

    private void initViews(View view) {
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_notifications));
        rvNotificationList = (RecyclerView) view.findViewById(R.id.rv_notification_list);
        alNotificationList = new ArrayList<>();
        adptNotificationList = new NotificationListAdapter(getActivity(), R.layout.adapter_notification_list, alNotificationList, this);
        webServiceCall = new WebServiceCall(this);
        mLayoutManager = new LinearLayoutManager(getActivity());
    }

    private void scrollListenerOnrecyclerView() {
        rvNotificationList.addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && alNotificationList.size() < totalNotify) {
                        page += 10;
                        isLoading = webServiceCall.notifyListWsCall(getContext(), page);
                    }
                }
            }

        });
    }


    @Override
    public void onResponseReceive(int requestCode) {
        NotificationListModel mNotificationList = (NotificationListModel) webServiceCall.volleyRequestInstatnce().getModelObject(NotificationListModel.class, NotificationListModel.class.getSimpleName());
        if (mNotificationList != null) {
            if (mNotificationList.getStatus() == BaseModel.STATUS_SUCCESS) {
                totalNotify = mNotificationList.getData().getTotalRecords();
                alNotificationList.addAll(mNotificationList.getData().getNotifications());
                adptNotificationList.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), mNotificationList.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(getActivity(), getActivity().getString(R.string.server_connect_error), Toast.LENGTH_LONG).show();
        }
        isLoading = false;
    }

    @Override
    public void onErrorReceive() {
        try {
            Toast.makeText(getActivity(), getActivity().getString(R.string.server_connect_error), Toast.LENGTH_LONG).show();
            isLoading = false;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
