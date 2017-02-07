package com.checkedin.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.model.response.BaseModel;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.widget.CompoundButton;
import com.material.widget.RadioButton;


public class ReportContentDialog extends Dialog implements VolleyStringRequest.AfterResponse {

    private final Context context;
    private RadioButton rbREportReson[];
    private EditText etComment;
    private Button btnReport;
    private String postId;
    private WebServiceCall webServiceCall;

    public ReportContentDialog(Context context, String postId) {
        super(context, R.style.Theme_AppTheme);
        this.postId = postId;
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setContentView(R.layout.dialog_report_content);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        initViews();


        findViewById(R.id.iv_report_content_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int counter;
                for (counter = 0; counter < rbREportReson.length; counter++) {
                    if (rbREportReson[counter].isChecked())
                        break;
                }
                String comment = etComment.getText().toString();
                webServiceCall.reportWsCall(context, postId, counter + 1, comment);
            }
        });
        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(android.widget.CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    rbREportReson[0].setChecked(rbREportReson[0] == buttonView);
                    rbREportReson[1].setChecked(rbREportReson[1] == buttonView);
                    rbREportReson[2].setChecked(rbREportReson[2] == buttonView);
                    rbREportReson[3].setChecked(rbREportReson[3] == buttonView);
                    rbREportReson[4].setChecked(rbREportReson[4] == buttonView);
                    rbREportReson[5].setChecked(rbREportReson[5] == buttonView);
                    rbREportReson[6].setChecked(rbREportReson[6] == buttonView);
                }
            }

        };

        rbREportReson[0].setOnCheckedChangeListener(listener);
        rbREportReson[1].setOnCheckedChangeListener(listener);
        rbREportReson[2].setOnCheckedChangeListener(listener);
        rbREportReson[3].setOnCheckedChangeListener(listener);
        rbREportReson[4].setOnCheckedChangeListener(listener);
        rbREportReson[5].setOnCheckedChangeListener(listener);
        rbREportReson[6].setOnCheckedChangeListener(listener);

    }

    private void initViews() {
        rbREportReson = new RadioButton[7];
        rbREportReson[0] = (RadioButton) findViewById(R.id.rb_report_content_reson1);
        rbREportReson[1] = (RadioButton) findViewById(R.id.rb_report_content_reson2);
        rbREportReson[2] = (RadioButton) findViewById(R.id.rb_report_content_reson3);
        rbREportReson[3] = (RadioButton) findViewById(R.id.rb_report_content_reson4);
        rbREportReson[4] = (RadioButton) findViewById(R.id.rb_report_content_reson5);
        rbREportReson[5] = (RadioButton) findViewById(R.id.rb_report_content_reson6);
        rbREportReson[6] = (RadioButton) findViewById(R.id.rb_report_content_reson7);
        etComment = (EditText) findViewById(R.id.et_report_content_comment);
        btnReport = (Button) findViewById(R.id.btn_report_content_report_now);

        webServiceCall = new WebServiceCall(this);
    }


    @Override
    public void onResponseReceive(int requestCode) {
        BaseModel baseModel = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, BaseModel.class.getSimpleName());
        if (baseModel != null) {
            if (baseModel.getStatus() == BaseModel.STATUS_SUCCESS) {
                dismiss();
            }
            Toast.makeText(context, baseModel.getMessage(), Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, R.string.server_connect_error, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorReceive() {
        Toast.makeText(context, R.string.server_connect_error, Toast.LENGTH_LONG).show();
    }
}
