package com.checkedin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.container.BaseContainerFragment;
import com.checkedin.fragment.ChatListFrg;
import com.checkedin.fragment.ChatboxFrg;
import com.checkedin.model.ChatList;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.views.swipe.BaseSwipeOpenViewHolder;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

@SuppressLint("NewApi")
public class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ViewHolder> implements VolleyStringRequest.AfterResponse {

    private Context context;
    private ArrayList<ChatList> items;
    private ChatListFrg chatListFrg;
    private int position;
    private WebServiceCall webServiceCall;

    public ChatListAdapter(Context context, ArrayList<ChatList> items, ChatListFrg chatListFrg) {
        this.context = context;
        this.items = items;
        this.chatListFrg = chatListFrg;
        webServiceCall = new WebServiceCall(this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvSenderName.setText(items.get(position).getFullName());
        holder.tvMessage.setText(StringEscapeUtils.unescapeJava(items.get(position).gettMessage()));
        holder.tvTime.setText(items.get(position).getFormatedCreateTime());

        int unRead = Integer.parseInt(items.get(position).getTotalUnRead());
        if (unRead == 0) {
            holder.tvUnReadMessage.setVisibility(View.GONE);
        } else if (unRead <= 9) {
            holder.tvUnReadMessage.setVisibility(View.VISIBLE);
            holder.tvUnReadMessage.setText(" " + items.get(position).getTotalUnRead() + " ");
        } else {
            holder.tvUnReadMessage.setVisibility(View.VISIBLE);
            holder.tvUnReadMessage.setText(items.get(position).getTotalUnRead());
        }
        Utility.loadImageGlide(holder.civProfilePhoto, items.get(position).getvImage());
        holder.viewMain.setTag(position);
        holder.tvDelete.setTag(position);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup rootView, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_chat_list, rootView, false);
        return new ViewHolder(view);
    }


    class ViewHolder extends BaseSwipeOpenViewHolder implements OnClickListener {

        private final TextView tvDelete;
        private TextView tvSenderName, tvTime, tvUnReadMessage;
        private EmojiconTextView tvMessage;
        private CircleImageView civProfilePhoto;

        private View viewMain;

        public ViewHolder(View itemView) {
            super(itemView);
            civProfilePhoto = (CircleImageView) itemView.findViewById(R.id.civ_chat_list_photo);
            tvSenderName = (TextView) itemView.findViewById(R.id.tv_chat_list_name);
            tvMessage = (EmojiconTextView) itemView.findViewById(R.id.tv_chat_list_msg);
            tvTime = (TextView) itemView.findViewById(R.id.tv_chat_list_time);
            tvDelete = (TextView) itemView.findViewById(R.id.tv_chat_list_delete);
            viewMain = itemView.findViewById(R.id.ll_main);

            tvUnReadMessage = (TextView) itemView.findViewById(R.id.tv_chat_list_unread);
            viewMain.setOnClickListener(this);
            tvDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                position = (Integer) v.getTag();
                switch (v.getId()) {
                    case R.id.tv_chat_list_delete:
                        webServiceCall.clearChat(context, items.get(position).getiUserID());
                        break;
                    case R.id.ll_main:
                        Bundle argument = new Bundle();
                        argument.putString("friend_id", items.get(position).getiUserID());
                        argument.putString("friend_name", items.get(position).getFullName());
                        argument.putString("friend_image_url", items.get(position).getvImage());
                        ChatboxFrg chatboxFrg = new ChatboxFrg();
                        chatboxFrg.setArguments(argument);

                        ((BaseContainerFragment) chatListFrg.getParentFragment()).replaceFragment(chatboxFrg, true);
                        break;
                }

            }
        }

        @NonNull
        @Override
        public View getSwipeView() {
            return viewMain;
        }

        @Override
        public float getEndHiddenViewSize() {
            return tvDelete.getMeasuredWidth();
        }

        @Override
        public float getStartHiddenViewSize() {
            return 0;
        }
    }

    public void changeMsgStatusRead() {
        items.get(position).setTotalUnRead("0");
        notifyDataSetChanged();
    }

    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
        if (baseModel != null) {
            if (baseModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                items.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, getItemCount() - position);
            } else {
                Utility.showSnackBar(((MainActivity) context).findViewById(android.R.id.content), baseModel.getMessage());
            }
        } else {
            Utility.showSnackBar(((MainActivity) context).findViewById(android.R.id.content), context.getString(R.string.server_connect_error));
        }

    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(((MainActivity) context).findViewById(android.R.id.content), context.getString(R.string.server_connect_error));
    }
}
