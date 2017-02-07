package com.checkedin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.AnalyticsTrackers;
import com.checkedin.AppController;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.ActivityCategoryAdapter;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.model.ActivityCategory;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.CategoryModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class ActivityCategoryFrg extends Fragment implements VolleyStringRequest.AfterResponse {

    private ArrayList<ActivityCategory> alActivityCategory;
    private ActivityCategoryAdapter adptCategory;
    private WebServiceCall webServiceCall;

    @Override
    public void onStart() {
        super.onStart();
        AppController.getInstance().trackScreenView(AnalyticsTrackers.ANALYTICS_PAGE_MY_ACTIVITY);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            ((MainActivity) getActivity()).ivChat.setVisibility(View.INVISIBLE);
            ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_activity));
            ((MainActivity) getActivity()).showSearch(View.GONE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_category, container, false);
        setHasOptionsMenu(true);


//        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
//        ((MainActivity) getActivity()).showSearch(View.GONE);
//        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_category));

        alActivityCategory = new ArrayList<>();
        adptCategory = new ActivityCategoryAdapter(getContext(), alActivityCategory, this);
        webServiceCall = new WebServiceCall(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rvCategory = (RecyclerView) view.findViewById(R.id.rv_category);
        rvCategory.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvCategory.setAdapter(adptCategory);
        groufinClickEvent(view);

        if (alActivityCategory != null && alActivityCategory.size() == 0)
            webServiceCall.activityCategoryWsCall(getContext(), UserPreferences.fetchUserId(getContext()), null);
    }

    private void groufinClickEvent(View view) {
        view.findViewById(R.id.tv_groufie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseContainerFragment)getParentFragment().getParentFragment()).replaceFragment(new PostGroufieActivityFrg(),true);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
    }


    @Override
    public void onResponseReceive(int requestCode) {
        CategoryModel mCategory = (CategoryModel) webServiceCall.volleyRequestInstatnce().getModelObject(CategoryModel.class, CategoryModel.class.getSimpleName());
        if (mCategory != null) {
            if (mCategory.getStatus() == BaseModel.STATUS_SUCCESS) {
                alActivityCategory.addAll(mCategory.getCategoryList());
                adptCategory.notifyDataSetChanged();
            } else {
                Utility.showSnackBar(getActivity().findViewById(android.R.id.content), mCategory.getMessage());
            }
        } else {
            Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getResources().getString(R.string.server_connect_error));
    }


    public void onResumeCustom() {
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_activity));
    }
}
