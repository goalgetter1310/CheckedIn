package com.checkedin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.checkedin.R;
import com.checkedin.views.SwipeBackLayout;

public class SwipeBackDialog extends Dialog implements SwipeBackLayout.SwipeBackListener {

	private SwipeBackLayout swipeBackLayout;
	private ImageView ivShadow;
	private Context context;


	public SwipeBackDialog(Context context, int style) {
		super(context, style);
		this.context = context;
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(getContainer());
		View view = LayoutInflater.from(context).inflate(layoutResID, null);
		swipeBackLayout.addView(view);
	}

	@SuppressWarnings("deprecation")
	private View getContainer() {
		RelativeLayout container = new RelativeLayout(context);
		swipeBackLayout = new SwipeBackLayout(context, this);
		swipeBackLayout.setOnSwipeBackListener(this);
		ivShadow = new ImageView(context);
		ivShadow.setBackgroundColor(context.getResources().getColor(R.color.dark_transparent));
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		container.addView(ivShadow, params);
		container.addView(swipeBackLayout);
		return container;
	}

	public void setDragEdge(SwipeBackLayout.DragEdge dragEdge) {
		swipeBackLayout.setDragEdge(dragEdge);
	}

//	public SwipeBackLayout getSwipeBackLayout() {
//		return swipeBackLayout;
//	}

	@Override
	public void onViewPositionChanged(float fractionAnchor, float fractionScreen) {
		ivShadow.setAlpha(1 - fractionScreen);
	}

}
