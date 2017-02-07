package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.databinding.AdapterActivityCategoryBinding;
import com.checkedin.fragment.ActivitySubCategoryFrg;
import com.checkedin.fragment.PostActivityFrg;
import com.checkedin.model.ActivityCategory;

import java.util.ArrayList;

public class ActivityCategoryAdapter extends RecyclerView.Adapter<ActivityCategoryAdapter.ViewHolder> {
    private final Fragment fragment;
    private Context context;
    private ArrayList<ActivityCategory> items;

    public ActivityCategoryAdapter(Context context, ArrayList<ActivityCategory> items, Fragment fragment) {
        this.context = context;
        this.items = items;
        this.fragment = fragment;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterActivityCategoryBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_activity_category, parent, false);
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
        private final AdapterActivityCategoryBinding mBinding;

        public ViewHolder(AdapterActivityCategoryBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void onItemClick(View v) {
            int position = (Integer) v.getTag();
            Bundle argument = new Bundle();
            argument.putParcelable("activity_category", items.get(position));
            if (items.get(position).isContainSubCategory()) {
                ActivitySubCategoryFrg activitySubCategoryFrg = new ActivitySubCategoryFrg();
                activitySubCategoryFrg.setArguments(argument);
                ((BaseContainerFragment) fragment.getParentFragment().getParentFragment()).replaceFragment(activitySubCategoryFrg, true);
            } else {
                PostActivityFrg postActivityFrg = new PostActivityFrg();
                postActivityFrg.setArguments(argument);
                ((BaseContainerFragment) fragment.getParentFragment().getParentFragment()).replaceFragment(postActivityFrg, true);
            }
        }

    }
}
