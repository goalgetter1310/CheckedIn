package com.checkedin.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.databinding.AdapterCheckinLocBinding;
import com.checkedin.fragment.CheckinPlacesFrg;
import com.checkedin.fragment.PostCheckinFrg;
import com.checkedin.model.Place;
import com.checkedin.views.StickyHeaderDecoration;

import java.util.ArrayList;


public class CheckinLocAdapter extends RecyclerView.Adapter<CheckinLocAdapter.ViewHolder> implements StickyHeaderDecoration.StickyHeaderAdapter<CheckinLocAdapter.HeaderHolder> {

    private final Context context;
    private final ArrayList<Place> items;
    private final CheckinPlacesFrg checkinPlacesFrg;


    public CheckinLocAdapter(Context context, ArrayList<Place> items, CheckinPlacesFrg checkinPlacesFrg) {
        this.context = context;
        this.items = items;
        this.checkinPlacesFrg = checkinPlacesFrg;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterCheckinLocBinding mBinding = DataBindingUtil.inflate(((MainActivity) context).getLayoutInflater(), R.layout.adapter_checkin_loc, parent, false);
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

    @Override
    public long getHeaderId(int position) {
        return items.get(position).isGooglePlace() ? 1 : 0;
    }

    @Override
    public HeaderHolder onCreateHeaderViewHolder(ViewGroup parent) {
        final View view = LayoutInflater.from(context).inflate(R.layout.adapter_checkin_loc_header, parent, false);
        return new HeaderHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderHolder viewholder, int position) {
        viewholder.tvName.setText(position == 0 ? context.getString(R.string.recommended_by_app) : context.getString(R.string.checkin));
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterCheckinLocBinding mBinding;

        public ViewHolder(AdapterCheckinLocBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

        }

        public void onItemClick(View v) {
            int position = (Integer) v.getTag();

            Bundle extras = new Bundle();
            extras.putParcelable("selected_checin_place", items.get(position));
            PostCheckinFrg postCheckinFrg = new PostCheckinFrg();
            postCheckinFrg.setArguments(extras);

            ((BaseContainerFragment) checkinPlacesFrg.getParentFragment().getParentFragment()).replaceFragment(postCheckinFrg, true);
        }
    }

    public class HeaderHolder extends RecyclerView.ViewHolder {
        private TextView tvName;

        public HeaderHolder(View itemView) {
            super(itemView);
            tvName = (TextView) itemView;
        }


    }
}
