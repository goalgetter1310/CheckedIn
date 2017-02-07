package com.checkedin.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.volley.WebServiceCall;


public class HelpFrg extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frg_help, container, false);
        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_help));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                WebView wvTermsCondition = (WebView) view.findViewById(R.id.wv_help);
                wvTermsCondition.loadUrl(WebServiceCall.WS_HELP);
            }
        }, 700);
        return view;
    }
}
