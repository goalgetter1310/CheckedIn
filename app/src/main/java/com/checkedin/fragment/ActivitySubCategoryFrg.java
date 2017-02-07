package com.checkedin.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.checkedin.ActionEvent;
import com.checkedin.ManageActionEvent;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.adapter.ActivitySubCategoryAdapter;
import com.checkedin.dialog.AddActivitySubCategoryDialog;
import com.checkedin.model.ActivityCategory;
import com.checkedin.model.ActivitySubCategory;
import com.checkedin.model.response.ActivityAddSubCategoryRes;
import com.checkedin.model.response.ActivitySubCategoryRes;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;


public class ActivitySubCategoryFrg extends Fragment implements OnClickListener, VolleyStringRequest.AfterResponse, ActionEvent {
    private ActivityCategory activityCategory;
    private ArrayList<ActivitySubCategory> alActivitySubCategory;
    private ActivitySubCategoryAdapter adptSubCategory;

    private ManageActionEvent manageActionEvent;
    private int totalrecord;
    private int page;
    private boolean isLoading;
    private RecyclerView rvSubCategory;
    private WebServiceCall webServiceCall;
    private int actionSelectedPosition;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout llLoading;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        activityCategory = arguments.getParcelable("activity_category");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_plan_sub_category, container, false);

        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(activityCategory.getName());
        page=0;
        webServiceCall = new WebServiceCall(this);
        manageActionEvent = new ManageActionEvent(getActivity(), this);
        llLoading = (LinearLayout) view.findViewById(R.id.ll_plan_sub_category_loading);

        setHasOptionsMenu(true);

        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
         rvSubCategory = (RecyclerView) view.findViewById(R.id.rv_sub_activity_list);
        FloatingActionButton fabAddSubCategory = (FloatingActionButton) view.findViewById(R.id.fab_sub_activity_add);

        alActivitySubCategory = new ArrayList<>();
        adptSubCategory = new ActivitySubCategoryAdapter(getActivity(), activityCategory, alActivitySubCategory, this);
        mLayoutManager=new LinearLayoutManager(getActivity());
        rvSubCategory.setLayoutManager(mLayoutManager);
        rvSubCategory.setAdapter(adptSubCategory);

        if (alActivitySubCategory.size() <= 0) {
            webServiceCall.activitySubCategoryWSCall(getContext(), UserPreferences.fetchUserId(getContext()), String.valueOf(activityCategory.getId()), page);
        }

        fabAddSubCategory.setOnClickListener(this);
        scrollListenerOnrecyclerView();
    }

    private void scrollListenerOnrecyclerView() {
        rvSubCategory.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = mLayoutManager.getChildCount();
                int totalItemCount = mLayoutManager.getItemCount();
                int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                if (!isLoading) {
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && alActivitySubCategory.size() < totalrecord) {
                        llLoading.setVisibility(LinearLayout.VISIBLE);
                        page += 10;
                        isLoading = webServiceCall.activitySubCategoryWSCall(getContext(),UserPreferences.fetchUserId(getContext()), String.valueOf(activityCategory.getId()), page);
                    }
                }
            }

        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.itm_menu_notification).setVisible(false);
        menu.findItem(R.id.itm_menu_friend).setVisible(false);
    }

    public void showActionEventMenu(boolean isActionEventMenu, int position) {
        this.actionSelectedPosition = position;
        manageActionEvent.showActionEventMenu(isActionEventMenu);
    }


    @Override
    public void onClick(View v) {
        new AddActivitySubCategoryDialog(getContext(), new AddActivitySubCategoryDialog.ActivityAddSubCategory() {
            @Override
            public void onSubCategoryAdded(String subCategoryName) {
                webServiceCall.activityAddSubCategoryWsCall(getContext(), subCategoryName, String.valueOf(alActivitySubCategory.get(0).getCategoryId()));
            }
        }).show();
    }

    @Override
    public void onEdit() {
        adptSubCategory.closeActionEventView();
        final ActivitySubCategory activitySubCategory = alActivitySubCategory.get(actionSelectedPosition);
        new AddActivitySubCategoryDialog(getContext(), activitySubCategory.getName(), new AddActivitySubCategoryDialog.ActivityAddSubCategory() {
            @Override
            public void onSubCategoryAdded(String subCategoryName) {
                activitySubCategory.setName(subCategoryName);
                adptSubCategory.notifyItemChanged(actionSelectedPosition);
                webServiceCall.editActivitySubCategoryWsCall(getContext(), String.valueOf(activitySubCategory.getId()), subCategoryName);
            }
        }).show();

    }

    @Override
    public void onDelete() {
        adptSubCategory.closeActionEventView();
        webServiceCall.deleteActivitySubCategoryWsCall(getContext(), String.valueOf(alActivitySubCategory.get(actionSelectedPosition).getId()));
        alActivitySubCategory.remove(actionSelectedPosition);
        adptSubCategory.notifyItemRemoved(actionSelectedPosition);
        adptSubCategory.notifyItemRangeChanged(actionSelectedPosition, adptSubCategory.getItemCount());
    }

    @Override
    public void onBack() {
        adptSubCategory.closeActionEventView();
    }

    @Override
    public void onResponseReceive(int requestCode) {
    llLoading.setVisibility(View.GONE);
        isLoading=false;
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_ACTIVITY_SUB_CATEGORY:
                ActivitySubCategoryRes activitySubCategoryRes = (ActivitySubCategoryRes) webServiceCall.volleyRequestInstatnce().getModelObject(ActivitySubCategoryRes.class, ActivitySubCategoryRes.class.getSimpleName());
                if (activitySubCategoryRes != null) {

                    if (activitySubCategoryRes.getStatus() == BaseModel.STATUS_SUCCESS) {
                        totalrecord=activitySubCategoryRes.getTotRecords();
                        if (page == 0) {
                            alActivitySubCategory.clear();
                            alActivitySubCategory.addAll(activitySubCategoryRes.getActivitySubCategoryList());
                            adptSubCategory.notifyItemInserted(adptSubCategory.getItemCount() - 1);
                        }
                        else {
                            alActivitySubCategory.addAll(activitySubCategoryRes.getActivitySubCategoryList());
                            adptSubCategory.notifyItemInserted(adptSubCategory.getItemCount() - 1);
                        }
                    } else {
                        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), activitySubCategoryRes.getMessage());
                    }

                } else {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;
            case WebServiceCall.REQUEST_CODE_ADD_SUB_CATEGORY:
            case WebServiceCall.REQUEST_CODE_EDIT_ACTIVITY_SUB_CATEGORY:
                ActivityAddSubCategoryRes activityAddSubCategoryRes = (ActivityAddSubCategoryRes) webServiceCall.volleyRequestInstatnce().getModelObject(ActivityAddSubCategoryRes.class, ActivityAddSubCategoryRes.class.getSimpleName());
                if (activityAddSubCategoryRes != null) {

                    if (activityAddSubCategoryRes.getStatus() == BaseModel.STATUS_SUCCESS) {
                        if (requestCode == WebServiceCall.REQUEST_CODE_ADD_SUB_CATEGORY) {
                            alActivitySubCategory.add(activityAddSubCategoryRes.getActivitySubCategory());
                            adptSubCategory.notifyItemInserted(adptSubCategory.getItemCount());
                        } else {
                            alActivitySubCategory.set(actionSelectedPosition, activityAddSubCategoryRes.getActivitySubCategory());
                            adptSubCategory.notifyItemChanged(adptSubCategory.getItemCount());
                        }
                    } else {
                        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), activityAddSubCategoryRes.getMessage());
                    }

                } else {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }

                break;

            case WebServiceCall.REQUEST_CODE_DELETE_ACTIVITY_SUB_CATEGORY:
                BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
                if (baseModel != null) {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), baseModel.getMessage());
                } else {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;

        }

    }

    @Override
    public void onErrorReceive() {
        llLoading.setVisibility(View.GONE);
        isLoading=false;
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }

    @Override
    public void onDestroyView() {
        manageActionEvent.closeActionView();
        super.onDestroyView();
    }
}
