package com.checkedin.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.checkedin.R;
import com.checkedin.model.response.BaseModel;
import com.checkedin.model.response.LastCheckinModel;
import com.checkedin.utility.UserPreferences;
import com.checkedin.volley.VolleyStringRequest;
import com.checkedin.volley.WebServiceCall;
import com.material.widget.TextView;

public class CheckinConfirmActivity extends Activity implements OnClickListener, VolleyStringRequest.AfterResponse {

    private WebServiceCall webServiceCall;
    private String placeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkin_confirm);

        TextView tvYes = (TextView) findViewById(R.id.btn_yes);
        TextView tvNo = (TextView) findViewById(R.id.btn_no);
        webServiceCall = new WebServiceCall(this);
        webServiceCall.lastCheckinUserWsCall(this);

        tvYes.setOnClickListener(this);
        tvNo.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                finish();
                break;

            case R.id.btn_no:
                UserPreferences.removeLastCheckinTime(this);
                webServiceCall.removeLastCheckinUserWsCall(this, placeId);
                break;
        }
    }



    @Override
    public void onResponseReceive(int requestCode) {
        switch (requestCode) {
            case WebServiceCall.REQUEST_CODE_REMOVE_LAST_CHECKIN_USER:
                BaseModel mFuturePopup = (BaseModel) webServiceCall.volleyRequestInstatnce().getModelObject(BaseModel.class, CheckinConfirmActivity.class.getSimpleName());
                if (mFuturePopup != null && mFuturePopup.getStatus() == BaseModel.STATUS_SUCCESS) {
                    Toast.makeText(this, mFuturePopup.getMessage(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, getString(R.string.server_connect_error), Toast.LENGTH_LONG).show();
                }
                finish();
                break;
            case WebServiceCall.REQUEST_CODE_LAST_CHECKIN_USER:
                LastCheckinModel mLastCheckin = (LastCheckinModel) webServiceCall.volleyRequestInstatnce().getModelObject(LastCheckinModel.class, LastCheckinModel.class.getSimpleName());
                if (mLastCheckin != null && mLastCheckin.getStatus() == BaseModel.STATUS_SUCCESS) {
                    placeId = mLastCheckin.getLastCheckin().getPlaceId();
                }
                break;
        }
    }

    @Override
    public void onErrorReceive() {
        Toast.makeText(this, getString(R.string.server_connect_error), Toast.LENGTH_LONG).show();
    }
}
