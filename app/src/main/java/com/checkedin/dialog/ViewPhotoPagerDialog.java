package com.checkedin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.ViewPager;

import com.checkedin.R;
import com.checkedin.adapter.ViewPhotoPagerAdpater;
import com.checkedin.model.Photos;

import java.util.ArrayList;

public class ViewPhotoPagerDialog extends Dialog {

    private final boolean isProfileImg;
    private ViewPager vpPhotos;
    private int postion;
    private ViewPhotoPagerAdpater adptViewPhotos;
    private ArrayList<Photos> alPhoto;
    private Context context;

    public ViewPhotoPagerDialog(Context context, int postion, ArrayList<Photos> alPhoto) {
        this(context, false, postion, alPhoto);

    }

    public ViewPhotoPagerDialog(Context context, boolean isProfileImg, int postion, ArrayList<Photos> alPhoto) {
        super(context, R.style.Theme_AppTheme);
        this.context = context;
        this.postion = postion;
        this.alPhoto = alPhoto;
        this.isProfileImg = isProfileImg;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pager_view_image);
        initViews();
//		setDragEdge(SwipeBackLayout.DragEdge.TOP);
        vpPhotos.setAdapter(adptViewPhotos);
        vpPhotos.setCurrentItem(postion);
    }

    private void initViews() {
        vpPhotos = (ViewPager) findViewById(R.id.vp_photos);
        adptViewPhotos = new ViewPhotoPagerAdpater(context,  isProfileImg,R.layout.adapter_full_image, alPhoto);
    }
}
