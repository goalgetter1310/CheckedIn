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
import com.checkedin.databinding.AdapterActivitySubCategoryBinding;
import com.checkedin.fragment.ActivitySubCategoryFrg;
import com.checkedin.fragment.PostActivityFrg;
import com.checkedin.model.ActivityCategory;
import com.checkedin.model.ActivitySubCategory;

import java.util.ArrayList;

public class ActivitySubCategoryAdapter extends RecyclerView.Adapter<ActivitySubCategoryAdapter.ViewHolder> {
    private final ActivityCategory activityCategory;
    private Context context;
    private ArrayList<ActivitySubCategory> items;
    private ActivitySubCategoryFrg fragment;
    private boolean isLongPress;
    private int longPressPosition;

    public ActivitySubCategoryAdapter(Context context, ActivityCategory activityCategory, ArrayList<ActivitySubCategory> items, ActivitySubCategoryFrg fragment) {
        this.context = context;
        this.fragment = fragment;
        this.items = items;
        this.activityCategory = activityCategory;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int type) {
        AdapterActivitySubCategoryBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_activity_sub_category, parent, false);

        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ActivitySubCategory activitySubCategory = items.get(position);

        holder.isSelectedForActionEvent = activitySubCategory.getPredefined().equals(ActivitySubCategory.CREATED_BY_USER_SUB_CATEGORY) && activitySubCategory.isActionEvent();

        holder.mBinding.setHolder(holder);
        holder.mBinding.setItems(activitySubCategory);
        holder.mBinding.getRoot().setTag(position);
    }

    public void closeActionEventView() {
        isLongPress = false;
        items.get(longPressPosition).setActionEvent(false);
        notifyItemChanged(longPressPosition);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {

        private final AdapterActivitySubCategoryBinding mBinding;
        private boolean isSelectedForActionEvent;

        public ViewHolder(AdapterActivitySubCategoryBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            mBinding.getRoot().setOnLongClickListener(this);
        }

        public void onItemClick(View v) {
            if (!isLongPress) {
                int position = (Integer) v.getTag();

                PostActivityFrg postActivityFrg = new PostActivityFrg();
                Bundle argument = new Bundle();
                argument.putParcelable("activity_category", activityCategory);
                argument.putParcelable("activity_sub_category", items.get(position));
                postActivityFrg.setArguments(argument);
                ((BaseContainerFragment) fragment.getParentFragment()).replaceFragment(postActivityFrg, true);
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

        public boolean isSelectedForActionEvent() {
            return isSelectedForActionEvent;
        }
    }


}
