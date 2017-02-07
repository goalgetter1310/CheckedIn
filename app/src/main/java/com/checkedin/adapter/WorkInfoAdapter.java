package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.databinding.AdapterWorkInfoBinding;
import com.checkedin.model.WorkInfo;

import java.util.ArrayList;



public class WorkInfoAdapter extends RecyclerView.Adapter<WorkInfoAdapter.ViewHolder> {


    private final ArrayList<WorkInfo> items;
    private final Context context;
    private final boolean isEditable;

    public WorkInfoAdapter(Context context, ArrayList<WorkInfo> items, boolean isEditable) {
        this.context = context;
        this.items = items;
        this.isEditable = isEditable;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterWorkInfoBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_work_info, parent, false);


        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setItems(items.get(position));
        holder.mBinding.setHolder(holder);


        holder.mBinding.getRoot().setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterWorkInfoBinding mBinding;

        public ViewHolder(AdapterWorkInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

        }

        public void onItemClick(View v) {
            int position = (Integer) v.getTag();
            Bundle argument = new Bundle();
            argument.putInt("edit_user_work_info", position);
            ((ProfileActivity) context).manageFragment(5, argument, true);
        }

        public boolean isEditable() {
            return isEditable;
        }
    }
}
