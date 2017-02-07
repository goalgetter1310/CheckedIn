package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.databinding.AdapterPersonalBinding;
import com.checkedin.fragment.PersonalLocationFrg;
import com.checkedin.model.CheckinActivity;
import com.checkedin.model.Place;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.PostModel;
import com.checkedin.utility.Utility;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;

import java.util.ArrayList;

public class PersonalLocAdapter extends RecyclerView.Adapter<PersonalLocAdapter.ViewHolder> implements VolleyStringRequest.AfterResponse {

    private final Context context;
    private final ArrayList<Place> items;
    private final PersonalLocationFrg fragment;
    private WebServiceCall webServiceCall;
    private boolean isLongPress;
    private int longPressPosition;

    public PersonalLocAdapter(Context context, ArrayList<Place> items, PersonalLocationFrg fragment) {
        this.context = context;
        this.items = items;
        this.fragment = fragment;
        webServiceCall = new WebServiceCall(this);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterPersonalBinding mBinding = DataBindingUtil.inflate(((MainActivity) context).getLayoutInflater(), R.layout.adapter_personal, parent, false);
        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.isSelectedForActionEvent = items.get(position).isActionEvent();

        holder.mBinding.setHolder(holder);
        holder.mBinding.setItems(items.get(position));
        holder.mBinding.getRoot().setTag(position);
    }


    public void closeActionEventView() {
        isLongPress = false;
        items.get(longPressPosition).setActionEvent(false);
        notifyItemChanged(longPressPosition);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private AdapterPersonalBinding mBinding;
        private boolean isSelectedForActionEvent;

        public ViewHolder(AdapterPersonalBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
            mBinding.getRoot().setOnLongClickListener(this);
        }

        public void onItemClick(View v) {
            if (!isLongPress) {
                int position = (Integer) v.getTag();
                CheckinActivity checkinPostActivity = new CheckinActivity();
                checkinPostActivity.setLocation(items.get(position));
                webServiceCall.personalCheckinWsCall(context, checkinPostActivity);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            isLongPress = true;
            longPressPosition = (Integer) v.getTag();
            items.get(longPressPosition).setActionEvent(true);
            fragment.showActionEventMenu(true, longPressPosition);
            notifyItemChanged(longPressPosition);


            return false;
        }

        public boolean isSelectedForActionEvent() {
            return isSelectedForActionEvent;
        }
    }

    @Override
    public void onResponseReceive(int requestCode) {
        PostModel mCheckinActivity = (PostModel) webServiceCall.volleyRequestInstatnce().getModelObject(PostModel.class, "Checkin Activity");
        if (mCheckinActivity != null) {
            if (mCheckinActivity.getStatus() == BaseModel.STATUS_SUCCESS) {
                ((MainActivity) context).openHome();
            }
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), mCheckinActivity.getMessage());
        } else {
            Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getResources().getString(R.string.server_connect_error));
        }
    }

    @Override
    public void onErrorReceive() {
        Utility.showSnackBar(((Activity) context).findViewById(android.R.id.content), context.getResources().getString(R.string.server_connect_error));
    }
}
