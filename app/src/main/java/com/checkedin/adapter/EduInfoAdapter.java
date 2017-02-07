package com.checkedin.adapter;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.checkedin.R;
import com.checkedin.activity.ProfileActivity;
import com.checkedin.databinding.AdapterEduInfoBinding;
import com.checkedin.model.EducationInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;


public class EduInfoAdapter extends RecyclerView.Adapter<EduInfoAdapter.ViewHolder> {


    private final ArrayList<EducationInfo> items;
    private final Context context;
    private final boolean isEditable;

    public EduInfoAdapter(Context context, ArrayList<EducationInfo> items, boolean isEditable) {
        this.context = context;
        this.items = items;
        this.isEditable = isEditable;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AdapterEduInfoBinding mBinding = DataBindingUtil.inflate(((Activity) context).getLayoutInflater(), R.layout.adapter_edu_info, parent, false);

        return new ViewHolder(mBinding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.eduYear = items.get(position).getTypeId() == EducationInfo.EDUCATION_TYPE_SCHOOL ? "High School" : "Univercity";
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(items.get(position).getFromDate()));
            int fromDate = calendar.get(Calendar.YEAR);
            holder.eduYear += " year " + fromDate;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.mBinding.setHolder(holder);
        holder.mBinding.setItems(items.get(position));
        holder.mBinding.getRoot().setTag(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private AdapterEduInfoBinding mBinding;
        private String eduYear;

        public ViewHolder(AdapterEduInfoBinding mBinding) {
            super(mBinding.getRoot());
            this.mBinding = mBinding;
        }


        public void onItemClick(View v) {
            int position = (Integer) v.getTag();
            Bundle argument = new Bundle();
            argument.putInt("edit_user_edu_info", position);
            argument.putInt("edu_type", items.get(position).getTypeId());
            ((ProfileActivity) context).manageFragment(4, argument, true);

        }

        public String getEduYear() {
            return eduYear;
        }

        public boolean isEditable() {
            return isEditable;
        }
    }
}
