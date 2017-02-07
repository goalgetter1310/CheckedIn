package com.checkedin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.checkedin.R;
import com.checkedin.databinding.DialogBottomBinding;
import com.checkedin.utility.Utility;


public class BottomActionDialog extends Dialog implements View.OnClickListener {

    private final PostAction postAction;
    private DialogBottomBinding mBinding;
    private final Context context;
    private boolean isEditable=true;
    public BottomActionDialog(Context context,boolean isEditable, PostAction postAction) {
        super(context, R.style.AppTheme);
        this.context = context;
        this.isEditable=isEditable;
        this.postAction = postAction;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Utility.colorRes(context, R.color.dialog_transparent)));
        mBinding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_bottom, null, false);
        setContentView(mBinding.getRoot());


        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        if(!isEditable)
        mBinding.tvEdit.setVisibility(View.GONE);
        else
        mBinding.tvEdit.setVisibility(View.VISIBLE);
        mBinding.llBottom.startAnimation(anim);
        mBinding.tvEdit.setOnClickListener(this);
        mBinding.tvDelete.setOnClickListener(this);
        mBinding.tvCancel.setOnClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        onStartAnimation();
    }


    private void onStartAnimation() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_up);
        mBinding.llBottom.startAnimation(anim);
    }

    private void onCloseAnimation() {
        Animation anim = AnimationUtils.loadAnimation(context, R.anim.slide_down);


        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                BottomActionDialog.super.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mBinding.llBottom.startAnimation(anim);
    }

    @Override
    public void dismiss() {
        onCloseAnimation();
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.tv_edit:
                postAction.onEditSelect();
                break;
            case R.id.tv_delete:
                postAction.onDeleteSelect();
                break;
        }
    }

    public interface PostAction {
        void onEditSelect();

        void onDeleteSelect();
    }

}
