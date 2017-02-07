package com.checkedin.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.checkedin.R;
import com.checkedin.activity.MainActivity;
import com.checkedin.volley.WebServiceCall;

public class TermsConditionFrg extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.frg_terms_condition, container, false);

        ((MainActivity) getActivity()).toggleActionBarIcon(0, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_terms_condition));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                WebView wvTermsCondition = (WebView) view.findViewById(R.id.wv_term_condition);
                wvTermsCondition.loadUrl(WebServiceCall.WS_TC);
            }
        }, 700);
        return view;
    }
}
