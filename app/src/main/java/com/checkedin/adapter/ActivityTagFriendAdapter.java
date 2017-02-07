package com.checkedin.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.checkedin.R;
import com.checkedin.adapter.ActivityTagFriendAdapter.MyViewHolder;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.fragment.TagFriendListFrg;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.TagUser;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;

public class ActivityTagFriendAdapter extends RecyclerView.Adapter<MyViewHolder> implements OnClickListener {
	private Activity activity;
	private int resource;
	private ArrayList<TagUser> items;
	private TagFriendListFrg tagFriendListFrg;

	public ActivityTagFriendAdapter(Activity activity, int resource, ArrayList<TagUser> items, TagFriendListFrg tagFriendListFrg) {
		this.activity = activity;
		this.resource = resource;
		this.items = items;
		this.tagFriendListFrg = tagFriendListFrg;
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public void setData(List<TagUser> tagUserList)
	{
		items.clear();
		items.addAll(tagUserList);
		notifyDataSetChanged();
	}
	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Utility.loadImageGlide(holder.civProfilePhoto,        items.get(position).getvImage());
//				Glide.with(activity).load(items.get(position).getvImage()).error(R.drawable.ic_placeholder).into(holder.civProfilePhoto);
		holder.tvName.setText(items.get(position).getFullName());
		holder.itemView.setTag(position);
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int resource) {
		View view = LayoutInflater.from(parent.getContext()).inflate(this.resource, null);
		return new MyViewHolder(view);
	}

	class MyViewHolder extends ViewHolder {
		private CircleImageView civProfilePhoto;
		private TextView tvName;
		private View itemView;

		public MyViewHolder(View itemView) {
			super(itemView);
			this.itemView = itemView;
			civProfilePhoto = (CircleImageView) itemView.findViewById(R.id.civ_adpt_tag_friend_img);
			tvName = (TextView) itemView.findViewById(R.id.tv_adpt_tag_friend_name);
			itemView.setOnClickListener(ActivityTagFriendAdapter.this);
		}

	}

	@Override
	public void onClick(View v) {
		if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
			Utility.doubleTapTime = System.currentTimeMillis();
			TimelineFrg friendProfile = new TimelineFrg();
			Bundle argument = new Bundle();
			argument.putString("friend_id", items.get((Integer) v.getTag()).getiTaggedUserID());
			friendProfile.setArguments(argument);
			((DialogFragmentContainer) tagFriendListFrg.getParentFragment()).fragmentTransition(friendProfile, true);
		}
	}
}
