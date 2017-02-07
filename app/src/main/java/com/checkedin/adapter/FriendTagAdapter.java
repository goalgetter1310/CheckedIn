package com.checkedin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.checkedin.R;
import com.checkedin.model.Friend;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.views.RecyclerViewFastScroller.BubbleTextGetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FriendTagAdapter extends RecyclerView.Adapter<FriendTagAdapter.ViewHolder> implements BubbleTextGetter, OnClickListener {
    private Context context;
    private int resource;
    private List<Friend> items, itemsTemp;
    private TagFriendListener friendClickListener;
    private boolean isSelection = false;

    public FriendTagAdapter(Context context, int resource, List<Friend> items, TagFriendListener friendClickListener, boolean isSelection) {
        this.isSelection = isSelection;
        this.friendClickListener = friendClickListener;
        this.context = context;
        this.resource = resource;
        this.items = new ArrayList<>();
        this.items.addAll(items);
        this.itemsTemp = new ArrayList<>();
        this.itemsTemp.addAll(items);
    }

//	public FriendTagAdapter(Context context, int resource, List<Friend> items) {
//		this.context = context;
//		this.resource = resource;
//		this.items = new ArrayList<>();
//		this.items.addAll(items);
//		this.itemsTemp = new ArrayList<>();
//		this.itemsTemp.addAll(items);
//	}

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(resource, parent, false);
        if (friendClickListener != null) {
            v.setOnClickListener(FriendTagAdapter.this);
        }
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvFriendName.setTag(items.get(position));
        holder.tvFriendName.setText(items.get(position).getFullName());
        Utility.loadImageGlide(holder.civProfilePic,items.get(position).getThumbImage());
//        Glide.with(context).load(items.get(position).getThumbImage()).error(R.drawable.ic_placeholder).into(holder.civProfilePic);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvFriendName;
        private CircleImageView civProfilePic;
        private ImageButton imgButton;

        public ViewHolder(View itemView) {
            super(itemView);
            tvFriendName = (TextView) itemView.findViewById(R.id.tv_adpt_friend_list_name);
            civProfilePic = (CircleImageView) itemView.findViewById(R.id.civ_adpt_friend_list_img);
            imgButton = (ImageButton) itemView.findViewById(R.id.civ_adpt_friend_list_img_btn);
            if (isSelection)
                imgButton.setVisibility(View.VISIBLE);
            else
                imgButton.setVisibility(View.GONE);
        }
    }

    @Override
    public String getTextToShowInBubble(int pos) {
        return Character.toString(items.get(pos).getFirstName().charAt(0));
    }

//	public void addFriends(List<Friend> friends) {
//		items.addAll(friends);
//		itemsTemp.addAll(friends);
//		notifyDataSetChanged();
//	}

    public void search(String query) {
        this.items.clear();
        if (query.equals("")) {
            this.items.addAll(itemsTemp);
            notifyDataSetChanged();
            return;
        }
        query = query.toLowerCase(Locale.getDefault());
        for (Friend friend : itemsTemp) {
            if (friend.getFullName().toLowerCase(Locale.getDefault()).contains(query)) {
                this.items.add(friend);
            }
        }
        notifyDataSetChanged();
    }

    public interface TagFriendListener {
        void onTagFriend(View v, boolean isTagged, String label);
    }

    @Override
    public void onClick(View v) {
        if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
            Utility.doubleTapTime = System.currentTimeMillis();
            try {
                if (v != null) {
                    Object tag = v.findViewById(R.id.tv_adpt_friend_list_name).getTag();
                    if (tag != null) {
                        Friend friend = (Friend) tag;
                        friendClickListener.onTagFriend(v, !v.isSelected(), friend.getFullName());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
