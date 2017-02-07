package com.checkedin.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.databinding.AdapterNearbyBinding;
import com.checkedin.dialog.MapViewDialog;
import com.checkedin.model.Place;

import java.util.ArrayList;



public class NearByAdapter extends RecyclerView.Adapter<NearByAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Place> items;

    public NearByAdapter(Context context, ArrayList<Place> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterNearbyBinding mBinding = DataBindingUtil.inflate(((MainActivity) context).getLayoutInflater(), R.layout.adapter_nearby, parent, false);
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
        private AdapterNearbyBinding mBinding;

        public ViewHolder(AdapterNearbyBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

        public void onItemClick(View v) {
            int position = (Integer) v.getTag();
            new MapViewDialog(context, items.get(position).getLatitude(), items.get(position).getLongitude()).show();
        }
    }
}
