package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.databinding.AdapterLivingPlaceInfoBinding;
import com.checkedin.model.LivingPlace;

import java.util.ArrayList;


public class LivingPlaceInfoAdapter extends RecyclerView.Adapter<LivingPlaceInfoAdapter.ViewHolder> {


    private final ArrayList<LivingPlace> items;
    private final Context context;
    private final boolean isEditable;

    public LivingPlaceInfoAdapter(Context context, ArrayList<LivingPlace> items, boolean isEditable) {
        this.context = context;
        this.items = items;
        this.isEditable = isEditable;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterLivingPlaceInfoBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_living_place_info, parent, false);

        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mBinding.setHolder(holder);
        holder.mBinding.setItems(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterLivingPlaceInfoBinding mBinding;

        public ViewHolder(AdapterLivingPlaceInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

        }


        public void onItemClick(View v) {
            ((ProfileActivity) context).manageFragment(3, null, true);
        }

        public boolean isEditable() {
            return isEditable;
        }
    }
}
