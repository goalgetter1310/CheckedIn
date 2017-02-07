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
import com.checkedin.adapter.PlanSubCategoryAdapter;
import com.checkedin.dialog.AddActivitySubCategoryDialog;
import com.checkedin.dialog.AddPlanSubCategoryDialog;
import com.checkedin.model.PlanCategory;
import com.checkedin.model.PlanSubCategory;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.PlanAddSubCategoryRes;
import com.checkedin.model.response.PlanSubCategoryRes;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class PlanSubCategoryFrg extends Fragment implements OnClickListener, VolleyStringRequest.AfterResponse, ActionEvent {
    private PlanCategory planCategory;
    private ArrayList<PlanSubCategory> alPlanSubCategory;
    private PlanSubCategoryAdapter adptSubCategory;

    private ManageActionEvent manageActionEvent;
    private RecyclerView rvSubCategory;

    private WebServiceCall webServiceCall;
    private int actionSelectedPosition;
    private int totalrecord;
    private int page;
    private boolean isLoading;
    private LinearLayoutManager mLayoutManager;
    private LinearLayout llLoading;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle arguments = getArguments();
        planCategory = arguments.getParcelable("planning_category");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_plan_sub_category, container, false);


        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(planCategory.getName());
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

        alPlanSubCategory = new ArrayList<>();
        adptSubCategory = new PlanSubCategoryAdapter(getContext(), planCategory, alPlanSubCategory, this);
        mLayoutManager=new LinearLayoutManager(getActivity());
        rvSubCategory.setLayoutManager(mLayoutManager);
        rvSubCategory.setAdapter(adptSubCategory);


        if (alPlanSubCategory.size() <= 0) {
            webServiceCall.planSubCategoryWSCall(getContext(), String.valueOf(planCategory.getId()), page);
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
                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount && alPlanSubCategory.size() < totalrecord) {
                        llLoading.setVisibility(LinearLayout.VISIBLE);
                        page += 10;
                        isLoading = webServiceCall.planSubCategoryWSCall(getContext(), String.valueOf(planCategory.getId()), page);
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
                webServiceCall.planAddSubCategoryWsCall(getContext(), subCategoryName, String.valueOf(alPlanSubCategory.get(0).getCategoryId()));
            }
        }).show();
    }

    @Override
    public void onEdit() {

        adptSubCategory.closeActionEventView();
        final PlanSubCategory planSubCategory = alPlanSubCategory.get(actionSelectedPosition);
        new AddPlanSubCategoryDialog(getContext(), planSubCategory.getName(), new AddPlanSubCategoryDialog.PlanAddSubCategory() {
            @Override
            public void onSubCategoryAdded(String subCategoryName) {
                planSubCategory.setName(subCategoryName);
                adptSubCategory.notifyItemChanged(actionSelectedPosition);
                webServiceCall.editPlanSubCategoryWsCall(getContext(), String.valueOf(planSubCategory.getId()), subCategoryName);
            }
        }).show();

    }

    @Override
    public void onDelete() {
        adptSubCategory.closeActionEventView();
        webServiceCall.deletePlanSubCategoryWsCall(getContext(), String.valueOf(alPlanSubCategory.get(actionSelectedPosition).getId()));
        alPlanSubCategory.remove(actionSelectedPosition);
        adptSubCategory.notifyItemRemoved(actionSelectedPosition);
        adptSubCategory.notifyItemRangeChanged(actionSelectedPosition, adptSubCategory.getItemCount());
    }

    @Override
    public void onBack() {
        adptSubCategory.closeActionEventView();
    }




    @Override
    public void onResponseReceive(int requestCode) {
        llLoading.setVisibility(LinearLayout.GONE);
        isLoading=false;
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_PLAN_SUB_CATEGORY_LIST:
                PlanSubCategoryRes planSubCategoryRes = (PlanSubCategoryRes) webServiceCall.volleyRequestInstatnce().getModelObject(PlanSubCategoryRes.class, PlanSubCategoryRes.class.getSimpleName());
                if (planSubCategoryRes != null) {

                    if (planSubCategoryRes.getStatus() == BaseModel.STATUS_SUCCESS) {
                        totalrecord=planSubCategoryRes.getTotRecords();
                        if (page == 0) {
                            alPlanSubCategory.clear();
                            alPlanSubCategory.addAll(planSubCategoryRes.getPlanSubCategoryList());
                            adptSubCategory.notifyItemInserted(adptSubCategory.getItemCount() - 1);
                        }
                        else {
                            alPlanSubCategory.addAll(planSubCategoryRes.getPlanSubCategoryList());
                            adptSubCategory.notifyItemInserted(adptSubCategory.getItemCount() - 1);
                        }
                    } else {
                        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), planSubCategoryRes.getMessage());
                    }

                } else {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }
                break;
            case WebServiceCall.REQUEST_CODE_ADD_PLANNING_SUB_CATEGORY:
            case WebServiceCall.REQUEST_CODE_EDIT_PLAN_SUB_CATEGORY:
                PlanAddSubCategoryRes planAddSubCategoryRes = (PlanAddSubCategoryRes) webServiceCall.volleyRequestInstatnce().getModelObject(PlanAddSubCategoryRes.class, PlanAddSubCategoryRes.class.getSimpleName());
                if (planAddSubCategoryRes != null) {

                    if (planAddSubCategoryRes.getStatus() == BaseModel.STATUS_SUCCESS && planAddSubCategoryRes.getPlanSubCategory() != null) {
                        if (requestCode == WebServiceCall.REQUEST_CODE_ADD_PLANNING_SUB_CATEGORY) {
                            alPlanSubCategory.add(planAddSubCategoryRes.getPlanSubCategory());
                            adptSubCategory.notifyItemInserted(adptSubCategory.getItemCount());
                        } else {
                            alPlanSubCategory.set(actionSelectedPosition, planAddSubCategoryRes.getPlanSubCategory());
                            adptSubCategory.notifyItemChanged(adptSubCategory.getItemCount());
                        }
                    } else {
                        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), planAddSubCategoryRes.getMessage());
                    }

                } else {
                    Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
                }

                break;

            case WebServiceCall.REQUEST_CODE_DELETE_PLAN_SUB_CATEGORY:
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
        llLoading.setVisibility(LinearLayout.GONE);
        isLoading=false;
        Utility.showSnackBar(getActivity().findViewById(android.R.id.content), getString(R.string.server_connect_error));
    }


    @Override
    public void onDestroyView() {
        manageActionEvent.closeActionView();
        super.onDestroyView();
    }
}
