package com.checkedin.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.checkedin.R;
import com.checkedin.model.Photos;
import com.checkedin.utility.Utility;
import com.checkedin.views.TouchImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewPhotoPagerAdpater extends PagerAdapter {

    private final boolean isProfileImg;
    private final Context context;
    private final int resource;
    private final ArrayList<Photos> items;
    private TouchImageView ivPhotos;

    public ViewPhotoPagerAdpater(Context context, boolean isProfileImg, int resource, ArrayList<Photos> items) {
        this.context = context;
        this.resource = resource;
        this.items = items;
        this.isProfileImg = isProfileImg;
    }

//    public ViewPhotoPagerAdpater(Context context, int resource, ArrayList<Photos> items) {
//        this(context, false, resource, items);
//    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(resource, container, false);
        initializeViews(view);
        if (isProfileImg) {
            Utility.loadImageGlide(       ivPhotos,          items.get(position).getImageUrl());
//                    Picasso.with(context).load(items.get(position).getImageUrl()).fit().error(R.drawable.ic_placeholder).into(ivPhotos);
        }else{
            Utility.loadImageGlide(    ivPhotos,                items.get(position).getOriginalSizePath());
//                    Picasso.with(context).load(items.get(position).getOriginalSizePath()).fit().error(R.drawable.ic_placeholder).into(ivPhotos);
        }
        container.addView(view);

        return view;
    }

    private void initializeViews(View view) {
        ivPhotos = (TouchImageView) view.findViewById(R.id.iv_adpt_img);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }

}
