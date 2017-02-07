package com.checkedin;


import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

public class ManageActionEvent implements View.OnClickListener {

    private final ActionEvent actionEvent;
    private final View viewActionMain;
    private final ImageView ivEdit, ivBack, ivDelete;
    private final View viewAboveContainer;

    public ManageActionEvent(Activity activity, ActionEvent actionEvent) {
        this.actionEvent = actionEvent;
        this.viewActionMain = activity.findViewById(R.id.inc_action_layout);

        viewAboveContainer = activity.findViewById(R.id.view_above_container);
        ivBack = (ImageView) viewActionMain.findViewById(R.id.iv_back);
        ivEdit = (ImageView) viewActionMain.findViewById(R.id.iv_edit);
        ivDelete = (ImageView) viewActionMain.findViewById(R.id.iv_delete);

    }

    public void showActionEventMenu(boolean isActionEventMenu) {
        viewAboveContainer.setOnClickListener(this);
        ivBack.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        if (isActionEventMenu) {
            viewAboveContainer.setVisibility(View.VISIBLE);
            viewActionMain.setVisibility(View.VISIBLE);
        } else {
            viewAboveContainer.setVisibility(View.GONE);
            viewActionMain.setVisibility(View.GONE);
        }

    }

    public void closeActionView() {
        viewAboveContainer.setOnClickListener(null);
        ivBack.setOnClickListener(null);
        ivEdit.setOnClickListener(null);
        ivDelete.setOnClickListener(null);
        viewAboveContainer.setVisibility(View.GONE);
        viewActionMain.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.view_above_container:
            case R.id.iv_back:
                actionEvent.onBack();
                break;
            case R.id.iv_edit:
                actionEvent.onEdit();
                break;
            case R.id.iv_delete:
                actionEvent.onDelete();
                break;
        }
        closeActionView();
    }
}
