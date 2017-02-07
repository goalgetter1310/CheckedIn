package com.checkedin.volley;

import android.content.Context;
import android.support.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.checkedin.AppController;
import com.checkedin.dialog.ProgressDialog;
import com.checkedin.utility.Utility;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.util.TextUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class VolleyStringRequest implements Listener<String>,
        Response.ErrorListener {


    private String error;
    private String response;
    private AfterResponse afterResponce;
    private ProgressDialog dialog;
    private int requestCode;

    public VolleyStringRequest(AfterResponse afterResponse) {
        this.afterResponce = afterResponse;
    }

    public String getResponse() {
        return response;
    }

    public String getError() {
        return error;
    }


    public void volleyRequest(Context context, String url,
                              final HttpEntity entity, int requestCode) {
        this.requestCode = requestCode;
        if (context != null) {
            dialog = new ProgressDialog(context);
        }
        StringRequest strReq = new StringRequest(Request.Method.POST, url,
                this, this) {
            @Override
            public String getBodyContentType() {
                return entity.getContentType().getValue();
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                try {
                    entity.writeTo(bos);
                } catch (IOException e) {
                    e.printStackTrace();
                    VolleyLog.e("IOException writing to ByteArrayOutputStream");
                }
                return bos.toByteArray();

            }
        };
        if (dialog != null)
            dialog.show();
        Utility.logUtils("Url: " + url);

        strReq.setRetryPolicy(new DefaultRetryPolicy(
                DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq,
                AppController.STRING_RESP);
    }


    public void volleyRequest(Context context, int method,
                              @NonNull String url, final Map<String, String> params,
                              int requestCode) {
        this.requestCode = requestCode;
        StringRequest strReq = new StringRequest(method, url,
                this, this) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }

        };

        if (context != null) {
            dialog = new ProgressDialog(context);
            dialog.setCancelable(false);
            dialog.show();
        }

        Utility.logUtils("Url: " + url);
        if (params != null) {
            for (String key : params.keySet()) {
                Utility.logUtils("Params_" + key + ": " + params.get(key));
            }
        }
        strReq.setRetryPolicy(new DefaultRetryPolicy(
                3000, 1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(strReq,
                String.valueOf(requestCode));

    }

    public Object getModelObject(Class<?> classType, String printLog) {
        Object classModel = null;

        Gson gson = new GsonBuilder().create();
        if (response != null) {
            String strResponse = response;
            Utility.logUtils(printLog + "-->" + response);
            try {
                classModel = gson.fromJson(strResponse, classType);
            } catch (JsonSyntaxException je) {
                je.printStackTrace();
            }
        }
        return classModel;
    }

    @Override
    public void onResponse(String response) {
        Utility.logUtils(response);
        this.response = response;
        if (dialog != null) {
            dialog.dismiss();
        }

        try {
            if (afterResponce != null) {
                afterResponce.onResponseReceive(requestCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        if (dialog != null) {
            dialog.dismiss();
        }
        this.error = error.getMessage();
        if (afterResponce != null) {
            afterResponce.onErrorReceive();
        }
    }

    public interface AfterResponse {
        void onResponseReceive(int requestCode);

        void onErrorReceive();
    }

    public void clearCache() {
        AppController.getInstance().clearCache();
    }

    public void cancelAllRequest(int tag) {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        afterResponce = null;
        AppController.getInstance().cancelPendingRequests(TextUtils.isEmpty(String.valueOf(tag)) ?
                AppController.STRING_RESP : tag);
    }
}
