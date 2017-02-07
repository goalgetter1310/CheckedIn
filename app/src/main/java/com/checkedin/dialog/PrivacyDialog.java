package com.checkedin.dialog;


import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.databinding.DialogPrivacySelectionBinding;
import com.material.widget.CompoundButton;

public class PrivacyDialog extends Dialog implements View.OnClickListener {


    private final Context context;
    private final int selected;
    private DialogPrivacySelectionBinding mBinding;
    private PrivarySelection privarySelection;

    public PrivacyDialog(Context context, int selected, PrivarySelection privarySelection) {
        super(context);
        this.context = context;
        this.selected = selected;
        this.privarySelection = privarySelection;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.inflate(((MainActivity) context).getLayoutInflater(), R.layout.dialog_privacy_selection, null, false);
        setContentView(mBinding.getRoot());

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mBinding.rbPrivacyPublic.setChecked(mBinding.rbPrivacyPublic == buttonView);
                    mBinding.rbPrivacyFriend.setChecked(mBinding.rbPrivacyFriend == buttonView);
                    mBinding.rbPrivacyPrivate.setChecked(mBinding.rbPrivacyPrivate == buttonView);

                }
            }

        };
        if (selected == 0) {
            mBinding.rbPrivacyPublic.setChecked(true);
        } else if (selected == 1) {
            mBinding.rbPrivacyFriend.setChecked(true);
        } else {
            mBinding.rbPrivacyPrivate.setChecked(true);
        }

        mBinding.rbPrivacyPublic.setOnCheckedChangeListener(listener);
        mBinding.rbPrivacyFriend.setOnCheckedChangeListener(listener);
        mBinding.rbPrivacyPrivate.setOnCheckedChangeListener(listener);
        mBinding.rlPrivacyPublic.setOnClickListener(this);
        mBinding.rlPrivacyFriend.setOnClickListener(this);
        mBinding.rlPrivacyPrivate.setOnClickListener(this);
        mBinding.tvPrivacyCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_privacy_public:
                mBinding.rbPrivacyPublic.setChecked(true);
                privarySelection.onPrivacySelection(0);
                break;
            case R.id.rl_privacy_friend:
                mBinding.rbPrivacyFriend.setChecked(true);
                privarySelection.onPrivacySelection(1);
                break;
            case R.id.rl_privacy_private:
                mBinding.rbPrivacyPrivate.setChecked(true);
                privarySelection.onPrivacySelection(2);
                break;

        }
        dismiss();
    }

    public interface PrivarySelection {
        void onPrivacySelection(int privacySelected);
    }
}
