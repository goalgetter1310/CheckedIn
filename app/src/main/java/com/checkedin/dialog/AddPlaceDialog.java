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
import com.checkedin.databinding.DialogAddLocationBinding;
import com.checkedin.utility.Utility;

public class AddPlaceDialog extends Dialog implements View.OnClickListener {

    private final AddPlace addPlace;
    private final int titleRes;
    private Context context;
    private String placeName;

    private DialogAddLocationBinding mBinding;

    public AddPlaceDialog(Context context, int titleRes, AddPlace addPlace) {
        super(context);
        this.context = context;
        this.addPlace = addPlace;
        this.titleRes=titleRes;
    }

    public AddPlaceDialog(Context context, int titleRes, String placeName, AddPlace addPlace) {
        this(context, titleRes, addPlace);
        this.placeName = placeName;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        mBinding = DataBindingUtil.inflate(((MainActivity) context).getLayoutInflater(), R.layout.dialog_add_location, null, false);
        setContentView(mBinding.getRoot());

        mBinding.etPlace.setText(placeName);
        mBinding.btnAdd.setText(TextUtils.isEmpty(placeName) ? R.string.add : R.string.change);
        mBinding.tvTitle.setText(titleRes);

        mBinding.btnAdd.setOnClickListener(this);
        mBinding.btnCancel.setOnClickListener(this);
    }


    private boolean placeyName() {
        placeName = mBinding.etPlace.getText().toString().trim();

        if (placeName.length() < 3) {
            Toast.makeText(context, R.string.sub_category_validation_msg, Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                if (placeyName()) {
                    addPlace.onPlaceAdded(placeName);
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

    public interface AddPlace {
        void onPlaceAdded(String placeName);
    }

}
