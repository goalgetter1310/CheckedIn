package com.checkedin.adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.databinding.AdapterChatroomDarkBinding;
import com.checkedin.model.MessageChatRoom;
import com.checkedin.utility.UserPreferences;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Greeshma on 20/09/16.
 */
public class MessageChatRoomAdapter extends RecyclerView.Adapter<MessageChatRoomAdapter.ViewHolder> {

    private final List<MessageChatRoom> msgs;
    private final Context context;
    private LayoutInflater inflater;

    public MessageChatRoomAdapter(Context context, ArrayList<MessageChatRoom> msgs) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.msgs = msgs;
    }

    public void addMessageList(List<MessageChatRoom> msgs) {
        this.msgs.addAll(msgs);
        notifyDataSetChanged();
    }

    public void addMessage(MessageChatRoom msg) {
        this.msgs.add(0, msg);
        notifyDataSetChanged();
    }

    public void clearMessageList() {
        this.msgs.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterChatroomDarkBinding mBinding = DataBindingUtil.inflate(inflater, R.layout.adapter_chatroom_dark, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mBinding.getRoot().setTag(position);

        if (!TextUtils.isEmpty(msgs.get(position).getTMessage())) {

            if (!msgs.get(position).isSelf(Integer.parseInt(UserPreferences.fetchUserId(context)))) {
                holder.mBinding.llDark.setVisibility(View.VISIBLE);
                holder.mBinding.llLight.setVisibility(View.GONE);

                holder.mBinding.chatTvMsg.setText(StringEscapeUtils.unescapeJava(msgs.get(position).getTMessage()));
//                msgs.get(position).setCreateTime(msgs.get(position).getTUpdateDate());
//                holder.mBinding.chatTvTime.setText(msgs.get(position).getFormatedCreateTime());
                holder.mBinding.chatTvTime.setText(msgs.get(position).setCreateTime(msgs.get(position).getTUpdateDate().replace("T", " ").replace("Z", "")));
                holder.mBinding.chatTvUserName.setText(msgs.get(position).getVFirstName() + " " + msgs.get(position).getVLastName());

            } else {
                holder.mBinding.llLight.setVisibility(View.VISIBLE);
                holder.mBinding.llDark.setVisibility(View.GONE);

                holder.mBinding.chatTvMsgLight.setText(StringEscapeUtils.unescapeJava(msgs.get(position).getTMessage()));
//                msgs.get(position).setCreateTime(msgs.get(position).getTUpdateDate().replace("T", " ").replace("Z", ""));
//                holder.mBinding.chatTvTimeLight.setText(msgs.get(position).getFormatedCreateTime());

                holder.mBinding.chatTvTimeLight.setText(msgs.get(position).setCreateTime(msgs.get(position).getTUpdateDate().replace("T", " ").replace("Z", "")));

            }
        }

    }

    @Override
    public int getItemCount() {
        return msgs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterChatroomDarkBinding mBinding;

        public ViewHolder(AdapterChatroomDarkBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }

    }


}
