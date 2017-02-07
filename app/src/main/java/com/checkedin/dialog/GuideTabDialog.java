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
import com.checkedin.databinding.DialogTabGuideBinding;
import com.checkedin.utility.Utility;


public class GuideTabDialog extends Dialog
        implements View.OnClickListener
{


    private final Context context;

    private int next;
    private DialogTabGuideBinding mBinding;

    public GuideTabDialog(Context context) {
        super(context, R.style.AppTheme);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Utility.colorRes(context, R.color.guide_transparent)));
        mBinding = DataBindingUtil.inflate((LayoutInflater.from(context)), R.layout.dialog_tab_guide, null, false);
        setContentView(mBinding.getRoot());

        mBinding.btnGotit.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (next) {
            case 0:
                mBinding.dialogTabLlHome.setVisibility(View.INVISIBLE);
                mBinding.dialogTabLlLocation.setVisibility(View.VISIBLE);
                mBinding.dialogTabTvTitle.setText(getContext().getString(R.string.location_guide));
                break;
            case 1:
                mBinding.dialogTabLlLocation.setVisibility(View.INVISIBLE);
                mBinding.dialogTabLlLongue.setVisibility(View.VISIBLE);
                mBinding.dialogTabTvTitle.setText(getContext().getString(R.string.checedin_lounde_guide));
                break;
            case 2:
                mBinding.dialogTabLlLongue.setVisibility(View.INVISIBLE);
                mBinding.dialogTabLlActivity.setVisibility(View.VISIBLE);
                mBinding.dialogTabTvTitle.setText(getContext().getString(R.string.activity_guide));
                break;
            case 3:
                mBinding.dialogTabLlActivity.setVisibility(View.INVISIBLE);
                mBinding.dialogTabLlPlanning.setVisibility(View.VISIBLE);
                mBinding.dialogTabTvTitle.setText(getContext().getString(R.string.planning_guide));
                mBinding.btnGotit.setText(R.string.finish);;
                break;
            case 4:
                dismiss();
                break;
        }

        next++;
    }
}
