package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.checkedin.R;
import com.checkedin.model.Friend;
import com.checkedin.model.response.BaseModel;
import com.checkedin.utility.Utility;
import com.checkedin.views.CircleImageView;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlockListAdapter extends RecyclerView.Adapter<BlockListAdapter.ViewHolder> implements VolleyStringRequest.AfterResponse {

    private ArrayList<Friend> items;
    private Context context;
    private WebServiceCall webServiceCall;
    private int selectedPosition;

    public BlockListAdapter(Context context, ArrayList<Friend> items) {
        this.context = context;
        this.items = items;
        webServiceCall = new WebServiceCall(this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Utility.loadImageGlide(  holder.civProfileImg      ,items.get(position).getThumbImage() );
//        Picasso.with(context).load(items.get(position).getThumbImage()).error(R.drawable.ic_placeholder).into(holder.civProfileImg);
        holder.tvName.setText(items.get(position).getFullName());

        holder.btnUnblock.setTag(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_blocklist, parent, false);

        return new ViewHolder(view);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnClickListener {
        private CircleImageView civProfileImg;
        private TextView tvName;
        private Button btnUnblock;

        public ViewHolder(View itemView) {
            super(itemView);
            civProfileImg = (CircleImageView) itemView.findViewById(R.id.civ_blocklist_profileImg);
            tvName = (TextView) itemView.findViewById(R.id.tv_blocklist_name);
            btnUnblock = (Button) itemView.findViewById(R.id.btn_blocklist_unblock);

            btnUnblock.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
                Utility.doubleTapTime = System.currentTimeMillis();
                selectedPosition = (Integer) v.getTag();
                webServiceCall.blockUserWsCall(context, items.get(selectedPosition).getId(), false);
            }
        }

    }

    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
        if (baseModel != null) {
            if (baseModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                items.remove(selectedPosition);
                notifyDataSetChanged();
            }
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), baseModel.getMessage());
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.server_connect_error));
        }
    }


    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getString(R.string.server_connect_error));
    }

}
