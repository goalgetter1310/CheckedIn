package com.checkedin.adapter;

import android.annotation.SuppressLint;
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
import com.checkedin.fragment.SearchFriendFrg;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.Friend;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.views.RecyclerViewFastScroller.BubbleTextGetter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Locale;

@SuppressLint("SimpleDateFormat")
public class FriendSearchAdapter extends RecyclerView.Adapter<FriendSearchAdapter.ViewHolder> implements BubbleTextGetter {
    private Activity activity;
    private int resource;
    private ArrayList<Friend> items;
    private SearchFriendFrg searchDialog;

    public FriendSearchAdapter(SearchFriendFrg searchDialog, Activity activity, int resource, ArrayList<Friend> items) {
        this.activity = activity;
        this.resource = resource;
        this.items = items;
        this.searchDialog = searchDialog;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activity).inflate(resource, parent, false);
//        ((CardView) v).setCardElevation(0);
//        ((CardView) v).setCardBackgroundColor(Color.TRANSPARENT);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvFriendName.setText(items.get(position).getFullName());
        if (!TextUtils.isEmpty(items.get(position).getThumbImage())) {
            Utility.loadImageGlide(    holder.civProfilePic      ,                        items.get(position).getThumbImage());
//                    Picasso.with(activity).load(items.get(position).getThumbImage()).error(R.drawable.ic_placeholder).into(holder.civProfilePic);
        }
        holder.itemView.setTag(position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView tvFriendName;
        private CircleImageView civProfilePic;
        private View itemView;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFriendName = (TextView) itemView.findViewById(R.id.tv_adpt_friend_list_name);
            itemView.findViewById(R.id.tv_adpt_friend_list_checkinTime).setVisibility(View.GONE);
            itemView.findViewById(R.id.tv_adpt_friend_list_checkinPlace).setVisibility(View.GONE);
            civProfilePic = (CircleImageView) itemView.findViewById(R.id.civ_adpt_friend_list_img);
            ViewHolder.this.itemView = itemView;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                Utility.hideKeyboard(activity);
                TimelineFrg friendProfile = new TimelineFrg();
                Bundle argument = new Bundle();
                argument.putString("friend_id",items.get((Integer) v.getTag()).getId());
                friendProfile.setArguments(argument);
                ((DialogFragmentContainer) searchDialog.getParentFragment()).fragmentTransition(friendProfile, true);
            }
        }
    }

    @Override
    public String getTextToShowInBubble(int position) {
        String fullName = items.get(position).getFullName();
        return Character.toString(fullName.toUpperCase(Locale.getDefault()).charAt(0));
    }
}
