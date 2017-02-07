package com.checkedin.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.model.Friend;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatFriendListAdapter extends RecyclerView.Adapter<ChatFriendListAdapter.ViewHolder> {
	private Context context;
	private int resource;
	private ArrayList<Friend> items;
	private FriendClickListener listener;

	public ChatFriendListAdapter(Context context, int resource, ArrayList<Friend> items, FriendClickListener listener) {
		this.context = context;
		this.resource = resource;
		this.items = items;
		this.listener = listener;
	}

	public interface FriendClickListener {
		void onFriendClicked(Friend friend);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View v = LayoutInflater.from(context).inflate(resource, parent, false);
		((CardView) v).setCardElevation(0);
		((CardView) v).setCardBackgroundColor(Color.TRANSPARENT);
		return new ViewHolder(v);
	}

	public void setItems(ArrayList<Friend> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		holder.tvFriendName.setText(items.get(position).getFullName());
		if (items.get(position).getThumbImage() != null && !items.get(position).getThumbImage().equals("")) {
			Utility.loadImageGlide(holder.civProfilePic ,items.get(position).getThumbImage());
//					Picasso.with(context).load(items.get(position).getThumbImage()).error(R.drawable.ic_placeholder).into(holder.civProfilePic);
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
			civProfilePic = (CircleImageView) itemView.findViewById(R.id.civ_adpt_friend_list_img);
			itemView.findViewById(R.id.tv_adpt_friend_list_checkinTime).setVisibility(View.GONE);
			itemView.findViewById(R.id.tv_adpt_friend_list_checkinPlace).setVisibility(View.GONE);
			ViewHolder.this.itemView = itemView;
			itemView.setOnClickListener(this);
		}

		@Override
		public void onClick(View v) {
			if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
				Utility.doubleTapTime = System.currentTimeMillis();
				listener.onFriendClicked(items.get((Integer) v.getTag()));
			}
		}
	}

}
