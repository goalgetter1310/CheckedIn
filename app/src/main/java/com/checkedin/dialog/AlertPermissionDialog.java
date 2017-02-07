package com.checkedin.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.checkedin.R;
import com.checkedin.utility.MyPrefs;
import com.checkedin.views.TextView;

public class AlertPermissionDialog {
    private static Activity mContext;
    private String mTitle = "Title", mMessage = "Message", mOkButton = "", mCancleButton = "";
    private View.OnClickListener mOkClickListener;
    private View.OnClickListener mCancleClickListener;
    private Dialog dialog;

    public AlertPermissionDialog(Activity mContext) {
        this.mContext = mContext;
    }


    public static void set(Activity mContext) {
        AlertPermissionDialog.mContext = mContext;
    }

    public static boolean show(String message) {
        if (message != null && message.length() == 0)
            return true;
        return show("", message);
    }

    public static boolean show(String title, String message) {
        if (mContext != null) {
            final boolean isExpire = message.equals("Authorization has been denied for this request.") || message.equals("Object reference not set to an instance of an object.");
            final AlertPermissionDialog alert = new AlertPermissionDialog(mContext);
            alert.setTitle(title).setMessage(!isExpire ? message : "Your Session has Expired! Please re-login to continue.").setOkClickListener("Ok", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (alert != null)
                        alert.dismiss();
                    if (isExpire) {
                        resetUserData();
                    }
                }
            });
            alert.show();
        }
        return mContext != null;
    }

    public static void resetUserData() {
        MyPrefs.getInstance(mContext).reset();
//        mContext.startActivity(new Intent(mContext, SplashActivity.class));
//        mContext.finish();
    }

    public static Activity get() {
        return mContext;
    }

    public void show() {
        dialog = getDialog(mTitle, mMessage, mOkButton, mCancleButton, mOkClickListener, mCancleClickListener);
        dialog.show();
    }

    public void dismiss() {
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public AlertPermissionDialog setOkClickListener(String mOkButton, View.OnClickListener mOkClickListener) {
        this.mOkButton = mOkButton;
        this.mOkClickListener = mOkClickListener;
        return this;
    }

    public AlertPermissionDialog setCancleClickListener(String mCancleButton, View.OnClickListener mCancleClickListener) {
        this.mCancleButton = mCancleButton;
        this.mCancleClickListener = mCancleClickListener;
        return this;
    }

    public AlertPermissionDialog setTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public AlertPermissionDialog setMessage(String mMessage) {
        this.mMessage = mMessage;
        return this;
    }

    public AlertPermissionDialog setTitleMessage(String mTitle, String mMessage) {
        this.mTitle = mTitle;
        this.mMessage = mMessage;
        return this;
    }

    private Dialog getDialog(String title, String msg, String okButton, String cancleButton, View.OnClickListener mOkClickListener, View.OnClickListener mCancleClickListener) {
        final Dialog myDialog = new Dialog(mContext);
        myDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        myDialog.setContentView(R.layout.dialog_permission);
        myDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        TextView tvTitle = (TextView) myDialog.findViewById(R.id.dialog_permission_title);
        TextView tvText = (TextView) myDialog.findViewById(R.id.dialog_permission_text);
        if (title.length() > 0)
            tvTitle.setText(title);
        else
            tvTitle.setVisibility(View.GONE);

        if (msg.length() > 0)
            tvText.setText(msg);
        else
            tvText.setVisibility(View.GONE);

        LinearLayout llOk = (LinearLayout) myDialog.findViewById(R.id.dialog_permission_ll_ok);
        if (okButton.length() > 0) {
            ((TextView) llOk.getChildAt(0)).setText(okButton);
            if (mOkClickListener != null)
                llOk.setOnClickListener(mOkClickListener);
        } else
            llOk.setVisibility(View.GONE);

        LinearLayout llCancel = (LinearLayout) myDialog.findViewById(R.id.dialog_permission_ll_cancel);
        if (cancleButton.length() > 0) {
            ((TextView) llCancel.getChildAt(0)).setText(cancleButton);
            if (mCancleClickListener != null)
                llCancel.setOnClickListener(mCancleClickListener);
        } else
            llCancel.setVisibility(View.GONE);

        return myDialog;

    }
}