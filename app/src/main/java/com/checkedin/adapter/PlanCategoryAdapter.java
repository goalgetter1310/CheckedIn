package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.databinding.AdapterPlanCategoryBinding;
import com.checkedin.fragment.PlanSubCategoryFrg;
import com.checkedin.fragment.PlanningCategoryFrg;
import com.checkedin.fragment.PostPlanFrg;
import com.checkedin.model.PlanCategory;

import java.util.ArrayList;

public class PlanCategoryAdapter extends RecyclerView.Adapter<PlanCategoryAdapter.ViewHolder> {
    private final PlanningCategoryFrg fragment;
    private Context context;
    private ArrayList<PlanCategory> items;


    public PlanCategoryAdapter(Context context, ArrayList<PlanCategory> items, PlanningCategoryFrg fragment) {
        this.context = context;
        this.items = items;
        this.fragment = fragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterPlanCategoryBinding mBinding = DataBindingUtil.inflate(
                ((Activity) context).getLayoutInflater(), R.layout.adapter_plan_category, parent, false);


        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setHolder(holder);
        holder.mBinding.setItems(items.get(position));
        holder.mBinding.getRoot().setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private final AdapterPlanCategoryBinding mBinding;

        public ViewHolder(AdapterPlanCategoryBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void onItemClick(View v) {
            int position = (Integer) v.getTag();
            Bundle argument = new Bundle();
            argument.putParcelable("planning_category", items.get(position));
            if (items.get(position).isContainSubCategory()) {
                PlanSubCategoryFrg planSubCategoryFrg = new PlanSubCategoryFrg();
                planSubCategoryFrg.setArguments(argument);
                ((BaseContainerFragment) fragment.getParentFragment().getParentFragment()).replaceFragment(planSubCategoryFrg, true);
            } else {
                PostPlanFrg postPlanFrg = new PostPlanFrg();
                postPlanFrg.setArguments(argument);
                ((BaseContainerFragment) fragment.getParentFragment().getParentFragment()).replaceFragment(postPlanFrg, true);
            }
        }
    }
}