package com.checkedin.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.model.Message;
import com.rockerhieu.emojicon.EmojiconTextView;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;

public class ChatboxAdapter extends RecyclerView.Adapter<ChatboxAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Message> items;

    public ChatboxAdapter(Context context, ArrayList<Message> items) {
        this.context = context;
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_chatbox_dark, parent, false);
        return new ViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.initViews(position);
        holder.tvMessage.setText(StringEscapeUtils.unescapeJava(items.get(position).getMsgTextOrImage()));
        holder.tvTime.setText(items.get(position).getFormatedCreateTime());
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private EmojiconTextView tvMessage;
        private TextView tvTime;

        public ViewHolder(View itemView, int position) {
            super(itemView);
            initViews(position);

        }

        public void initViews(int position) {
            if (!items.get(position).isSelf()) {
                itemView.findViewById(R.id.ll_dark).setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.ll_light).setVisibility(View.GONE);
                tvMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_tv_msg);
                tvTime = (TextView) itemView.findViewById(R.id.chat_tv_time);
            } else {
                itemView.findViewById(R.id.ll_light).setVisibility(View.VISIBLE);
                itemView.findViewById(R.id.ll_dark).setVisibility(View.GONE);
                tvMessage = (EmojiconTextView) itemView.findViewById(R.id.chat_tv_msg_light);
                tvTime = (TextView) itemView.findViewById(R.id.chat_tv_time_light);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

}
