package com.checkedin.dialog;

import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

import com.checkedin.R;

public class ProgressDialog extends android.app.ProgressDialog {

	public ProgressDialog(Context context) {
		super(context, R.style.CustomDialogTheme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_progress);
		WindowManager.LayoutParams windowMgr = getWindow().getAttributes();
		windowMgr.x = 0;
		windowMgr.y = 0;
		getWindow().setAttributes(windowMgr);
	}

}
