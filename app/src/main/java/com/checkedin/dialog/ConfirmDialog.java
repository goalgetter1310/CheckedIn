package com.checkedin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.checkedin.R;

public class ConfirmDialog extends Dialog implements View.OnClickListener {
    private final OnConfirmYes onConfirmYes;
    private final OnConfirmNo onConfirmNo;
    private String msg;

    public ConfirmDialog(Context context, String msg, OnConfirmYes onConfirmYes) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.onConfirmYes = onConfirmYes;
        this.onConfirmNo = null;
        this.msg = msg;
    }

    public ConfirmDialog(Context context, String msg, OnConfirmYes onConfirmYes, OnConfirmNo onConfirmNo) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.onConfirmYes = onConfirmYes;
        this.onConfirmNo = onConfirmNo;
        this.msg = msg;
    }

    public ConfirmDialog(Context context, String msg, OnConfirmNo onConfirmNo) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.onConfirmNo = onConfirmNo;
        this.onConfirmYes = null;
        this.msg = msg;
    }

    public ConfirmDialog(Context context, @StringRes int msgResId, OnConfirmYes onConfirmYes) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.onConfirmYes = onConfirmYes;
        this.onConfirmNo = null;
        this.msg = context.getResources().getString(msgResId);
    }

    public ConfirmDialog(Context context, @StringRes int msgResId, OnConfirmYes onConfirmYes, OnConfirmNo onConfirmNo) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.onConfirmYes = onConfirmYes;
        this.onConfirmNo = onConfirmNo;
        this.msg = context.getResources().getString(msgResId);
    }

    public ConfirmDialog(Context context, @StringRes int msgResId, OnConfirmNo onConfirmNo) {
        super(context, android.R.style.Theme_DeviceDefault_Dialog_NoActionBar);
        this.onConfirmNo = onConfirmNo;
        this.onConfirmYes = null;
        this.msg = context.getResources().getString(msgResId);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        setContentView(R.layout.dialog_confirm);
        setCancelable(false);
        TextView tvMessage = (TextView) findViewById(R.id.dialog_confirm_tv_msg);
        com.material.widget.TextView btnNo = (com.material.widget.TextView) findViewById(R.id.dialog_confirm_btn_no);
        com.material.widget.TextView btnYes = (com.material.widget.TextView) findViewById(R.id.dialog_confirm_btn_yes);

        tvMessage.setText(msg);

        btnNo.setOnClickListener(this);
        btnYes.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_confirm_btn_no:
                if (onConfirmNo != null)
                    onConfirmNo.confirmNo();
                dismiss();
                break;
            case R.id.dialog_confirm_btn_yes:
                if (onConfirmYes != null)
                    onConfirmYes.confirmYes();
                dismiss();
                break;
        }
    }

    public interface OnConfirmYes {
        void confirmYes();
    }

    public interface OnConfirmNo {
        void confirmNo();
    }
}
