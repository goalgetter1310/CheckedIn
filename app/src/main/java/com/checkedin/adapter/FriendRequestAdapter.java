package com.checkedin.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.container.DialogFragmentContainer;
import com.checkedin.fragment.FriendRequestFrg;
import com.checkedin.fragment.TimelineFrg;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.views.RecyclerViewFastScroller.BubbleTextGetter;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.widget.Button;

import java.util.ArrayList;
import java.util.Locale;

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestAdapter.ViewHolder> implements BubbleTextGetter, VolleyStringRequest.AfterResponse {
    private final FriendRequestFrg friendRequestFrg;
    private Context context;
    private int resource;
    private ArrayList<Friend> items;
    private WebServiceCall webServiceCall;
    private int selectPosition;

    public FriendRequestAdapter(Context context, int resource, ArrayList<Friend> items, FriendRequestFrg friendRequestFrg) {
        this.context = context;
        this.resource = resource;
        this.items = items;
        this.friendRequestFrg = friendRequestFrg;
        webServiceCall = new WebServiceCall(this);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(resource, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvFriendName.setText(items.get(position).getFullName());
        holder.btnConfirm.setTag(position);
        holder.btnDelete.setTag(position);
        holder.llFriendRequest.setTag(position);
        Utility.loadImageGlide(   holder.civProfilePic,          items.get(position).getThumbImage());
//        Glide.with(context).load(items.get(position).getThumbImage()).error(R.drawable.ic_placeholder).into(holder.civProfilePic);
    }

    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel mFriendRequest = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, "AcceptReject  Response");
        if (mFriendRequest != null) {
            if (mFriendRequest.getStatus() == BaseModel.STATUS_SUCCESS) {
                items.remove(selectPosition);
                friendRequestFrg.updateRequestCounter();
                notifyDataSetChanged();
            } else {
                Toast.makeText(context, mFriendRequest.getMessage(), Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(context, R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {

    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private TextView tvFriendName;
        private android.widget.LinearLayout llFriendRequest;
        private CircleImageView civProfilePic;
        private Button btnConfirm, btnDelete;

        public ViewHolder(View itemView) {
            super(itemView);

            tvFriendName = (TextView) itemView.findViewById(R.id.tv_adpt_friend_request_name);
            civProfilePic = (CircleImageView) itemView.findViewById(R.id.civ_adpt_friend_request_img);
            btnConfirm = (Button) itemView.findViewById(R.id.btn_adpt_friend_request_confirm);
            btnDelete = (Button) itemView.findViewById(R.id.btn_adpt_friend_request_delete);
            llFriendRequest= (android.widget.LinearLayout) itemView.findViewById(R.id.ll_friend_request);

            llFriendRequest.setOnClickListener(this);
            btnConfirm.setOnClickListener(this);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                if (Utility.checkInternetConnectivity(context)) {
                    selectPosition = (Integer) v.getTag();
                    switch (v.getId()) {
                        case R.id.btn_adpt_friend_request_confirm:
                            webServiceCall.sendValueToServer(context, items.get(selectPosition).getRelationId(), Friend.REQUEST_ACCEPTED);
                            break;
                        case R.id.btn_adpt_friend_request_delete:
                            webServiceCall.sendValueToServer(context, items.get(selectPosition).getRelationId(), Friend.REQUEST_REJECTED);
                            break;
                        case R.id.ll_friend_request:
                            TimelineFrg friendProfile = new TimelineFrg();
                            Bundle argument = new Bundle();
                            argument.putString("friend_id", items.get((Integer) v.getTag()).getId());
                            friendProfile.setArguments(argument);

                            ((DialogFragmentContainer) friendRequestFrg.getParentFragment().getParentFragment()).fragmentTransition(friendProfile, true);
                            break;
                    }

                } else {
                    Toast.makeText(context, R.string.no_internet_connect, Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    @Override
    public String getTextToShowInBubble(int position) {
        return Character.toString(items.get(position).getFullName().toUpperCase(Locale.getDefault()).charAt(0));
    }
}
