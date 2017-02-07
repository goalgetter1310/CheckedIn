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

public class AboutUsFrg extends Fragment {
    private WebView wvTermsCondition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frg_about_us, container, false);
        ((MainActivity) getActivity()).toggleActionBarIcon(1, true);
        ((MainActivity) getActivity()).showSearch(View.GONE);
        ((MainActivity) getActivity()).setToolbarTitle(getString(R.string.title_about_us));


        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        wvTermsCondition = (WebView) view.findViewById(R.id.wv_about);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                wvTermsCondition.loadUrl(WebServiceCall.WS_ABOUT_US);
            }
        }, 500);
    }

    @Override
    public void onDestroyView() {
        wvTermsCondition.destroy();
        super.onDestroyView();

    }
}
