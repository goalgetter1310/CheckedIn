package com.checkedin.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.fragment.FriendListFrg;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.Friend;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.views.RecyclerViewFastScroller.BubbleTextGetter;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.ViewHolder> implements BubbleTextGetter {
    private Activity activity;
    private int resource;
    private ArrayList<Friend> items;
    private FriendListFrg friendListFrg;

    public FriendListAdapter(Activity activity, int resource, ArrayList<Friend> items, FriendListFrg friendListFrg) {
        this.activity = activity;
        this.resource = resource;
        this.items = items;
        this.friendListFrg = friendListFrg;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(resource, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvFriendName.setText(items.get(position).getFullName());
        String checkinDate = items.get(position).getLastCheckinTime();
        String checkinPlace = items.get(position).getLastCheckinLoc();
        if (!TextUtils.isEmpty(checkinDate)) {
            holder.tvLastCheckinTime.setVisibility(View.VISIBLE);
            holder.tvLastCheckinTime.setText(activity.getString(R.string.last_checkin_time, Utility.formatSampleDate(checkinDate)));
        } else {
            holder.tvLastCheckinTime.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(checkinPlace)) {
            holder.tvLastCheckinPlace.setVisibility(View.VISIBLE);
            holder.tvLastCheckinPlace.setText(activity.getString(R.string.last_checkin_loc, checkinPlace));
        } else {
            holder.tvLastCheckinPlace.setVisibility(View.GONE);
        }
        Utility.loadImageGlide(  holder.civProfilePic                              ,items.get(position).getThumbImage());
//                Picasso.with(activity).load(items.get(position).getThumbImage()).error(R.drawable.ic_placeholder).into(holder.civProfilePic);
        holder.itemView.setTag(position);

    }


    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView tvFriendName, tvLastCheckinPlace, tvLastCheckinTime;
        private CircleImageView civProfilePic;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFriendName = (TextView) itemView.findViewById(R.id.tv_adpt_friend_list_name);
            tvLastCheckinPlace = (TextView) itemView.findViewById(R.id.tv_adpt_friend_list_checkinPlace);
            tvLastCheckinTime = (TextView) itemView.findViewById(R.id.tv_adpt_friend_list_checkinTime);
            civProfilePic = (CircleImageView) itemView.findViewById(R.id.civ_adpt_friend_list_img);
            ViewHolder.this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                TimelineFrg friendProfile = new TimelineFrg();
                Bundle argument = new Bundle();
                argument.putString("friend_id",items.get((Integer) v.getTag()).getId());
                friendProfile.setArguments(argument);

                ((DialogFragmentContainer) friendListFrg.getParentFragment().getParentFragment()).fragmentTransition(friendProfile, true);
            }
        }

    }

    @Override
    public String getTextToShowInBubble(int position) {
        String fullName = items.get(position).getFullName();
        return Character.toString(fullName.toUpperCase(Locale.getDefault()).charAt(0));
    }
}
