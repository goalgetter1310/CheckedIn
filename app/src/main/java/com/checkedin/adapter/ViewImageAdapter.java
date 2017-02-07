package com.checkedin.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.checkedin.R;
import com.checkedin.model.Photos;
import com.checkedin.utility.Utility;

import java.util.ArrayList;

@SuppressLint("ViewHolder")
public class ViewImageAdapter extends ArrayAdapter<Photos> {

    private Context context;
    private int resource;
    private ArrayList<Photos> items;

    private ImageView ivPhoto;
    private TextView tvCaption;

    public ViewImageAdapter(Context context, int resource, ArrayList<Photos> items) {
        super(context, resource, items);
        this.context = context;
        this.resource = resource;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(resource, parent, false);

        initViews(view);

        Utility.loadImageGlide(ivPhoto,items.get(position).getOriginalSizePath());
//        Glide.with(context).load(items.get(position).getOriginalSizePath()).error(R.drawable.ic_placeholder).into(ivPhoto);

        String caption = items.get(position).getCaption().trim();
        if (!TextUtils.isEmpty(caption)) {
            tvCaption.setVisibility(LinearLayout.VISIBLE);
            tvCaption.setText(caption);
        } else {
            tvCaption.setVisibility(LinearLayout.GONE);
        }

        return view;
    }

    private void initViews(View view) {
        ivPhoto = (ImageView) view.findViewById(R.id.iv_adpt_activity_photo_img);
        tvCaption = (TextView) view.findViewById(R.id.tv_adpt_activity_photo_caption);
    }

}
