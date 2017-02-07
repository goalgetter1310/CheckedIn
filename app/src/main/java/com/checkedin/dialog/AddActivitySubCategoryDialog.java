package com.checkedin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.databinding.DialogAddSubCategoryBinding;
import com.checkedin.utility.Utility;

public class AddActivitySubCategoryDialog extends Dialog implements View.OnClickListener {

    private final ActivityAddSubCategory activityAddSubCategory;
    private Context context;
    private String subCategoryName;

    private DialogAddSubCategoryBinding mBinding;

    public AddActivitySubCategoryDialog(Context context, ActivityAddSubCategory activityAddSubCategory) {
        super(context);
        this.context = context;
        this.activityAddSubCategory = activityAddSubCategory;
    }

    public AddActivitySubCategoryDialog(Context context, String subCategoryName, ActivityAddSubCategory activityAddSubCategory) {
        super(context);
        this.context = context;
        this.activityAddSubCategory = activityAddSubCategory;
        this.subCategoryName = subCategoryName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.inflate(((MainActivity) context).getLayoutInflater(), R.layout.dialog_add_sub_category, null, false);
        setContentView(mBinding.getRoot());

        mBinding.etSubCategory.setText(subCategoryName);
        mBinding.btnAdd.setText(TextUtils.isEmpty(subCategoryName) ? R.string.add : R.string.change);

        mBinding.btnAdd.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
    }


    private boolean getSubcategoryName() {
        subCategoryName = mBinding.etSubCategory.getText().toString().trim();

        if (subCategoryName.length() < 3) {
            Toast.makeText(context, R.string.sub_category_validation_msg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (getSubcategoryName()) {
                    activityAddSubCategory.onSubCategoryAdded(subCategoryName);
                    dismiss();
                }
                break;
            case R.id.btn_cancel:
                dismiss();
        }
    }

    @Override
    public void dismiss() {
        Utility.hideKeyboard((MainActivity) context);
        super.dismiss();
    }

    public interface ActivityAddSubCategory {
        void onSubCategoryAdded(String subCategoryName);
    }

}
