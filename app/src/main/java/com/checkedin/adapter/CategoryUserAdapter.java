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
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.AdapterCategoryUserBinding;
import com.checkedin.fragment.UserActivityFrg;
import com.checkedin.model.ActivityCategory;

import java.util.ArrayList;

public class CategoryUserAdapter extends RecyclerView.Adapter<CategoryUserAdapter.ViewHolder> {
    private final Fragment fragment;
    private final String friendId;
    private Context context;
    private ArrayList<ActivityCategory> items;

    public CategoryUserAdapter(Context context, ArrayList<ActivityCategory> items, Fragment fragment, String friendId) {
        this.context = context;
        this.items = items;
        this.fragment = fragment;
        this.friendId = friendId;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterCategoryUserBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_category_user, parent, false);
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
        private final AdapterCategoryUserBinding mBinding;

        public ViewHolder(AdapterCategoryUserBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void onItemClick(View v) {
            int position = (Integer) v.getTag();
            if (items.get(position).getActivityCount() > 0) {
                Bundle argument = new Bundle();
                argument.putString("activity_category_id", String.valueOf(items.get(position).getId()));
                argument.putString("friend_id", friendId);
                UserActivityFrg userActivity = new UserActivityFrg();
                userActivity.setArguments(argument);
                ((DialogFragmentContainer) fragment.getParentFragment()).fragmentTransition(userActivity, true);
            }
        }

    }
}