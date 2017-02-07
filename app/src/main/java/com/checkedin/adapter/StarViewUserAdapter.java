package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.AdapterStarViewUserBinding;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.Photos;
import com.checkedin.model.UserDetail;
import com.checkedin.utility.Utility;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class StarViewUserAdapter extends RecyclerView.Adapter<StarViewUserAdapter.ViewHolder> {
    private Context context;
    private ArrayList<UserDetail> items;

    public StarViewUserAdapter(Context context, ArrayList<UserDetail> items) {
        this.context = context;
        this.items = items;

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        String url=WebServiceCall.IMAGE_BASE_PATH + Photos.PROFILE_PATH + Photos.THUMB_SIZE_PATH + items.get(position).getImageUrl();
        Utility.loadImageGlide( holder.mBinding.civProfileImg,    url);
                Glide.with(context).load(url).error(R.drawable.ic_placeholder).into(holder.mBinding.civProfileImg);
        holder.mBinding.tvName.setText(items.get(position).getFullName());

        holder.itemView.setTag(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int resource) {
        AdapterStarViewUserBinding mBinding = DataBindingUtil.inflate(((Activity)context).getLayoutInflater(), R.layout.adapter_star_view_user, parent, false);
        return new ViewHolder(mBinding);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {

        private AdapterStarViewUserBinding mBinding;

        public ViewHolder(AdapterStarViewUserBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                TimelineFrg friendProfile = new TimelineFrg();
                Bundle argument = new Bundle();
                argument.putString("friend_id", items.get((Integer) v.getTag()).getId());
                friendProfile.setArguments(argument);

                if (DialogFragmentContainer.isDialogOpen) {
                    DialogFragmentContainer.getInstance().fragmentTransition(friendProfile, true);
                } else {
                    DialogFragmentContainer dialogFrgContainer = DialogFragmentContainer.getInstance();
                    dialogFrgContainer.init(friendProfile);
                    dialogFrgContainer.show(((MainActivity) context).getSupportFragmentManager(), DialogFragmentContainer.class.getSimpleName());
                }
            }
        }
    }

}
