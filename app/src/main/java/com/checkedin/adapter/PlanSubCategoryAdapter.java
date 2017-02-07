package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.databinding.AdapterPlanSubCategoryBinding;
import com.checkedin.fragment.PlanSubCategoryFrg;
import com.checkedin.fragment.PostPlanFrg;
import com.checkedin.model.ActivitySubCategory;
import com.checkedin.model.PlanCategory;
import com.checkedin.model.PlanSubCategory;
import com.checkedin.utility.Utility;

import java.util.ArrayList;

public class PlanSubCategoryAdapter extends RecyclerView.Adapter<PlanSubCategoryAdapter.ViewHolder> {
    private final PlanCategory planCategory;
    private Context context;
    private ArrayList<PlanSubCategory> items;
    private PlanSubCategoryFrg fragment;
    private boolean isLongPress;
    private int longPressPosition;

    public PlanSubCategoryAdapter(Context context, PlanCategory planCategory, ArrayList<PlanSubCategory> items, PlanSubCategoryFrg fragment) {
        this.context = context;
        this.fragment = fragment;
        this.items = items;
        this.planCategory = planCategory;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        AdapterPlanSubCategoryBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_plan_sub_category, parent, false);

        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PlanSubCategory planSubCategory = items.get(position);

        if (planSubCategory.getPredefined().equals(ActivitySubCategory.CREATED_BY_USER_SUB_CATEGORY) && planSubCategory.isActionEvent()) {
            holder.mBinding.getRoot().setBackgroundColor(Utility.colorRes(context, R.color.divider_light));
        } else {
            holder.mBinding.getRoot().setBackgroundColor(Color.TRANSPARENT);
        }

        holder.mBinding.setHolder(holder);
        holder.mBinding.setItems(planSubCategory);
        holder.mBinding.getRoot().setTag(position);
    }

    public void closeActionEventView() {
        isLongPress = false;
        items.get(longPressPosition).setActionEvent(false);
        notifyItemChanged(longPressPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final AdapterPlanSubCategoryBinding mBinding;


        public ViewHolder(AdapterPlanSubCategoryBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            mBinding.getRoot().setOnLongClickListener(this);
        }

        public void onItemClick(View v) {
            if (!isLongPress) {
                int position = (Integer) v.getTag();

                PostPlanFrg postPlanFrg = new PostPlanFrg();
                Bundle argument = new Bundle();
                argument.putParcelable("planning_category", planCategory);
                argument.putParcelable("planning_sub_category", items.get(position));
                postPlanFrg.setArguments(argument);
                ((BaseContainerFragment) fragment.getParentFragment()).replaceFragment(postPlanFrg, true);
            }
        }


        @Override
        public boolean onLongClick(View v) {
            isLongPress = true;
            longPressPosition = (Integer) v.getTag();
            items.get(longPressPosition).setActionEvent(true);
            if (items.get(longPressPosition).getPredefined().equals(ActivitySubCategory.CREATED_BY_USER_SUB_CATEGORY)) {
                fragment.showActionEventMenu(true, longPressPosition);
            }
            notifyItemChanged(longPressPosition);
            return false;
        }
    }


}
