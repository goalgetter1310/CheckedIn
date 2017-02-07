package com.checkedin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.checkedin.R;
import com.checkedin.SelectImage;
import com.checkedin.utility.Utility;

public class ImageChooserDialog extends Dialog implements View.OnClickListener {

	private LinearLayout llCameraSelect, llGallerySelect;
	public static boolean isCamera;
	private Activity act;
	private SelectImage.ActivityResult activityResult;
	private boolean isMultipleSelect;

	public ImageChooserDialog(Activity act, SelectImage.ActivityResult activityResult) {
		super(act);
		this.act = act;
		this.activityResult = activityResult;
	}

	public ImageChooserDialog(Activity act, SelectImage.ActivityResult activityResult, boolean isMultipleSelect) {
		super(act);
		this.act = act;
		this.activityResult = activityResult;
		this.isMultipleSelect = isMultipleSelect;
	}

	public ImageChooserDialog(Activity act) {
		this(act, (SelectImage.ActivityResult) act);
	}

	@SuppressWarnings("deprecation")
	private void roundRectShape() {

		GradientDrawable gdRoundRect = new GradientDrawable();
		gdRoundRect.setCornerRadius(5);
		findViewById(R.id.ll_dialog_main).setBackgroundDrawable(gdRoundRect);

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_select_image);
		getWindow().getAttributes().windowAnimations = R.style.SelectImageDialogAnimation;
		initializeViews();
		roundRectShape();

		llCameraSelect.setOnClickListener(this);
		llGallerySelect.setOnClickListener(this);

	}

	private void initializeViews() {
		llCameraSelect = (LinearLayout) findViewById(R.id.ll_dialog_select_camera);
		llGallerySelect = (LinearLayout) findViewById(R.id.ll_dialog_select_gallery);

	}

	@Override
	public void onClick(View v) {
		if ((Utility.doubleTapTime + 500) < System.currentTimeMillis()) {
			Utility.doubleTapTime = System.currentTimeMillis();
			switch (v.getId()) {
			case R.id.ll_dialog_select_camera:
				new SelectImage(activityResult, act, SelectImage.CAMERA_SELECTED);
				isCamera = true;
				break;
			case R.id.ll_dialog_select_gallery:
				isCamera = false;
				if (isMultipleSelect)
					new SelectImage(activityResult, act, SelectImage.GALLERY_SELECTED, true);
				else
					new SelectImage(activityResult, act, SelectImage.GALLERY_SELECTED);
				break;
			}
			this.dismiss();
		}
	}

}