package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.databinding.AdapterTimelineFriendBinding;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.Friend;
import com.checkedin.utility.Utility;

import java.util.ArrayList;


public class TimelineFriendAdapter extends RecyclerView.Adapter<TimelineFriendAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<Friend> items;

    public TimelineFriendAdapter(Context context, ArrayList<Friend> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterTimelineFriendBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_timeline_friend, parent, false);

        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Utility.loadImageGlide(holder.mBinding.ivProfileImg, items.get(position).getThumbImage());
        holder.mBinding.tvName.setText(items.get(position).getFullName());

        holder.mBinding.getRoot().setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size() <= 6 ? items.size() : 6;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private AdapterTimelineFriendBinding mBinding;

        public ViewHolder(AdapterTimelineFriendBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;

            mBinding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int position = (Integer) v.getTag();

            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                TimelineFrg friendProfile = new TimelineFrg();

                Bundle argument = new Bundle();
                argument.putString("friend_id", items.get(position).getId());
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
