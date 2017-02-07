package com.checkedin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.checkedin.R;
import com.checkedin.databinding.DialogGuideBinding;
import com.checkedin.utility.Utility;


public class GuideDialog extends Dialog implements View.OnClickListener {


    private final Context context;

    private int next;
    private DialogGuideBinding mBinding;

    public GuideDialog(Context context) {
        super(context, R.style.AppTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Utility.colorRes(context, R.color.guide_transparent)));
        mBinding = DataBindingUtil.inflate((LayoutInflater.from(context)), R.layout.dialog_guide, null, false);
        setContentView(mBinding.getRoot());

        mBinding.llGuide1.setVisibility(View.VISIBLE);
        mBinding.llGuide3.setVisibility(View.GONE);
        mBinding.llGuide3.setVisibility(View.GONE);

        mBinding.btnGotit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (next) {
            case 0:
                mBinding.llGuide1.setVisibility(View.GONE);
                mBinding.llGuide2.setVisibility(View.VISIBLE);
                mBinding.llGuide3.setVisibility(View.GONE);
                break;
            case 1:
                mBinding.llGuide1.setVisibility(View.GONE);
                mBinding.llGuide2.setVisibility(View.GONE);
                mBinding.llGuide3.setVisibility(View.VISIBLE);
                mBinding.btnGotit.setText(R.string.finish);
                break;
            case 2:
                dismiss();
                break;
        }

        next++;
    }
}
